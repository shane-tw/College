package controllers;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import jdk.nashorn.internal.objects.Global;
import models.GlobalConstants;
import models.Payment;
import views.AddPaymentView;
import views.Controller;
import views.PaymentListItemView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Shane on 20/03/2017.
 */
public class AddPaymentController extends Controller<AddPaymentView> {

    public AddPaymentController(AddPaymentView view) {
        super(view);
        getView().getPaymentDateField().textProperty().addListener(this::onFieldValueChanged);
        getView().getPaymentCostField().textProperty().addListener(this::onFieldValueChanged);
        getView().getAddPaymentBtn().setOnAction(this::handleAddPayment);
    }

    public void onFieldValueChanged(ObservableValue observable, String oldValue, String newValue) {
        try {
            GlobalConstants.getDateFormat().parse(getView().getPaymentDateField().getText()); // Try parse date.
            Double val = Double.parseDouble(getView().getPaymentCostField().getText()); // Try parse double.
            getView().getAddPaymentBtn().setDisable(val < 0); // Only enable button if value is non-negative.
        } catch (ParseException | NumberFormatException e) { // Require a valid number to be inputted.
            getView().getAddPaymentBtn().setDisable(true); // Keep button disabled if invalid input in a field.
        }
    }

    public void handleAddPayment(ActionEvent actionEvent) { // Add payment using details from fields to invoice associated with view. Close window.
        Payment payment = new Payment();
        payment.setAmount(Double.parseDouble(getView().getPaymentCostField().getText()));
        try {
            payment.setDate(GlobalConstants.getDateFormat().parse(getView().getPaymentDateField().getText()));
        } catch (ParseException e) {
            e.printStackTrace(); // This should be physically impossible to ever happen. Input was already validated.
        }
        PaymentListController.getInstance().getView().getInvoiceManagementView().getInvoice().addPayment(payment);
        getView().getWindow().close();
    }

}
