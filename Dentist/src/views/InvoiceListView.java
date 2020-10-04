package views;

import controllers.InvoiceListController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Patient;

/**
 * Created by Shane on 19/03/2017.
 */
public class InvoiceListView extends View<InvoiceListController> {

    private PatientManagementView patientManagementView;
    private Patient patient;
    private VBox root;
    private VBox innerRoot;
    private ScrollPane scrollPane;
    private VBox invoicesVBox;
    private HBox innerButtonsHBox;
    private Button addInvoiceBtn;
    
    public InvoiceListView(PatientManagementView patientManagementView, Patient patient) {
        this(new Stage(), patientManagementView, patient);
    }

    public InvoiceListView(Stage window, PatientManagementView patientManagementView, Patient patient) {
        super(window);
        setPatientManagementView(patientManagementView);
        setPatient(patient);
        assignChildren();
        assignController(new InvoiceListController(this));
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

    public VBox getInnerRoot() {
        return innerRoot;
    }

    public void setInnerRoot(VBox innerRoot) {
        this.innerRoot = innerRoot;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public VBox getInvoicesVBox() {
        return invoicesVBox;
    }

    public void setInvoicesVBox(VBox invoicesVBox) {
        this.invoicesVBox = invoicesVBox;
    }

    public HBox getInnerButtonsHBox() {
        return innerButtonsHBox;
    }

    public void setInnerButtonsHBox(HBox innerButtonsHBox) {
        this.innerButtonsHBox = innerButtonsHBox;
    }

    public Button getAddInvoiceBtn() {
        return addInvoiceBtn;
    }

    public void setAddInvoiceBtn(Button addInvoiceBtn) {
        this.addInvoiceBtn = addInvoiceBtn;
    }

    @Override
    public void assignChildren() {
        setRoot(new VBox());
        getRoot().setPrefHeight(400);
        getRoot().setPrefWidth(600);

        setInnerRoot(new VBox());
        getInnerRoot().setPadding(new Insets(15));
        getInnerRoot().setSpacing(10);
        VBox.setVgrow(getInnerRoot(), Priority.ALWAYS);
        getRoot().getChildren().add(getInnerRoot());

        setScrollPane(new ScrollPane());
        getScrollPane().setFitToWidth(true);
        VBox.setVgrow(getScrollPane(), Priority.ALWAYS);
        getInnerRoot().getChildren().add(getScrollPane());

        setInvoicesVBox(new VBox());
        getInvoicesVBox().setSpacing(10);
        getInvoicesVBox().setPadding(new Insets(15));
        getScrollPane().setContent(getInvoicesVBox());

        setInnerButtonsHBox(new HBox());
        getInnerButtonsHBox().setAlignment(Pos.CENTER_RIGHT);
        getInnerButtonsHBox().setSpacing(8);
        getInnerRoot().getChildren().add(getInnerButtonsHBox());

        setAddInvoiceBtn(new Button());
        getAddInvoiceBtn().setText("Add Invoice");
        getInnerButtonsHBox().getChildren().add(getAddInvoiceBtn());
    }
}
