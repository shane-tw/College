package controllers;

import javafx.event.ActionEvent;
import managers.SessionManager;
import views.Controller;
import views.GeneralManagementView;

/**
 * Created by Shane on 26/03/2017.
 */
public class GeneralManagementController extends Controller<GeneralManagementView> {

    public GeneralManagementController(GeneralManagementView view) {
        super(view);
        getView().getQuitBtn().setOnAction(this::handleQuit);
        getView().getSaveBtn().setOnAction(this::handleSave);
    }

    public void handleQuit(ActionEvent actionEvent) {
        getView().getWindow().close();
    }

    public void handleSave(ActionEvent actionEvent) {
        SessionManager.save();
        getView().getWindow().close();
    }

}
