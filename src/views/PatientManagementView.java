package views;

import controllers.PatientManagementController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Patient;

/**
 * Created by Shane on 19/03/2017.
 */
public class PatientManagementView extends View<PatientManagementController> {

    private Patient patient;
    private VBox root;
    private TabPane tabPane;
    private Tab patientDetailsTab;
    private Tab invoicesTab;
    private Separator separator;
    private HBox buttonsHBox;
    private Button doneBtn;

    public PatientManagementView(Patient patient) {
        this(new Stage(), patient);
    }

    public PatientManagementView(Stage window, Patient patient) {
        super(window);
        setPatient(patient);
        assignChildren();
        assignController(new PatientManagementController(this));
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

    public TabPane getTabPane() {
        return tabPane;
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public Tab getPatientDetailsTab() {
        return patientDetailsTab;
    }

    public void setPatientDetailsTab(Tab patientDetailsTab) {
        this.patientDetailsTab = patientDetailsTab;
    }

    public Tab getInvoicesTab() {
        return invoicesTab;
    }

    public void setInvoicesTab(Tab invoicesTab) {
        this.invoicesTab = invoicesTab;
    }

    public Separator getSeparator() {
        return separator;
    }

    public void setSeparator(Separator separator) {
        this.separator = separator;
    }

    public HBox getButtonsHBox() {
        return buttonsHBox;
    }

    public void setButtonsHBox(HBox buttonsHBox) {
        this.buttonsHBox = buttonsHBox;
    }

    public Button getDoneBtn() {
        return doneBtn;
    }

    public void setDoneBtn(Button doneBtn) {
        this.doneBtn = doneBtn;
    }

    @Override
    public void assignChildren() {
        setRoot(new VBox());
        getRoot().setPrefHeight(400);
        getRoot().setPrefWidth(600);

        setTabPane(new TabPane());
        getTabPane().setPrefWidth(Double.POSITIVE_INFINITY);
        getTabPane().setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        VBox.setVgrow(getTabPane(), Priority.ALWAYS);
        getRoot().getChildren().add(getTabPane());

        setPatientDetailsTab(new Tab());
        getPatientDetailsTab().setText("Patient Details");
        getPatientDetailsTab().setContent(new PatientDetailsView(this, getPatient()).getRoot());
        getTabPane().getTabs().add(getPatientDetailsTab());

        setInvoicesTab(new Tab());
        getInvoicesTab().setText("Manage Invoices");
        getInvoicesTab().setContent(new InvoiceListView(this, getPatient()).getRoot());
        getTabPane().getTabs().add(getInvoicesTab());

        setSeparator(new Separator());
        getSeparator().setPrefWidth(200);
        getRoot().getChildren().add(getSeparator());

        setButtonsHBox(new HBox());
        getButtonsHBox().setAlignment(Pos.TOP_RIGHT);
        getButtonsHBox().setSpacing(8);
        getButtonsHBox().setPadding(new Insets(8));
        getRoot().getChildren().add(getButtonsHBox());

        setDoneBtn(new Button());
        getDoneBtn().setText("Done");
        getDoneBtn().setCancelButton(true);
        getDoneBtn().setPrefWidth(75);
        getButtonsHBox().getChildren().add(getDoneBtn());

        getWindow().setTitle("Edit Patient - Dentistry");
        getWindow().setScene(new Scene(root));
        getWindow().setResizable(false);
        getWindow().initModality(Modality.APPLICATION_MODAL);
        getWindow().sizeToScene();
    }

}
