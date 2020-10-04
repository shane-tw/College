package controllers;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import managers.GlobalProcedureManager;
import models.GlobalProcedure;
import models.Procedure;
import views.AddGlobalProcedureView;
import views.AddProcedureView;
import views.Controller;

/**
 * Created by Shane on 20/03/2017.
 */
public class AddGlobalProcedureController extends Controller<AddGlobalProcedureView> {

    public AddGlobalProcedureController(AddGlobalProcedureView view) {
        super(view);
        getView().getProcedureNameField().textProperty().addListener(this::onFieldValueChanged);
        getView().getProcedureCostField().textProperty().addListener(this::onFieldValueChanged);
        getView().getAddProcedureBtn().setOnAction(this::handleAddProcedure);
    }

    public void onFieldValueChanged(ObservableValue observable, String oldValue, String newValue) {
        boolean costValid = true;
        try {
            Double val = Double.parseDouble(getView().getProcedureCostField().getText());
            costValid = (val >= 0);
        } catch (NumberFormatException e) {
            costValid = false;
        }
        if (costValid && !getView().getProcedureNameField().getText().isEmpty()) {
            getView().getAddProcedureBtn().setDisable(false);
        } else {
            getView().getAddProcedureBtn().setDisable(true);
        }
    }

    public void handleAddProcedure(ActionEvent actionEvent) {
        GlobalProcedure globalProcedure = new GlobalProcedure();
        globalProcedure.setName(getView().getProcedureNameField().getText());
        globalProcedure.setCost(Double.parseDouble(getView().getProcedureCostField().getText()));
        GlobalProcedureManager.addGlobalProcedure(globalProcedure);
        getView().getWindow().close();
    }

}
