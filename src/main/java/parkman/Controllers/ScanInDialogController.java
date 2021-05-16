package parkman.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScanInDialogController {
    @FXML
    public ImageView testPictureFrame;
    public ImageView cameraFeedFrame;
    public Button testPictureBtn;
    public Button cameraaFeedBtm;
    public File tempPng = new File("temp.png");
    public Button continueButton;
    public Label loadingLabel;

    private File selectedImgFile = null;

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
        }
    }

    public void ContinueButtonAction() {
        loadingLabel.setOpacity(1);
        new Thread(() -> {
            Platform.runLater(() -> {
                Stage stage = (Stage) continueButton.getScene().getWindow();
                stage.hide();
            });
        }).start();

    }
}
