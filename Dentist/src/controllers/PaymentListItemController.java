package controllers;

import javafx.event.ActionEvent;
import views.Controller;
import views.EditPaymentView;
import views.PaymentListItemView;

/**
 * Created by Shane on 13/02/2017.
 */
public class PaymentListItemController extends Controller<PaymentListItemView> {

    public PaymentListItemController(PaymentListItemView view) {
        super(view);
        getView().getEditPaymentBtn().setOnAction(this::handleEditPayment);
        getView().getDeletePaymentBtn().setOnAction(this::handleDeletePayment);
    }

    public void handleEditPayment(ActionEvent actionEvent) {
        new EditPaymentView(getView().getPayment()).getWindow().show();
    }

    public void handleDeletePayment(ActionEvent actionEvent) {
        PaymentListController.getInstance().getView().getInvoiceManagementView().getInvoice().removePayment(getView());
    }

}
