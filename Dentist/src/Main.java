import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import managers.SessionManager;
import views.LoginRegisterView;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        SessionManager.load(); // Static class. Loads the session that will be used throughout the program's lifetime. Self-explanatory really.
        new LoginRegisterView(primaryStage).getWindow().show(); // Create new instance of loginregisterview. use primaryStage as the stage for it.
                                                                // Then show the stage, as it has been populated with nodes and a controller.
    }

    public static void main(String[] args) {
        launch(args);
    }
}
