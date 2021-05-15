package parkman.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class OverviewController implements Initializable {
    public GridPane gridPaneParking;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for(int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Image image = new Image("img/carBlack.png");
                ImageView iv1 = new ImageView();
                iv1.setImage(image);
                iv1.setFitWidth(30);
                iv1.setPreserveRatio(true);
                gridPaneParking.add(iv1,j,i);
            }

        }

    }

}
