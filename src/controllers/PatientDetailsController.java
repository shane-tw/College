package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import views.Controller;
import views.PatientDetailsView;

/**
 * Created by Shane on 25/03/2017.
 */
public class PatientDetailsController extends Controller<PatientDetailsView> {

    public PatientDetailsController(PatientDetailsView view) {
        super(view);
        getView().getPatientNameField().setText(getView().getPatient().getName());
        getView().getPatientAddressField().setText(getView().getPatient().getAddress());
        getView().getPatientContactNumberField().setText(getView().getPatient().getContactNumber());

        getView().getPatientNameField().textProperty().addListener(this::onNameChanged);
        getView().getPatientAddressField().textProperty().addListener(this::onAddressChanged);
        getView().getPatientContactNumberField().textProperty().addListener(this::onContactNumberChanged);
    }

    public void onNameChanged(ObservableValue observable, String oldValue, String newValue) {
        getView().getPatient().setName(newValue);
    }

    public void onAddressChanged(ObservableValue observable, String oldValue, String newValue) {
        getView().getPatient().setAddress(newValue);
    }

    public void onContactNumberChanged(ObservableValue observable, String oldValue, String newValue) {
        getView().getPatient().setContactNumber(newValue);
    }

}
