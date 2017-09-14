package controllers;

import javafx.event.ActionEvent;
import views.Controller;
import views.EditProcedureView;
import views.ProcedureListItemView;

/**
 * Created by Shane on 13/02/2017.
 */
public class ProcedureListItemController extends Controller<ProcedureListItemView> {

    public ProcedureListItemController(ProcedureListItemView view) {
        super(view);
        getView().getEditProcedureBtn().setOnAction(this::handleEditProcedure);
        getView().getDeleteProcedureBtn().setOnAction(this::handleDeleteProcedure);
    }

    public void handleEditProcedure(ActionEvent actionEvent) {
        new EditProcedureView(getView().getProcedure()).getWindow().show();
    }

    public void handleDeleteProcedure(ActionEvent actionEvent) {
        ProcedureListController.getInstance().getView().getInvoiceManagementView().getInvoice().removeProcedure(getView());
    }

}
