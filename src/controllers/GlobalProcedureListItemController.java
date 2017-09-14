package controllers;

import javafx.event.ActionEvent;
import managers.GlobalProcedureManager;
import views.Controller;
import views.EditGlobalProcedureView;
import views.GlobalProcedureListItemView;

/**
 * Created by Shane on 13/02/2017.
 */
public class GlobalProcedureListItemController extends Controller<GlobalProcedureListItemView> {

    public GlobalProcedureListItemController(GlobalProcedureListItemView view) {
        super(view);
        getView().getEditProcedureBtn().setOnAction(this::handleEditProcedure);
        getView().getDeleteProcedureBtn().setOnAction(this::handleDeleteProcedure);
    }

    public void handleEditProcedure(ActionEvent actionEvent) {
        new EditGlobalProcedureView(getView().getProcedure()).getWindow().show();
    }

    public void handleDeleteProcedure(ActionEvent actionEvent) {
        GlobalProcedureManager.removeGlobalProcedureView(getView());
    }

}
