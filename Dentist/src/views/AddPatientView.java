package views;

import controllers.AddPatientController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Shane on 19/03/2017.
 */
public class AddPatientView extends View<AddPatientController> {

    private GridPane root;
    private Label patientNameLabel;
    private TextField patientNameField;
    private Label patientAddressLabel;
    private TextField patientAddressField;
    private Label patientContactNumberLabel;
    private TextField patientContactNumberField;
    private GridPane buttonsGridPane;
    private Button cancelBtn;
    private Button addPatientBtn;

    public AddPatientView() {
        this(new Stage());
    }

    public AddPatientView(Stage window) {
        super(window);
        assignChildren();
        assignController(new AddPatientController(this)); // The controller when instantiated will add event handlers to the view's nodes.
    }

    @Override
    public GridPane getRoot() {
        return root;
    }

    public void setRoot(GridPane root) {
        this.root = root;
    }

    public Label getPatientNameLabel() {
        return patientNameLabel;
    }

    public void setPatientNameLabel(Label patientNameLabel) {
        this.patientNameLabel = patientNameLabel;
    }

    public TextField getPatientNameField() {
        return patientNameField;
    }

    public void setPatientNameField(TextField patientNameField) {
        this.patientNameField = patientNameField;
    }

    public Label getPatientAddressLabel() {
        return patientAddressLabel;
    }

    public void setPatientAddressLabel(Label patientAddressLabel) {
        this.patientAddressLabel = patientAddressLabel;
    }

    public TextField getPatientAddressField() {
        return patientAddressField;
    }

    public void setPatientAddressField(TextField patientAddressField) {
        this.patientAddressField = patientAddressField;
    }

    public Label getPatientContactNumberLabel() {
        return patientContactNumberLabel;
    }

    public void setPatientContactNumberLabel(Label patientContactNumberLabel) {
        this.patientContactNumberLabel = patientContactNumberLabel;
    }

    public TextField getPatientContactNumberField() {
        return patientContactNumberField;
    }

    public void setPatientContactNumberField(TextField patientContactNumberField) {
        this.patientContactNumberField = patientContactNumberField;
    }

    public GridPane getButtonsGridPane() {
        return buttonsGridPane;
    }

    public void setButtonsGridPane(GridPane buttonsGridPane) {
        this.buttonsGridPane = buttonsGridPane;
    }

    public Button getCancelBtn() {
        return cancelBtn;
    }

    public void setCancelBtn(Button cancelBtn) {
        this.cancelBtn = cancelBtn;
    }

    public Button getAddPatientBtn() {
        return addPatientBtn;
    }

    public void setAddPatientBtn(Button addPatientBtn) {
        this.addPatientBtn = addPatientBtn;
    }

    @Override
    public void assignChildren() {
        setRoot(new GridPane());
        getRoot().setHgap(15);
        getRoot().setVgap(10);
        getRoot().setPadding(new Insets(15));

        ColumnConstraints rootColumnConstraint = new ColumnConstraints();
        rootColumnConstraint.setHgrow(Priority.SOMETIMES);
        getRoot().getColumnConstraints().addAll(rootColumnConstraint, rootColumnConstraint);

        RowConstraints rootRowConstraint = new RowConstraints();
        rootRowConstraint.setVgrow(Priority.SOMETIMES);
        getRoot().getRowConstraints().addAll(rootRowConstraint, rootRowConstraint, rootRowConstraint, rootRowConstraint);

        setPatientNameLabel(new Label());
        getPatientNameLabel().setText("Patient Name");
        getRoot().add(getPatientNameLabel(), 0, 0);

        setPatientNameField(new TextField());
        getRoot().add(getPatientNameField(), 1, 0);

        setPatientAddressLabel(new Label());
        getPatientAddressLabel().setText("Address");
        getRoot().add(getPatientAddressLabel(), 0, 1);

        setPatientAddressField(new TextField());
        getRoot().add(getPatientAddressField(), 1, 1);

        setPatientContactNumberLabel(new Label());
        getPatientContactNumberLabel().setText("Contact Number");
        getRoot().add(getPatientContactNumberLabel(), 0, 2);

        setPatientContactNumberField(new TextField());
        getRoot().add(getPatientContactNumberField(), 1, 2);

        setButtonsGridPane(new GridPane());
        getButtonsGridPane().setHgap(8);
        getRoot().add(getButtonsGridPane(), 1, 3);

        ColumnConstraints buttonsColumnConstraint = new ColumnConstraints();
        buttonsColumnConstraint.setHgrow(Priority.SOMETIMES);
        buttonsColumnConstraint.setPrefWidth(100);
        getButtonsGridPane().getColumnConstraints().addAll(buttonsColumnConstraint, buttonsColumnConstraint);

        RowConstraints buttonsRowConstraint = new RowConstraints();
        buttonsRowConstraint.setVgrow(Priority.SOMETIMES);
        getButtonsGridPane().getRowConstraints().addAll(buttonsRowConstraint);

        setCancelBtn(new Button());
        getCancelBtn().setText("Cancel");
        getCancelBtn().setCancelButton(true);
        getCancelBtn().setMaxWidth(Double.POSITIVE_INFINITY);
        getButtonsGridPane().add(getCancelBtn(), 0, 0);

        setAddPatientBtn(new Button());
        getAddPatientBtn().setText("Add Patient");
        getAddPatientBtn().setDefaultButton(true);
        getAddPatientBtn().setDisable(true);
        getAddPatientBtn().setMaxWidth(Double.POSITIVE_INFINITY);
        getButtonsGridPane().add(getAddPatientBtn(), 1, 0);

        getWindow().setTitle("Add Patient - Dentistry");
        getWindow().setScene(new Scene(root));
        getWindow().setResizable(false);
        getWindow().initModality(Modality.APPLICATION_MODAL);
        getWindow().sizeToScene(); // Known JavaFX bug workaround. Required because setting resizable to false results in the window being larger than it should be.
    }

}
