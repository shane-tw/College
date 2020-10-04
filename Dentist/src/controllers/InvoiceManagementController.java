package controllers;

import javafx.event.ActionEvent;
import views.Controller;
import views.InvoiceManagementView;

/**
 * Created by Shane on 20/03/2017.
 */
public class InvoiceManagementController extends Controller<InvoiceManagementView> {

    public InvoiceManagementController(InvoiceManagementView view) {
        super(view);
        getView().getDoneBtn().setOnAction(this::handleDone);
    }

    public void handleDone(ActionEvent actionEvent) {
        getView().getWindow().close();
    }

}