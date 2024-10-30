package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Transaction;
import model.generator.DataGenerator;
import presenter.TransactionEditDialogPresenter;

import java.io.IOException;

public class AccountAppController {

    private Stage primaryStage;

    public AccountAppController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initRootLayout() {
        try {
            this.primaryStage.setTitle("My second JavaFX app");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AccountAppController.class.getResource("../view/AccountOverviewPane.fxml"));
            BorderPane rootLayout = loader.load();

            // set initial data into controller
            AccountOverviewController controller = loader.getController();
            controller.setAppController(this);
            controller.setData(DataGenerator.generateAccountData());

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }

    }

    public boolean showTransactionEditDialog(Transaction transaction) {
        try {
            // Load the fxml file and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AccountAppController.class.getResource("../view/TransactionEditDialog.fxml"));
            BorderPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit transaction");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the transaction into the presenter.
            TransactionEditDialogPresenter presenter = loader.getController();
            presenter.setDialogStage(dialogStage);
            presenter.setData(transaction);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return presenter.isApproved();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
