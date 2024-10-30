package presenter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

import model.Category;
import model.Transaction;

public class TransactionEditDialogPresenter {

    private Transaction transaction;

    @FXML
    private TextField dateTextField;

    @FXML
    private TextField payeeTextField;

    @FXML
    private TextField categoryTextField;

    @FXML
    private TextField inflowTextField;

    private Stage dialogStage;

    private boolean approved;

    private LocalDateStringConverter converter;

    @FXML
    public void initialize() {
        String pattern = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        converter = new LocalDateStringConverter(formatter, formatter);

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(Transaction transaction) {
        this.transaction = transaction;
        updateControls();
    }

    public boolean isApproved() {
        return approved;
    }

    @FXML
    private void handleOkAction(ActionEvent event) {
        updateModel();
        approved = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        dialogStage.close();
    }

    private void updateModel() {
        transaction.setDate(converter.fromString(dateTextField.getText()));
        transaction.setPayee(payeeTextField.getText());
        transaction.setCategory(new Category(categoryTextField.getText()));
        DecimalFormat decimalFormatter = new DecimalFormat();
        decimalFormatter.setParseBigDecimal(true);
        try {
            transaction.setInflow((BigDecimal) decimalFormatter.parse(inflowTextField.getText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void updateControls() {
        dateTextField.setText(converter.toString(transaction.getDate()));
        payeeTextField.setText(transaction.getPayee());
        categoryTextField.setText(transaction.getCategory().getName());
        inflowTextField.setText(transaction.getInflow().toString());
    }
}
