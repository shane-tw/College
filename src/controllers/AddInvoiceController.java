package controllers;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import managers.SessionManager;
import models.GlobalConstants;
import models.Invoice;
import views.AddInvoiceView;
import views.Controller;
import java.util.Date;

import java.text.ParseException;

/**
 * Created by Shane on 10/02/2017.
 */
public class AddInvoiceController extends Controller<AddInvoiceView> {

    public AddInvoiceController(AddInvoiceView view) {
        super(view);
        getView().getInvoiceDateField().textProperty().addListener(this::onFieldValueChanged); // See onFieldValueChanged for explanation.
        getView().getCancelBtn().setOnAction(this::onCancelAction);
        getView().getAddInvoiceBtn().setOnAction(this::handleAddInvoice);
    }

    public void onFieldValueChanged(ObservableValue observable, String oldValue, String newValue) { // Ensures all 3 fields must be non-empty before the submit button is enabled.
        try {
            GlobalConstants.getDateFormat().parse(getView().getInvoiceDateField().getText()); // Try parse date.
            getView().getAddInvoiceBtn().setDisable(false); // Only enable button if value is non-negative.
        } catch (ParseException e) { // Require a valid number to be inputted.
            getView().getAddInvoiceBtn().setDisable(true); // Keep button disabled if invalid input in a field.
        }
    }

    public void onCancelAction(ActionEvent actionEvent) {
        getView().getWindow().close();
    }

    public void handleAddInvoice(ActionEvent actionEvent) { // Called by add invoice button. Creates invoice from field values and adds it to the logged in dentist's list of invoices. Then closes window.
        Date date = null;
        try {
            date = GlobalConstants.getDateFormat().parse(getView().getInvoiceDateField().getText());
        } catch (ParseException e) {
            e.printStackTrace(); // Guarranteed to never happen. Prior validation elsewhere ensures this.
        }
        Invoice invoice = new Invoice(getView().getInvoiceListView().getPatient(), date);
        getView().getInvoiceListView().getPatient().getInvoiceManager().addInvoice(invoice);
        getView().getWindow().close();
    }
}
