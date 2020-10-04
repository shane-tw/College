package controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import managers.PatientManager;
import managers.SessionManager;
import models.Patient;
import views.AddPatientView;
import views.Controller;
import views.PatientListItemView;
import views.PatientListView;

/**
 * Created by Shane on 11/02/2017.
 */
public class PatientListController extends Controller<PatientListView> {

    private static PatientListController instance;

    public PatientListController(PatientListView view) {
        super(view);
        setInstance(this);
        for (Patient patient : SessionManager.getLoggedInDentist().getPatientManager().getPatientsUnmodifiable()) {
            showPatient(patient);
        }
        getView().getAddPatientBtn().setOnAction(this::handleAddPatient);
    }

    public static PatientListController getInstance() {
        return instance;
    }

    public static void setInstance(PatientListController instance) {
        PatientListController.instance = instance;
    }

    public void showPatient(Patient patient) {
        PatientListItemView newView = new PatientListItemView(patient);
        newView.getPatientNameField().textProperty().bind(patient.nameProperty());
        newView.getPatientAddressField().textProperty().bind(patient.addressProperty());
        newView.getPatientContactNumberField().textProperty().bind(patient.contactNumberProperty());
        getView().getPatientsVBox().getChildren().add(newView.getRoot());
    }

    public void hidePatientView(PatientListItemView patientListItemView) {
        getView().getPatientsVBox().getChildren().remove(patientListItemView.getRoot());
    }

    public void handleAddPatient(ActionEvent actionEvent) {
        new AddPatientView().getWindow().show();
    }

}
