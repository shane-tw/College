package views;

import controllers.PatientDetailsController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Patient;

/**
 * Created by Shane on 25/03/2017.
 */
public class PatientDetailsView extends View<PatientDetailsController> {

    private PatientManagementView patientManagementView;
    private Patient patient;
    private VBox root;
    private GridPane gridPane;
    private Label patientNameLabel;
    private TextField patientNameField;
    private Label patientAddressLabel;
    private TextField patientAddressField;
    private Label patientContactNumberLabel;
    private TextField patientContactNumberField;
    private HBox noticeHBox;
    private Label noticeLabel;

    public PatientDetailsView(PatientManagementView patientManagementView, Patient patient) {
        this(new Stage(), patientManagementView, patient);
    }

    public PatientDetailsView(Stage window, PatientManagementView patientManagementView, Patient patient) {
        super(window);
        setPatientManagementView(patientManagementView);
        setPatient(patient);
        assignChildren();
        assignController(new PatientDetailsController(this));
    }

    public PatientManagementView getPatientManagementView() {
        return patientManagementView;
    }

    public void setPatientManagementView(PatientManagementView patientManagementView) {
        this.patientManagementView = patientManagementView;
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

    public HBox getNoticeHBox() {
        return noticeHBox;
    }

    public void setNoticeHBox(HBox noticeHBox) {
        this.noticeHBox = noticeHBox;
    }

    public Label getNoticeLabel() {
        return noticeLabel;
    }

    public void setNoticeLabel(Label noticeLabel) {
        this.noticeLabel = noticeLabel;
    }

    @Override
    public void assignChildren() {
        setRoot(new VBox());
        getRoot().setAlignment(Pos.CENTER);
        getRoot().setSpacing(15);

        setGridPane(new GridPane());
        getGridPane().setAlignment(Pos.CENTER);
        getGridPane().setHgap(10);
        getGridPane().setVgap(15);
        getRoot().getChildren().add(getGridPane());

        setPatientNameLabel(new Label());
        getPatientNameLabel().setText("Name");
        getGridPane().add(getPatientNameLabel(), 0, 0);

        setPatientNameField(new TextField());
        getGridPane().add(getPatientNameField(), 1, 0);

        setPatientAddressLabel(new Label());
        getPatientAddressLabel().setText("Address");
        getGridPane().add(getPatientAddressLabel(), 0, 1);

        setPatientAddressField(new TextField());
        getGridPane().add(getPatientAddressField(), 1, 1);

        setPatientContactNumberLabel(new Label());
        getPatientContactNumberLabel().setText("Contact Number");
        getGridPane().add(getPatientContactNumberLabel(), 0, 2);

        setPatientContactNumberField(new TextField());
        getGridPane().add(getPatientContactNumberField(), 1, 2);

        setNoticeHBox(new HBox());
        getNoticeHBox().setAlignment(Pos.CENTER);
        getRoot().getChildren().add(getNoticeHBox());

        setNoticeLabel(new Label());
        getNoticeLabel().setText("Note: Changes take immediate effect.");
        getNoticeLabel().setUnderline(true);
        getNoticeHBox().getChildren().add(getNoticeLabel());
    }

}
