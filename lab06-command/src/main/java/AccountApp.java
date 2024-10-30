import javafx.application.Application;
import javafx.stage.Stage;
import controller.AccountAppController;

public class AccountApp extends Application {

	private Stage primaryStage;
	
	private AccountAppController appController;

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("My first JavaFX app");

		this.appController = new AccountAppController(primaryStage);
		this.appController.initRootLayout();

	}

	public static void main(String[] args) {
		launch(args);
	}


}
