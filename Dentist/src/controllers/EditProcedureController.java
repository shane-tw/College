package controllers;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import views.Controller;
import views.EditProcedureView;

/**
 * Created by Shane on 20/03/2017.
 */
public class EditProcedureController extends Controller<EditProcedureView> {

    public EditProcedureController(EditProcedureView view) {
        super(view);
        getView().getProcedureNameField().textProperty().addListener(this::onFieldValueChanged);
        getView().getProcedureCostField().textProperty().addListener(this::onFieldValueChanged);
        getView().getEditProcedureBtn().setOnAction(this::handleEditProcedure);
        getView().getProcedureNameField().setText(getView().getProcedure().getName());
        getView().getProcedureCostField().setText(String.format("%.2f", getView().getProcedure().getCost()));
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
        getView().getProcedure().setName(getView().getProcedureNameField().getText());
        getView().getProcedure().setCost(Double.parseDouble(getView().getProcedureCostField().getText()));
        getView().getWindow().close();
    }

}
