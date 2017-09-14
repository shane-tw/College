package controllers;

import javafx.event.ActionEvent;
import views.Controller;
import views.PatientManagementView;

/**
 * Created by Shane on 22/03/2017.
 */
public class PatientManagementController extends Controller<PatientManagementView> {

    public PatientManagementController(PatientManagementView view) {
        super(view);
        getView().getDoneBtn().setOnAction(this::handleDone);
    }

    public void handleDone(ActionEvent actionEvent) {
        getView().getWindow().close();
    }

}