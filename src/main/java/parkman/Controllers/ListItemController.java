package parkman.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import parkman.Models.Transaction;

public class ListItemController {

    public Label idLabel;
    public Label plateLabel;
    public Label enteanceDateLabel;
    public Label exitDateLabel;

    private Transaction transaction;

    public ListItemController(Transaction _transaction) {
        transaction = _transaction;
    }

    @FXML
    public void initialize() {
        idLabel.setText("#" + transaction.getId());
        plateLabel.setText(transaction.getPlateNumber());
        enteanceDateLabel.setText(transaction.getFormattedEnteranceTimestamp());
        exitDateLabel.setText(transaction.getExitTimestamp() == null ? "ACTIVE" : transaction.getFormattedExitTimestamp());
    }

}
