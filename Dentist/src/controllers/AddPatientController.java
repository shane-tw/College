package controllers;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import managers.SessionManager;
import models.Patient;
import views.AddPatientView;
import views.Controller;

/**
 * Created by Shane on 10/02/2017.
 */
public class AddPatientController extends Controller<AddPatientView> {

    public AddPatientController(AddPatientView view) {
        super(view);
        getView().getPatientNameField().textProperty().addListener(this::onFieldValueChanged); // See onFieldValueChanged for explanation.
        getView().getPatientAddressField().textProperty().addListener(this::onFieldValueChanged);
        getView().getPatientContactNumberField().textProperty().addListener(this::onFieldValueChanged);
        getView().getCancelBtn().setOnAction(this::onCancelAction);
        getView().getAddPatientBtn().setOnAction(this::handleAddPatient);
    }

    public void onFieldValueChanged(ObservableValue observable, String oldValue, String newValue) { // Ensures all 3 fields must be non-empty before the submit button is enabled.
        getView().getAddPatientBtn().setDisable(getView().getPatientNameField().getText().isEmpty() || getView().getPatientAddressField().getText().isEmpty() || getView().getPatientContactNumberField().getText().isEmpty());
    }

    public void onCancelAction(ActionEvent actionEvent) {
        getView().getWindow().close();
    }

    public void handleAddPatient(ActionEvent actionEvent) { // Called by add patient button. Creates patient from field values and adds it to the logged in dentist's list of patients. Then closes window.
        Patient patient = new Patient(getView().getPatientNameField().getText(), getView().getPatientAddressField().getText());
        patient.setContactNumber(getView().getPatientContactNumberField().getText());
        SessionManager.getLoggedInDentist().getPatientManager().addPatient(patient);
        getView().getWindow().close();
    }
}
