package views;

import controllers.RegisterController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by Shane on 18/03/2017.
 */
public class RegisterView extends View<RegisterController> {

    private LoginRegisterView loginRegisterView;
    private VBox root;
    private GridPane gridPane;
    private Label nameLabel;
    private TextField nameField;
    private Label passwordLabel;
    private PasswordField passwordField;
    private HBox registerBtnHBox;
    private Label failureText;
    private Button registerBtn;

    public RegisterView(LoginRegisterView loginRegisterView) {
        this(new Stage(), loginRegisterView);
    }

    public RegisterView(Stage window, LoginRegisterView loginRegisterView) {
        super(window);
        setLoginRegisterView(loginRegisterView);
        assignChildren();
        assignController(new RegisterController(this));
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

    public TextField getNameField() {
        return nameField;
    }

    public void setNameField(TextField nameField) {
        this.nameField = nameField;
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

    public HBox getRegisterBtnHBox() {
        return registerBtnHBox;
    }

    public void setRegisterBtnHBox(HBox registerBtnHBox) {
        this.registerBtnHBox = registerBtnHBox;
    }

    public Label getFailureText() {
        return failureText;
    }

    public void setFailureText(Label failureText) {
        this.failureText = failureText;
    }

    public Button getRegisterBtn() {
        return registerBtn;
    }

    public void setRegisterBtn(Button registerBtn) {
        this.registerBtn = registerBtn;
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

        setNameField(new TextField());
        getNameField().setPromptText("Enter your full name");
        getGridPane().add(getNameField(), 1, 0);

        setPasswordLabel(new Label());
        getPasswordLabel().setText("Password");
        getGridPane().add(getPasswordLabel(), 0, 1);

        setPasswordField(new PasswordField());
        getPasswordField().setPromptText("Enter your password");
        getGridPane().add(getPasswordField(), 1, 1);

        setRegisterBtnHBox(new HBox());
        getRegisterBtnHBox().setAlignment(Pos.CENTER_RIGHT);
        getRegisterBtnHBox().setSpacing(8);
        getRoot().getChildren().add(getRegisterBtnHBox());

        setFailureText(new Label());
        getFailureText().setText("Registration failed.");
        getFailureText().setTextFill(Color.RED);
        getFailureText().setVisible(false);
        getRegisterBtnHBox().getChildren().add(getFailureText());

        setRegisterBtn(new Button());
        getRegisterBtn().setText("Register");
        getRegisterBtn().setDefaultButton(true);
        getRegisterBtn().setDisable(true);
        getRegisterBtn().setPrefWidth(90);
        getRegisterBtnHBox().getChildren().add(getRegisterBtn());
    }

}
