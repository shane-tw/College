package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.util.StringConverter;
import managers.GlobalProcedureManager;
import models.GlobalProcedure;
import models.Procedure;
import views.AddProcedureView;
import views.Controller;

/**
 * Created by Shane on 20/03/2017.
 */
public class AddProcedureController extends Controller<AddProcedureView> {

    public AddProcedureController(AddProcedureView view) {
        super(view);
        getView().getProcedureDropdown().setItems(FXCollections.observableArrayList(GlobalProcedureManager.getGlobalProceduresUnmodifiable()));
        getView().getProcedureDropdown().setConverter(new StringConverter<GlobalProcedure>(){
            @Override public String toString(GlobalProcedure globalProcedure) {
                return globalProcedure.getName();
            }

            @Override public GlobalProcedure fromString(String string) {
                throw new UnsupportedOperationException("Method fromString should not be used by anything.");
            }

        });
        getView().getProcedureDropdown().getSelectionModel().selectedItemProperty().addListener(this::onProcedureChanged);
        getView().getAddProcedureBtn().setOnAction(this::handleAddProcedure);
    }

    public void handleAddProcedure(ActionEvent actionEvent) {
        GlobalProcedure globalProcedure = getView().getProcedureDropdown().getValue();
        Procedure procedure = new Procedure(getView().getInvoiceManagementView().getInvoice().getId());
        procedure.setName(globalProcedure.getName());
        procedure.setCost(globalProcedure.getCost());
        procedure.setInvoiceId(getView().getInvoiceManagementView().getInvoice().getId());
        ProcedureListController.getInstance().getView().getInvoiceManagementView().getInvoice().addProcedure(procedure);
        getView().getWindow().close();
    }

    public void onProcedureChanged(ObservableValue<? extends GlobalProcedure> observable, GlobalProcedure oldValue, GlobalProcedure newValue) {
        getView().getAddProcedureBtn().setDisable(newValue == null);
    }

}
