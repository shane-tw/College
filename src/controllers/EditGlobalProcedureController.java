package controllers;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import views.EditGlobalProcedureView;
import views.Controller;

/**
 * Created by Shane on 20/03/2017.
 */
public class EditGlobalProcedureController extends Controller<EditGlobalProcedureView> {

    public EditGlobalProcedureController(EditGlobalProcedureView view) {
        super(view);
        getView().getProcedureNameField().textProperty().addListener(this::onFieldValueChanged);
        getView().getProcedureCostField().textProperty().addListener(this::onFieldValueChanged);
        getView().getEditProcedureBtn().setOnAction(this::handleEditProcedure);
        getView().getProcedureNameField().setText(getView().getGlobalProcedure().getName());
        getView().getProcedureCostField().setText(String.format("%.2f", getView().getGlobalProcedure().getCost()));
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
            getView().getEditProcedureBtn().setDisable(false);
        } else {
            getView().getEditProcedureBtn().setDisable(true);
        }
    }

    public void handleEditProcedure(ActionEvent actionEvent) {
        getView().getGlobalProcedure().setName(getView().getProcedureNameField().getText());
        getView().getGlobalProcedure().setCost(Double.parseDouble(getView().getProcedureCostField().getText()));
        getView().getWindow().close();
    }

}
