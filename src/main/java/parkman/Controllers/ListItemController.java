package parkman.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import parkman.DAO.ParkmanDAO;
import parkman.Models.Transaction;

import java.io.ByteArrayInputStream;


public class ListItemController {
    private ParkmanDAO dao;

    public Label idLabel;
    public Label plateLabel;
    public Label enteanceDateLabel;
    public Label exitDateLabel;
    public Label lblParkingSpot;

    private Transaction transaction;

    public ListItemController(Transaction _transaction) {
        transaction = _transaction;
        dao = ParkmanDAO.getInstance();
    }

    @FXML
    public void initialize() {
        idLabel.setText("#" + transaction.getId());
        plateLabel.setText(transaction.getPlateNumber());
        enteanceDateLabel.setText(transaction.getFormattedEnteranceTimestamp());
        exitDateLabel.setText(transaction.getExitTimestamp() == null ? "ACTIVE" : transaction.getFormattedExitTimestamp());
        lblParkingSpot.setText(transaction.getParkingSpot());

    }

    public void getPlate() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        ImageView imageView = new ImageView();
        imageView.setImage(transaction.getPlatePicture() == null ?
                new Image("/img/photo_placeholder.png") :
                new Image(new ByteArrayInputStream(transaction.getPlatePicture())));
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
        alert.setWidth(200);
        alert.setGraphic(imageView);
        alert.setHeaderText("Plate picture of the given car:");
        alert.setContentText("");
        alert.setTitle("Plate picture");

        alert.showAndWait();
    }

    public void getCar() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        ImageView imageView = new ImageView();
        imageView.setImage(transaction.getCarPicture() == null ?
                new Image("/img/photo_placeholder.png") :
                new Image(new ByteArrayInputStream(transaction.getCarPicture())));
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
        alert.setGraphic(imageView);
        alert.setHeaderText("Car picture of the given car:");
        alert.setContentText("");
        alert.setTitle("Car picture");
        alert.showAndWait();
    }

}
