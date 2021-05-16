package parkman.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import parkman.DAO.ParkmanDAO;
import parkman.Models.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.python.google.common.collect.FluentIterable.from;

public class OverviewController implements Initializable {
    private ParkmanDAO dao;
    private ArrayList<Transaction> transactions;
    private String firstAvailableCache;

    public GridPane gridPaneParking;
    public Label lblFreeSpot;

    public OverviewController() { dao = ParkmanDAO.getInstance(); }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gridPaneParking.setVgap(50);

        transactions = dao.getTransactions();
        char letter = 'A';
        char number = '1';

        for(int i = 1; i < 7; i++) {
            gridPaneParking.add(new Label(letter++ + ""),i,0);
            gridPaneParking.add(new Label(number++ + ""),0,i);
        }

        letter = 'A';
        number = '1';
        String firstAvaible = "";

        for(int i = 1; i < 7; i++) {
            for (int j = 1; j < 7; j++) {
                String parking = "";
                parking += letter;
                parking += number;

                String finalParking = parking;
                Collection<Transaction> coll = from(transactions).filter(x -> {
                    assert x != null;
                    return x.getExitTimestamp() == null && x.getParkingSpot().equals(finalParking);
                }).toList();

                Image image = null;
                if(firstAvaible.isEmpty() && coll.isEmpty()) {
                    image = new Image("img/carGreen.png");
                    firstAvaible = Character.toString(letter) + number;
                } else if(coll.isEmpty())
                    image = new Image("img/carBlack.png");
                else
                    image = new Image("img/carRed.png");

                ImageView iv1 = new ImageView();
                iv1.setImage(image);
                iv1.setFitWidth(30);
                iv1.setPreserveRatio(true);
                gridPaneParking.add(iv1,j,i);

                letter++;
            }
            number++;
            letter = 'A';
        }

        firstAvailableCache = firstAvaible;
        lblFreeSpot.setText("First avaible spot is " + firstAvaible + ".");
        lblFreeSpot.setStyle("-fx-font-weight: bold;");
    }

    public static String GetFirstEmptySpot(ArrayList<Transaction> transactions) {
        char letter = 'A';
        char number = '1';

        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 7; j++) {
                String parking = "";
                parking += letter;
                parking += number;

                String finalParking = parking;
                Collection<Transaction> coll = from(transactions).filter(x -> {
                    assert x != null;
                    return x.getExitTimestamp() == null && x.getParkingSpot().equals(finalParking);
                }).toList();

                if (coll.isEmpty()) {
                    return Character.toString(letter) + number;
                }

                ++letter;
            }
            ++number;
            letter = 'A';
        }

        return "null";
    }
}
