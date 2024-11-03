package command;

import javafx.collections.ObservableList;
import model.Account;
import model.Transaction;

import java.util.List;

public class RemoveTransactionCommand implements Command {

    private final List<Transaction> transactionsToRemove;
    private final int numberOfTransactionsToRemove;
    private final Account account;

    public RemoveTransactionCommand(List<Transaction> transactionsToRemove, Account account) {
        this.transactionsToRemove = transactionsToRemove;
        this.numberOfTransactionsToRemove = transactionsToRemove.size();
        this.account = account;
    }

    @Override
    public void execute() {
        account.getTransactions().removeAll(transactionsToRemove);
    }

    @Override
    public String getName() {
        return numberOfTransactionsToRemove + " transactions removed";
    }

    @Override
    public void undo() {
        account.getTransactions().addAll(transactionsToRemove);
    }

    @Override
    public void redo() {
        account.getTransactions().removeAll(transactionsToRemove);
    }
}
