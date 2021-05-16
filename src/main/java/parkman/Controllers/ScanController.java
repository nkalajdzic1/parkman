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
            ImageIO.write(webcam.getImage(), "jpeg", baos);
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
            ImageIO.write(bImage, "jpg", bos);
            return bos.toByteArray();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void AddTransaction(File imageFile) throws IOException, InterruptedException {
        byte[] imageBytes = imageFile == null ? GetWebcamImageBytes() : GetImageBytes(imageFile);

        if(imageBytes == null) return;

        int insertId = dao.addInitialTransaction(
                       new Transaction(
                            imageBytes,
                            new Timestamp(System.currentTimeMillis()),
                            2.4f,
                            "test"
                        )
                    );

        if(insertId == -1) return;;

        String plateNumber = RunPythonInterpreter(insertId);

        if(plateNumber == null) return;

        dao.updatePlateById(insertId, plateNumber);
    }

    public String RunPythonInterpreter(int insertId) throws InterruptedException, IOException {
        String databasePath = new File("db/parkmanDb.sqlite").getPath();

        Process p = Runtime.getRuntime().exec("python  ./AI/src/main.py " + databasePath + " " +  insertId);

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
