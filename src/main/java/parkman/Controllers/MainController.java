package parkman.Controllers;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

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

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;


public class MainController implements Initializable {
    private ParkmanDAO dao;
    private ArrayList<Transaction> tranasctions;
    private ScanController scanController;

    public VBox itemHolder;
    public Button scanInBtn;
    public Button scanOutBtn;
    public Button overviewBtn;
    public Button exitBtn;

    public enum ScanInAction {
        TEST,
        WEBCAM
    }

    public MainController() {
        dao = ParkmanDAO.getInstance();
        scanController = new ScanController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ListTransactions();
    }

    private void ListTransactions() {
        tranasctions = dao.getTransactions();
        itemHolder.getChildren().clear();

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
        Stage stage = new Stage();
        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/scanDialog.fxml"));
            ScanInDialogController scanInDialogController = new ScanInDialogController();
            loader.setController(scanInDialogController);
            root = loader.load();
            stage.setTitle("Select Input Source");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(true);
            stage.show();

            stage.setOnHiding(event -> {
                File imageFile = scanInDialogController.getImageFile();
                try {
                    scanController.AddTransaction(imageFile);
                    this.ListTransactions();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scanOutBtnAction() {
        Stage stage = new Stage();
        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/scanDialog.fxml"));
            ScanInDialogController scanInDialogController = new ScanInDialogController();
            loader.setController(scanInDialogController);
            root = loader.load();
            stage.setTitle("Select Input Source");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(true);
            stage.show();

            stage.setOnHiding(event -> {
                File imageFile = scanInDialogController.getImageFile();
                try {
//                    scanController.AddTransaction(imageFile);
//                    this.ListTransactions();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
