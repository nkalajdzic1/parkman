package parkman.Controllers;

import com.github.sarxos.webcam.Webcam;
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

    public void AddTransaction() throws IOException, InterruptedException {
        byte[] imageBytes = GetWebcamImageBytes();

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

    public byte[] GetWebcamImageBytes() {
        Webcam webcam = Webcam.getDefault();
        webcam.open();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            BufferedImage bfi = true == true ? ImageIO.read(new File("/home/mula/Desktop/Projects/Piton/Parkman/ParkmanUI/AI/src/images/slika4.jpg")) : webcam.getImage();
            ImageIO.write(bfi, "jpeg", baos);

            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
