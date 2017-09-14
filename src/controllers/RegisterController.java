package controllers;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import managers.DentistManager;
import managers.SessionManager;
import models.Dentist;
import views.*;

/**
 * Created by Shane on 11/02/2017.
 */
public class RegisterController extends Controller<RegisterView> {

    public RegisterController(RegisterView view) {
        super(view);
        getView().getRegisterBtn().setOnAction(this::handleRegister);
        getView().getRegisterBtn().defaultButtonProperty().bind(getView().getLoginRegisterView().getRegisterTab().selectedProperty());
        getView().getNameField().textProperty().addListener(this::onFieldValueChanged);
        getView().getPasswordField().textProperty().addListener(this::onFieldValueChanged);
    }

    public void handleRegister(ActionEvent actionEvent) {
        getView().getRegisterBtn().setDisable(true);
        Dentist selectedDentist = DentistManager.findDentistByName(getView().getNameField().getText());
        if (selectedDentist == null) {
            Dentist dentist = new Dentist();
            dentist.setName(getView().getNameField().getText());
            dentist.setPassword(getView().getPasswordField().getText());
            DentistManager.addDentist(dentist);
            SessionManager.setLoggedInDentist(dentist);
            new GeneralManagementView().getWindow().show();
            getView().getLoginRegisterView().getWindow().close();
        } else if (selectedDentist.getPassword().equals(getView().getPasswordField().getText())) {
            showError("You've registered already.");
        } else {
            showError("Name already taken.");
        }
    }

    public void showError(String errorMessage) {
        getView().getFailureText().setText(errorMessage);
        getView().getFailureText().setVisible(true);
    }

    public void hideError() {
        getView().getFailureText().setVisible(false);
    }

    public void onFieldValueChanged(ObservableValue observable, String oldValue, String newValue) {
        getView().getRegisterBtn().setDisable(getView().getNameField().getText().isEmpty() || getView().getPasswordField().getText().isEmpty());
        hideError();
    }

}
