package parkman.Controllers;

import java.io.File;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.io.IOException;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import parkman.DAO.ParkmanDAO;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;
import parkman.Models.Transaction;

import javax.imageio.ImageIO;

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

    public void scanInBtnAction() throws IOException {
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        System.out.println(webcam.getImageBytes().remaining());
        dao.addInitialTransaction(
            new Transaction(
                    webcam.getImageBytes().array(),
                    new Timestamp(System.currentTimeMillis()),
                    2.4f,
                "test"
            )
        );
    }

    public void scanOutBtnAction() {

    }

    public void overviewBtnAction() {

    }

    public void exitBtnAction() {
        Platform.exit();
    }

}
