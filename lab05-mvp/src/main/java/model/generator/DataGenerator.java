package model.generator;

import java.math.BigDecimal;
import java.time.LocalDate;

import model.Account;
import model.Category;
import model.Transaction;

public class DataGenerator {

	public final static Account generateAccountData() {
		Account account = new Account("Personal account");
		Category income = new Category("Income");
		Category groceries = new Category("Groceries");
		Category fuel = new Category("Fuel");
		
		account.addTransaction(new Transaction(LocalDate.of(2015, 8, 24), "My company", income,  BigDecimal.valueOf(1000L)));
		account.addTransaction(new Transaction(LocalDate.of(2015, 8, 25), "Greenrocesr's", groceries,  BigDecimal.valueOf(973L)));
		account.addTransaction(new Transaction(LocalDate.of(2015, 8, 26), "Petrol station", fuel,BigDecimal.valueOf(727L)));
		
		return account;
	}
}
