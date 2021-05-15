package parkman.Controllers;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
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

import java.util.ResourceBundle;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;
import parkman.Models.Transaction;


public class MainController implements Initializable {
    private ParkmanDAO dao;
    private ArrayList<Transaction> tranasctions;
    private ScanController scanController;

    public VBox itemHolder;
    public Button scanInBtn;
    public Button scanOutBtn;
    public Button overviewBtn;
    public Button exitBtn;

    public MainController() {
        dao = ParkmanDAO.getInstance();
        scanController = new ScanController();
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

    public void scanInBtnAction() {
        try {
            scanController.AddTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scanOutBtnAction() {

    }

    public void overviewBtnAction() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/overview.fxml"));
        OverviewController overviewController = new OverviewController();
        Parent root  = loader.load();
        stage.setTitle("Overview");
        stage.setScene(new Scene(root, 500, 500));
        stage.setResizable(false);
        stage.show();
    }

    public void exitBtnAction() {
        Platform.exit();
    }

}
