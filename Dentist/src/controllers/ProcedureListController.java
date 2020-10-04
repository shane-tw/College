package controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import models.Procedure;
import views.*;

/**
 * Created by Shane on 20/03/2017.
 */
public class ProcedureListController extends Controller<ProcedureListView> {

    private static ProcedureListController instance;

    public ProcedureListController(ProcedureListView view) {
        super(view);
        setInstance(this);
        for (Procedure procedure : getView().getInvoiceManagementView().getInvoice().getProceduresUnmodifiable()) {
            showProcedure(procedure);
        }
        getView().getAddProcedureBtn().setOnAction(this::handleAddProcedure);
    }

    public static ProcedureListController getInstance() {
        return instance;
    }

    public static void setInstance(ProcedureListController instance) {
        ProcedureListController.instance = instance;
    }

    public void handleAddProcedure(ActionEvent actionEvent) {
        new AddProcedureView(getView().getInvoiceManagementView()).getWindow().show();
    }

    public Node showProcedure(Procedure procedure) {
        ProcedureListItemView newView = new ProcedureListItemView(procedure);
        newView.getProcedureNameField().textProperty().bind(procedure.nameProperty());
        newView.getProcedureCostField().textProperty().bind(procedure.costProperty().asString("%.2f"));
        getView().getProceduresVBox().getChildren().add(newView.getRoot());
        return newView.getRoot();
    }

    public void hideProcedureView(ProcedureListItemView procedureListItemView) {
        getView().getProceduresVBox().getChildren().remove(procedureListItemView.getRoot());
    }

}
