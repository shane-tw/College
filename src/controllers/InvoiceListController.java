package controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import managers.InvoiceManager;
import managers.SessionManager;
import models.GlobalConstants;
import models.Invoice;
import models.Patient;
import views.*;

/**
 * Created by Shane on 13/02/2017.
 */
public class InvoiceListController extends Controller<InvoiceListView> {

    private static InvoiceListController instance;

    public InvoiceListController(InvoiceListView view) {
        super(view);
        setInstance(this);

        for (Invoice invoice : getView().getPatient().getInvoiceManager().getInvoicesUnmodifiable()) {
            Node root = showInvoice(invoice);
            InvoiceManager.addInvoiceAssociation(invoice, root);
        }

        getView().getAddInvoiceBtn().setOnAction(this::handleAddInvoice);
    }

    public static InvoiceListController getInstance() {
        return instance;
    }

    private static void setInstance(InvoiceListController instance) {
        InvoiceListController.instance = instance;
    }

    public Node showInvoice(Invoice invoice) {
        InvoiceListItemView newView = new InvoiceListItemView();
        newView.getInvoiceDateField().textProperty().bind(Bindings.createStringBinding(() -> // Create string binding from callable.
            GlobalConstants.getDateFormat().format(invoice.getDate()) // Date shown will change when invoice date changes.
        ));
        getView().getInvoicesVBox().getChildren().add(newView.getRoot());
        return newView.getRoot();
    }

    public void hideInvoice(Invoice invoice) {
        Node root = InvoiceManager.getRootByInvoice(invoice);
        getView().getInvoicesVBox().getChildren().remove(root);
    }

    public void handleAddInvoice(ActionEvent actionEvent) {
        new AddInvoiceView(getView()).getWindow().show();
    }

}
