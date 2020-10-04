package controllers;

import managers.DentistManager;
import views.Controller;
import views.LoginRegisterView;

public class LoginRegisterController extends Controller<LoginRegisterView> {

    public LoginRegisterController(LoginRegisterView view) {
        super(view);
        if (DentistManager.getDentistsUnmodifiable().isEmpty()) {
            getView().getRoot().getSelectionModel().select(getView().getRegisterTab());
            getView().getLoginTab().setDisable(true);
        }
    }

}
