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

public class ScanInDialogController {
    @FXML
    public ImageView testPictureFrame;
    public ImageView cameraFeedFrame;

    public Button testPictureBtn;
    public Button continueButton;

    public RadioButton testPictureRadio;
    public RadioButton webcamRadio;

    public Label loadingLabel;

    public File tempPng = new File("temp.png");

    private File selectedImgFile = null;

    private MainController mainController;
    private ScanController scanController = new ScanController();

    public ScanInDialogController(MainController _mainController) {
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

    public void TestPictureRadioAction() {
        webcamRadio.setSelected(false);
    }

    public void WebcamRadioAction() {
        testPictureRadio.setSelected(false);
    }

    public String GetSelectedInput() {
        return testPictureRadio.isSelected() ? "test" : webcamRadio.isSelected() ? "webcam" : null;
    }

    public void ContinueButtonAction() {
        loadingLabel.setOpacity(1);

        String selection = this.GetSelectedInput();
        if(selection == null) {
            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.close();
            return;
        }

        File imageFile = null;
        if(selection.equals("test")) {
            imageFile = this.getImageFile();
        }

        try {
            scanController.AddTransaction(imageFile);
            mainController.ListTransactions();

            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
