package views;

import controllers.LoginRegisterController;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by Shane on 19/03/2017.
 */
public class LoginRegisterView extends View<LoginRegisterController> {

    private TabPane root;
    private Tab loginTab;
    private VBox loginTabPage;
    private Tab registerTab;
    private VBox registerTabPage;

    public LoginRegisterView() {
        this(new Stage());
    }

    public LoginRegisterView(Stage window) {
        super(window);
        assignChildren();
        assignController(new LoginRegisterController(this));
    }

    @Override
    public TabPane getRoot() {
        return root;
    }

    public void setRoot(TabPane root) {
        this.root = root;
    }

    public Tab getLoginTab() {
        return loginTab;
    }

    public void setLoginTab(Tab loginTab) {
        this.loginTab = loginTab;
    }

    public VBox getLoginTabPage() {
        return loginTabPage;
    }

    public void setLoginTabPage(VBox loginTabPage) {
        this.loginTabPage = loginTabPage;
    }

    public Tab getRegisterTab() {
        return registerTab;
    }

    public void setRegisterTab(Tab registerTab) {
        this.registerTab = registerTab;
    }

    public VBox getRegisterTabPage() {
        return registerTabPage;
    }

    public void setRegisterTabPage(VBox registerTabPage) {
        this.registerTabPage = registerTabPage;
    }

    @Override
    public void assignChildren() {
        setRoot(new TabPane());
        getRoot().setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        setLoginTab(new Tab());
        getLoginTab().setText("Log In");
        getRoot().getTabs().add(getLoginTab());

        setLoginTabPage(new LoginView(this).getRoot());
        getLoginTab().setContent(getLoginTabPage());

        setRegisterTab(new Tab());
        getRegisterTab().setText("Register");
        getRoot().getTabs().add(getRegisterTab());

        setRegisterTabPage(new RegisterView(this).getRoot());
        getRegisterTab().setContent(getRegisterTabPage());

        getWindow().initStyle(StageStyle.UTILITY);
        getWindow().setTitle("Login/Register - Dentistry");
        getWindow().setScene(new Scene(root));
        getWindow().setResizable(false);
        getWindow().sizeToScene();
    }

}
