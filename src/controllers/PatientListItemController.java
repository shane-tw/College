package controllers;

import javafx.event.ActionEvent;
import managers.PatientManager;
import managers.SessionManager;
import models.Patient;
import views.Controller;
import views.PatientListItemView;
import views.PatientManagementView;

/**
 * Created by Shane on 13/02/2017.
 */
public class PatientListItemController extends Controller<PatientListItemView> {

    public PatientListItemController(PatientListItemView view, Patient patient) {
        super(view);
        getView().setPatient(patient);
        getView().getEditPatientBtn().setOnAction(this::handleEditPatient);
        getView().getDeletePatientBtn().setOnAction(this::handleDeletePatient);
    }

    public void handleEditPatient(ActionEvent actionEvent) {
        PatientManager patientManager = SessionManager.getLoggedInDentist().getPatientManager();
        new PatientManagementView(getView().getPatient()).getWindow().show();
    }

    public void handleDeletePatient(ActionEvent actionEvent) {
        PatientManager patientManager = SessionManager.getLoggedInDentist().getPatientManager();
        patientManager.removePatient(getView());
    }

}
