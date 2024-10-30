package model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Transaction {
	
	private static final String EMPTY = "";
	
	private ObjectProperty<LocalDate> date;
	private StringProperty payee;
	private ObjectProperty<Category> category;
	private ObjectProperty<BigDecimal> inflow;

	
	private Transaction() {
		this(LocalDate.now(), EMPTY, new Category(EMPTY),  BigDecimal.ZERO);
	}
	
	public Transaction(LocalDate date, String payee, Category category, BigDecimal inflow) {
		this.date = new SimpleObjectProperty<>(date);
		this.payee = new SimpleStringProperty(payee);
		this.category = new SimpleObjectProperty<>(category);
		this.inflow = new SimpleObjectProperty<>(inflow);
	}

	public final LocalDate getDate() {
		return date.get();
	}

	public final void setDate(LocalDate date) {
		this.date.set(date);
	}
	
	public final ObjectProperty<LocalDate> getDateProperty() {
		return date;
	}

	public final String getPayee() {
		return payee.get();
	}
	
	public final void setPayee(String payee) {
		this.payee.set(payee);
	}
	
	public final StringProperty getPayeeProperty() {
		return payee;
	}
	
	public final Category getCategory() {
		return category.get();
	}
	
	
	public final void setCategory(Category category) {
		this.category.set(category);
	}

	public final ObjectProperty<Category> getCategoryProperty() {
		return category;
	}
	

	public final BigDecimal getInflow() {
		return inflow.get();
	}

	public final void setInflow(BigDecimal inflow) {
		this.inflow.set(inflow);
	}
	
	public final ObjectProperty<BigDecimal> getInflowProperty() {
		return inflow;
	}

	public static final Transaction newTransaction() {
		return new Transaction();
	}
}
