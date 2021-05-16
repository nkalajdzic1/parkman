package parkman.Controllers;

import com.github.sarxos.webcam.Webcam;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import parkman.DAO.ParkmanDAO;
import parkman.Models.Transaction;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Timestamp;
import java.util.Date;

public class ScanController {

    private ParkmanDAO dao;

    public ScanController() {
        dao = ParkmanDAO.getInstance();
    }

    public byte[] GetWebcamImageBytes() {
        Webcam webcam = Webcam.getDefault();
        webcam.open();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ImageIO.write(webcam.getImage(), "png", baos);
            webcam.close();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            webcam.close();
        }

        return null;
    }

    public byte[] GetImageBytes(File imageFile) {
        try {
            BufferedImage bImage = ImageIO.read(imageFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", bos);
            return bos.toByteArray();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void AddTransaction(File imageFile) throws Exception {
        byte[] imageBytes = imageFile == null ? GetWebcamImageBytes() : GetImageBytes(imageFile);

        if(imageBytes == null) {
            throw new Exception("FXML:Image source not found!");
        };

        int insertId = dao.addInitialTransaction(
                       new Transaction(
                            imageBytes,
                            new Timestamp(System.currentTimeMillis()),
                            2.4f,
                            OverviewController.GetFirstEmptySpot(dao.getTransactions())
                        )
                    );

        if(insertId == -1) {
            throw new Exception("FXML:Failed to insert transaction in database!");
        };

        String plateNumber = RunPythonInterpreter(insertId);

        if(plateNumber == null) {
            dao.deleteTransaction(insertId);
            throw new Exception("FXML:Plate number could not be extracted!");
        }

        if(!dao.updatePlateById(insertId, plateNumber))
            throw new Exception("FXML: Failed to update plate with #ID" + insertId + "!");
    }

    public void UpdateExit(File imageFile) throws Exception {
        byte[] imageBytes = imageFile == null ? GetWebcamImageBytes() : GetImageBytes(imageFile);

        if(imageBytes == null) {
            throw new Exception("FXML:Image source not found!");
        };

        String plateNumber = RunPythonInterpreter(-1);

        if(plateNumber == null) {
            throw new Exception("FXML:Plate number could not be extracted!");
        };

        if(!dao.updateExitTimestampQuery(plateNumber, new Timestamp(new Date().getTime())))
            throw new Exception("FXML: Entrance record for that car does not exist!");
    }

    public String RunPythonInterpreter(int insertId) throws InterruptedException, IOException {
        String databasePath = new File("db/parkmanDb.sqlite").getPath();
        Process p;

        if(insertId != -1)
            p = Runtime.getRuntime().exec("python ./AI/src/main.py INSERT " + databasePath + " " +  insertId);
        else
            p = Runtime.getRuntime().exec("python ./AI/src/main.py UPDATE ./temp/holder.png");

        p.waitFor();

        BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line ;
        while ((line = bri.readLine()) != null) {
            String[] split = line.split(":");

            if(split[0].equals("SUCCESS")) {
                bri.close();
                p.waitFor();
                p.destroy();
                return split[1];
            }
        }

        bri.close();
        p.waitFor();
        p.destroy();

        return null;
    }
}
