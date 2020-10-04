package controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import managers.InvoiceManager;
import managers.SessionManager;
import models.GlobalConstants;
import models.Invoice;
import models.Patient;
import models.Payment;
import views.*;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * Created by Shane on 19/03/2017.
 */
public class PaymentListController extends Controller<PaymentListView> {

    private static PaymentListController instance;

    public PaymentListController(PaymentListView view) {
        super(view);
        setInstance(this);
        for (Payment payment : getView().getInvoiceManagementView().getInvoice().getPaymentsUnmodifiable()) {
            showPayment(payment);
        }
        getView().getAddPaymentBtn().setOnAction(this::handleAddPayment);
    }

    public static PaymentListController getInstance() {
        return instance;
    }

    public static void setInstance(PaymentListController instance) {
        PaymentListController.instance = instance;
    }

    public void handleAddPayment(ActionEvent actionEvent) {
        new AddPaymentView(getView().getInvoiceManagementView()).getWindow().show();
    }

    public Node showPayment(Payment payment) {
        PaymentListItemView newView = new PaymentListItemView(payment);
        newView.getPaymentDateField().textProperty().bind(Bindings.createStringBinding(() ->
                GlobalConstants.getDateFormat().format(payment.dateProperty().get()), payment.dateProperty())
        );
        newView.getPaymentCostField().textProperty().bind(payment.amountProperty().asString("%.2f"));
        getView().getPaymentsVBox().getChildren().add(newView.getRoot());
        return newView.getRoot();
    }

    public void hidePaymentView(PaymentListItemView paymentListItemView) {
        getView().getPaymentsVBox().getChildren().remove(paymentListItemView.getRoot());
    }
    
}
