package parkman.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScanOutDialogController {
    @FXML
    public ImageView testPictureFrame;
    public ImageView cameraFeedFrame;

    public Button testPictureBtn;
    public Button continueButton;

    public RadioButton testPictureRadio;
    public RadioButton webcamRadio;

    public Label statusLabel;

    public File tempPng = new File("temp/holder.png");

    private File selectedImgFile = null;

    private MainController mainController;
    private ScanController scanController = new ScanController();

    public ScanOutDialogController(MainController _mainController) {
        this.mainController = _mainController;
    }

    @FXML
    public void initialize() {
        testPictureFrame.setImage(new Image("/img/photo_placeholder.png"));
    }

    public File getImageFile() {
        return selectedImgFile;
    }

    public void testPictureBtnAction() throws IOException {
        FileChooser.ExtensionFilter imageFilter
                = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setTitle("Select Test Image");

        selectedImgFile = fileChooser.showOpenDialog(testPictureBtn.getScene().getWindow());
        if (selectedImgFile != null) {
            BufferedImage bufferedImage = ImageIO.read(selectedImgFile);
            ImageIO.write(bufferedImage, "png", tempPng);
            testPictureFrame.setImage(new Image(tempPng.toURI().toURL().toString()));
            selectedImgFile = tempPng;
            testPictureRadio.setSelected(true);
        }
    }

    public String GetSelectedInput() {
        return testPictureRadio.isSelected() ? "test" : webcamRadio.isSelected() ? "webcam" : null;
    }

    public void TestPictureRadioAction() {
        webcamRadio.setSelected(false);
    }

    public void WebcamRadioAction() {
        testPictureRadio.setSelected(false);
    }

    public void ContinueButtonAction() {
        statusLabel.setStyle("-fx-text-fill: black");
        statusLabel.setText("Loading...");

        new Thread(this::UpdateExitTime).start();
    }

    private void UpdateExitTime() {
        String selection = this.GetSelectedInput();

        if(selection == null) {
            Platform.runLater(() -> {
                Stage stage = (Stage) continueButton.getScene().getWindow();
                stage.close();
            });
            return;
        }

        File imageFile = null;
        if(selection.equals("test")) {
            imageFile = this.getImageFile();
        }

        try {
            scanController.UpdateExit(imageFile);

            Platform.runLater(() -> {
                mainController.ListTransactions();
                Stage stage = (Stage) continueButton.getScene().getWindow();
                stage.close();
            });
        } catch(Exception e) {
            String[] split = e.getMessage().split(":");

            if(split[0].equals("FXML")) {
                Platform.runLater(() -> {
                    statusLabel.setText(split[1]);
                    statusLabel.setStyle("-fx-text-fill: red");
                });
            } else {
                e.printStackTrace();
            }
        }
    }
}
