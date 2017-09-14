package controllers;

import javafx.event.ActionEvent;
import managers.InvoiceManager;
import managers.SessionManager;
import models.Invoice;
import models.Patient;
import views.Controller;
import views.InvoiceListItemView;
import views.InvoiceManagementView;

import java.util.List;

/**
 * Created by Shane on 16/02/2017.
 */
public class InvoiceListItemController extends Controller<InvoiceListItemView> {

    public InvoiceListItemController(InvoiceListItemView view) {
        super(view);
        getView().getEditInvoiceBtn().setOnAction(this::handleEditInvoice);
        getView().getDeleteInvoiceBtn().setOnAction(this::handleDeleteInvoice);
    }

    public void handleEditInvoice(ActionEvent actionEvent) {
        Invoice invoice = InvoiceManager.getInvoiceByRoot(getView().getRoot());
        new InvoiceManagementView(invoice).getWindow().show();
    }

    public void handleDeleteInvoice(ActionEvent actionEvent) {
        List<Patient> patientList = SessionManager.getLoggedInDentist().getPatientManager().getPatientsUnmodifiable();
        Invoice invoice = InvoiceManager.getInvoiceByRoot(getView().getRoot());
        for (Patient patient : patientList) {
            if (patient.getInvoiceManager().removeInvoice(invoice)) {
                return;
            }
        }
    }

}
