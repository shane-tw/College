package controllers;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import managers.DentistManager;
import managers.SessionManager;
import models.Dentist;
import views.*;

import java.util.*;

/**
 * Created by Shane on 11/02/2017.
 */
public class LoginController extends Controller<LoginView> {

    public LoginController(LoginView view) {
        super(view);
        DentistManager.setLoginController(this);
        showDentists(DentistManager.getDentistsUnmodifiable());
        getView().getLoginBtn().setOnAction(this::handleLogin);
        getView().getLoginBtn().defaultButtonProperty().bind(getView().getLoginRegisterView().getLoginTab().selectedProperty());
        getView().getNameSelect().valueProperty().addListener(this::onFieldValueChanged);
        getView().getPasswordField().textProperty().addListener(this::onFieldValueChanged);
    }

    public void showDentists(List<Dentist> dentistArrayList) {
        ObservableList<String> dentistNames = FXCollections.observableArrayList();
        for (Dentist dentist : dentistArrayList) {
            dentistNames.add(dentist.getName());
        }
        getView().getNameSelect().itemsProperty().set(dentistNames);
    }

    public void handleLogin(ActionEvent actionEvent) {
        getView().getLoginBtn().setDisable(true);
        Dentist selectedDentist = DentistManager.findDentistByName((String)getView().getNameSelect().getValue());
        if (selectedDentist == null) {
            showError("Dentist doesn't exist.");
        } else if (!selectedDentist.getPassword().equals(getView().getPasswordField().getText())) {
            showError("Invalid password.");
        } else {
            SessionManager.setLoggedInDentist(selectedDentist);
            new GeneralManagementView().getWindow().show();
            getView().getLoginRegisterView().getWindow().close();
        }
    }

    public void showError(String errorMessage) {
        getView().getFailureText().setText(errorMessage);
        getView().getFailureText().setVisible(true);
    }

    public void hideError() {
        getView().getFailureText().setVisible(false);
    }

    public void onFieldValueChanged(ObservableValue observable, Object oldValue, Object newValue) {
        getView().getLoginBtn().setDisable(getView().getNameSelect().getValue() == null || getView().getPasswordField().getText().isEmpty());
        hideError();
    }

}
