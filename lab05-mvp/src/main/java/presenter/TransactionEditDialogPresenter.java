package presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import model.Category;
import model.Transaction;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
		try {
			transaction.setPayee(payeeTextField.getText());
			transaction.setCategory(new Category(categoryTextField.getText()));
			transaction.setInflow(convertToBigDecimal(inflowTextField.getText()));
			transaction.setDate(convertToLocalDate(dateTextField.getText()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private LocalDate convertToLocalDate(String localDateString) {
		String pattern = "yyyy-MM-dd";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		LocalDateStringConverter converter = new LocalDateStringConverter(formatter, formatter);
		return converter.fromString(localDateString);
	}

	private String convertLocalDateToString(LocalDate localDate)  {
		String pattern = "yyyy-MM-dd";
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
		LocalDateStringConverter converter = new LocalDateStringConverter(dateFormatter, dateFormatter);
		return converter.toString(localDate);
	}

	private BigDecimal convertToBigDecimal(String decimalString) throws ParseException {
		DecimalFormat decimalFormatter = new DecimalFormat();
		decimalFormatter.setParseBigDecimal(true);
		return (BigDecimal) decimalFormatter.parse(decimalString);
	}

	private void updateControls() {
		dateTextField.setText(convertLocalDateToString(transaction.getDate()));
		payeeTextField.setText(transaction.getPayee());
		categoryTextField.setText(transaction.getCategory().toString());
		inflowTextField.setText(transaction.getInflow().toString());
	}
}
