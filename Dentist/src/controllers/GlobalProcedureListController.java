package controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import managers.GlobalProcedureManager;
import models.GlobalProcedure;
import views.*;

/**
 * Created by Shane on 20/03/2017.
 */
public class GlobalProcedureListController extends Controller<GlobalProcedureListView> {

    private static GlobalProcedureListController instance;

    public GlobalProcedureListController(GlobalProcedureListView view) {
        super(view);
        setInstance(this);
        for (GlobalProcedure procedure : GlobalProcedureManager.getGlobalProceduresUnmodifiable()) {
            showGlobalProcedure(procedure);
        }
        getView().getAddProcedureBtn().setOnAction(this::handleAddGlobalProcedure);
    }

    public static GlobalProcedureListController getInstance() {
        return instance;
    }

    public static void setInstance(GlobalProcedureListController instance) {
        GlobalProcedureListController.instance = instance;
    }

    public void handleAddGlobalProcedure(ActionEvent actionEvent) {
        new AddGlobalProcedureView(getView().getGeneralManagementView()).getWindow().show();
    }

    public Node showGlobalProcedure(GlobalProcedure procedure) {
        GlobalProcedureListItemView newView = new GlobalProcedureListItemView(procedure);
        newView.getProcedureNameField().textProperty().bind(procedure.nameProperty());
        newView.getProcedureCostField().textProperty().bind(procedure.costProperty().asString("%.2f"));
        getView().getProceduresVBox().getChildren().add(newView.getRoot());
        return newView.getRoot();
    }

    public void hideGlobalProcedureView(GlobalProcedureListItemView globalProcedureListItemView) {
        getView().getProceduresVBox().getChildren().remove(globalProcedureListItemView.getRoot());
    }

}
