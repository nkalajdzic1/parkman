package parkman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import parkman.Controllers.MainController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        MainController ctrl = new MainController();
        loader.setController(ctrl);

        Parent root = loader.load();
        primaryStage.setTitle("Parkman");
        primaryStage.setScene(new Scene(root, 850, 450));
        primaryStage.setMinWidth(850);
        primaryStage.setMinHeight(450);
        primaryStage.setMaxWidth(850);
        primaryStage.setMaxHeight(450);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
