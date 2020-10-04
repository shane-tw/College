package views;

import controllers.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by Shane on 19/03/2017.
 */
public class LoginView extends View<LoginController> {

    private LoginRegisterView loginRegisterView;
    private VBox root;
    private GridPane gridPane;
    private Label nameLabel;
    private ComboBox nameSelect;
    private Label passwordLabel;
    private PasswordField passwordField;
    private HBox loginBtnHBox;
    private Label failureText;
    private Button loginBtn;

    public LoginView(LoginRegisterView loginRegisterView) {
        this(new Stage(), loginRegisterView);
    }

    public LoginView(Stage window, LoginRegisterView loginRegisterView) {
        super(window);
        setLoginRegisterView(loginRegisterView);
        assignChildren();
        assignController(new LoginController(this));
    }

    public LoginRegisterView getLoginRegisterView() {
        return loginRegisterView;
    }

    public void setLoginRegisterView(LoginRegisterView loginRegisterView) {
        this.loginRegisterView = loginRegisterView;
    }

    @Override
    public VBox getRoot() {
        return root;
    }

    public void setRoot(VBox root) {
        this.root = root;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }

    public Label getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(Label nameLabel) {
        this.nameLabel = nameLabel;
    }

    public ComboBox getNameSelect() {
        return nameSelect;
    }

    public void setNameSelect(ComboBox nameSelect) {
        this.nameSelect = nameSelect;
    }

    public Label getPasswordLabel() {
        return passwordLabel;
    }

    public void setPasswordLabel(Label passwordLabel) {
        this.passwordLabel = passwordLabel;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(PasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public HBox getLoginBtnHBox() {
        return loginBtnHBox;
    }

    public void setLoginBtnHBox(HBox loginBtnHBox) {
        this.loginBtnHBox = loginBtnHBox;
    }

    public Label getFailureText() {
        return failureText;
    }

    public void setFailureText(Label failureText) {
        this.failureText = failureText;
    }

    public Button getLoginBtn() {
        return loginBtn;
    }

    public void setLoginBtn(Button loginBtn) {
        this.loginBtn = loginBtn;
    }

    @Override
    public void assignChildren() {
        setRoot(new VBox());
        getRoot().setSpacing(10);
        getRoot().setPadding(new Insets(15));

        setGridPane(new GridPane());
        getGridPane().setHgap(15);
        getGridPane().setVgap(10);
        getRoot().getChildren().add(getGridPane());

        ColumnConstraints col1Constraint = new ColumnConstraints();
        col1Constraint.setHgrow(Priority.SOMETIMES);
        getGridPane().getColumnConstraints().add(col1Constraint);

        setNameLabel(new Label());
        getNameLabel().setText("User Name");
        getGridPane().add(getNameLabel(), 0, 0);

        setNameSelect(new ComboBox());
        getNameSelect().setPromptText("Select name");
        getNameSelect().setMaxWidth(Double.POSITIVE_INFINITY);
        getGridPane().add(getNameSelect(), 1, 0);

        setPasswordLabel(new Label());
        getPasswordLabel().setText("Password");
        getGridPane().add(getPasswordLabel(), 0, 1);

        setPasswordField(new PasswordField());
        getPasswordField().setPromptText("Enter your password");
        getGridPane().add(getPasswordField(), 1, 1);

        setLoginBtnHBox(new HBox());
        getLoginBtnHBox().setAlignment(Pos.CENTER_RIGHT);
        getLoginBtnHBox().setSpacing(8);
        getRoot().getChildren().add(getLoginBtnHBox());

        setFailureText(new Label());
        getFailureText().setText("Failed to log in.");
        getFailureText().setTextFill(Color.RED);
        getFailureText().setVisible(false);
        getLoginBtnHBox().getChildren().add(getFailureText());

        setLoginBtn(new Button());
        getLoginBtn().setText("Log In");
        getLoginBtn().setDefaultButton(true);
        getLoginBtn().setDisable(true);
        getLoginBtn().setPrefWidth(90);
        getLoginBtnHBox().getChildren().add(getLoginBtn());
    }

}
