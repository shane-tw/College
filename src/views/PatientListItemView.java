package views;

import controllers.PatientListItemController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Patient;

/**
 * Created by Shane on 19/03/2017.
 */
public class PatientListItemView extends View<PatientListItemController> {

    private PatientListView patientListView;
    private Patient patient;
    private VBox root;
    private GridPane gridPane;
    private Label patientNameLabel;
    private Label patientNameField;
    private Label patientAddressLabel;
    private Label patientAddressField;
    private Label patientContactNumberLabel;
    private Label patientContactNumberField;
    private HBox buttonsHBox;
    private Button editPatientBtn;
    private Button deletePatientBtn;
    private Separator separator;

    public PatientListItemView(Patient patient) {
        this(new Stage(), patient);
    }

    public PatientListItemView(Stage window, Patient patient) {
        super(window);
        setPatient(patient);
        assignChildren();
        assignController(new PatientListItemController(this, patient));
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
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

    public Label getPatientNameLabel() {
        return patientNameLabel;
    }

    public void setPatientNameLabel(Label patientNameLabel) {
        this.patientNameLabel = patientNameLabel;
    }

    public Label getPatientNameField() {
        return patientNameField;
    }

    public void setPatientNameField(Label patientNameField) {
        this.patientNameField = patientNameField;
    }

    public Label getPatientAddressLabel() {
        return patientAddressLabel;
    }

    public void setPatientAddressLabel(Label patientAddressLabel) {
        this.patientAddressLabel = patientAddressLabel;
    }

    public Label getPatientAddressField() {
        return patientAddressField;
    }

    public void setPatientAddressField(Label patientAddressField) {
        this.patientAddressField = patientAddressField;
    }

    public Label getPatientContactNumberLabel() {
        return patientContactNumberLabel;
    }

    public void setPatientContactNumberLabel(Label patientContactNumberLabel) {
        this.patientContactNumberLabel = patientContactNumberLabel;
    }

    public Label getPatientContactNumberField() {
        return patientContactNumberField;
    }

    public void setPatientContactNumberField(Label patientContactNumberField) {
        this.patientContactNumberField = patientContactNumberField;
    }

    public HBox getButtonsHBox() {
        return buttonsHBox;
    }

    public void setButtonsHBox(HBox buttonsHBox) {
        this.buttonsHBox = buttonsHBox;
    }

    public Button getEditPatientBtn() {
        return editPatientBtn;
    }

    public void setEditPatientBtn(Button editPatientBtn) {
        this.editPatientBtn = editPatientBtn;
    }

    public Button getDeletePatientBtn() {
        return deletePatientBtn;
    }

    public void setDeletePatientBtn(Button deletePatientBtn) {
        this.deletePatientBtn = deletePatientBtn;
    }

    public Separator getSeparator() {
        return separator;
    }

    public void setSeparator(Separator separator) {
        this.separator = separator;
    }

    @Override
    public void assignChildren() {
        setRoot(new VBox());
        getRoot().setSpacing(10);

        setGridPane(new GridPane());
        getGridPane().setHgap(20);
        getRoot().getChildren().add(getGridPane());

        ColumnConstraints columnConstraint1 = new ColumnConstraints();
        getGridPane().getColumnConstraints().add(columnConstraint1);

        ColumnConstraints columnConstraint2 = new ColumnConstraints();
        columnConstraint2.setPrefWidth(205);
        getGridPane().getColumnConstraints().add(columnConstraint2);

        ColumnConstraints columnConstraint3 = new ColumnConstraints();
        columnConstraint3.setHgrow(Priority.SOMETIMES);
        getGridPane().getColumnConstraints().add(columnConstraint3);

        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setPrefHeight(30);
        rowConstraint.setVgrow(Priority.SOMETIMES);
        getGridPane().getRowConstraints().addAll(rowConstraint, rowConstraint, rowConstraint);

        setPatientNameLabel(new Label());
        getPatientNameLabel().setText("Name");
        getGridPane().add(getPatientNameLabel(), 0, 0);

        setPatientNameField(new Label());
        getPatientNameField().setTextFill(Color.web("#909090"));
        getGridPane().add(getPatientNameField(), 1, 0);

        setPatientAddressLabel(new Label());
        getPatientAddressLabel().setText("Address");
        getGridPane().add(getPatientAddressLabel(), 0, 1);

        setPatientAddressField(new Label());
        getPatientAddressField().setTextFill(Color.web("#909090"));
        getGridPane().add(getPatientAddressField(), 1, 1);

        setPatientContactNumberLabel(new Label());
        getPatientContactNumberLabel().setText("Contact Number");
        getGridPane().add(getPatientContactNumberLabel(), 0, 2);

        setPatientContactNumberField(new Label());
        getPatientContactNumberField().setTextFill(Color.web("#909090"));
        getGridPane().add(getPatientContactNumberField(), 1, 2);

        setButtonsHBox(new HBox());
        getButtonsHBox().setAlignment(Pos.TOP_RIGHT);
        getButtonsHBox().setSpacing(8);
        getGridPane().add(getButtonsHBox(), 2, 0);

        setEditPatientBtn(new Button());
        getEditPatientBtn().setText("Edit");
        getEditPatientBtn().setPrefWidth(78);
        getButtonsHBox().getChildren().add(getEditPatientBtn());

        setDeletePatientBtn(new Button());
        getDeletePatientBtn().setText("Delete");
        getDeletePatientBtn().setPrefWidth(78);
        getButtonsHBox().getChildren().add(getDeletePatientBtn());

        setSeparator(new Separator());
        getRoot().getChildren().add(getSeparator());
    }

}
