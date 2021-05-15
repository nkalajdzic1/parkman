package parkman.Controllers;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import parkman.DAO.ParkmanDAO;

import java.util.Base64;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;
import parkman.Models.Transaction;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MainController implements Initializable {
    private ParkmanDAO dao;
    private ArrayList<Transaction> tranasctions;

    public VBox itemHolder;
    public Button scanInBtn;
    public Button scanOutBtn;
    public Button overviewBtn;
    public Button exitBtn;

    public MainController() {
        dao = ParkmanDAO.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tranasctions = dao.getTransactions();

        for(Transaction transaction : tranasctions) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/list_item.fxml"));
                ListItemController ctrl = new ListItemController(transaction);
                loader.setController(ctrl);

                itemHolder.getChildren().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void scanInBtnAction() throws IOException, InterruptedException {
        Webcam webcam = Webcam.getDefault();
        webcam.open();

        BufferedImage bfi = true == true ? ImageIO.read(new File("/home/mula/Projects/Python/parkman/ParkmanUI/AI/src/images/slika4.jpg")) : webcam.getImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bfi, "jpeg", baos);

        int insertId = dao.addInitialTransaction(
                new Transaction(
                        baos.toByteArray(),
                        new Timestamp(System.currentTimeMillis()),
                        2.4f,
                        "test"
                )
            );
        System.out.println(insertId);

//        String databasePath = new File("db/parkmanDb.sqlite").getPath();
//        int insertId = 10;
//
//        String command = "python ./AI/src/main.py " + databasePath + " " +  insertId;
//        System.out.println(command);
//        Process p = Runtime.getRuntime().exec(command);
//        p.waitFor();
//        BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//        String line;
//
//        while ((line = bri.readLine()) != null) {
//            System.out.println(line);
//        }
//        bri.close();
//        while ((line = bre.readLine()) != null) {
//            System.out.println(line);
//        }
//        bre.close();
//        p.waitFor();
//        System.out.println("Done.");
//
//        p.destroy();
    }

    public void scanOutBtnAction() {

    }

    public void overviewBtnAction() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/overview.fxml"));
        OverviewController overviewController = new OverviewController();
        Parent root  = loader.load();
        stage.setTitle("Overview");
        stage.setScene(new Scene(root, 900, 500));
        stage.setResizable(false);
        stage.show();
    }

    public void exitBtnAction() {
        Platform.exit();
    }

}
