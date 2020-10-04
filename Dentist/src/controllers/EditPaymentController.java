package controllers;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import models.GlobalConstants;
import models.Payment;
import views.EditPaymentView;
import views.Controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Shane on 20/03/2017.
 */
public class EditPaymentController extends Controller<EditPaymentView> {

    public EditPaymentController(EditPaymentView view) {
        super(view);
        getView().getPaymentDateField().textProperty().addListener(this::onFieldValueChanged);
        getView().getPaymentCostField().textProperty().addListener(this::onFieldValueChanged);
        getView().getEditPaymentBtn().setOnAction(this::handleEditPayment);
        getView().getPaymentDateField().setText(GlobalConstants.getDateFormat().format(getView().getPayment().getDate()));
        getView().getPaymentCostField().setText(String.format("%.2f", getView().getPayment().getAmount()));
    }

    public void onFieldValueChanged(ObservableValue observable, String oldValue, String newValue) {
        try {
            GlobalConstants.getDateFormat().parse(getView().getPaymentDateField().getText());
            Double val = Double.parseDouble(getView().getPaymentCostField().getText());
            getView().getEditPaymentBtn().setDisable(val < 0);
        } catch (ParseException | NumberFormatException e) {
            getView().getEditPaymentBtn().setDisable(true);
        }
    }

    public void handleEditPayment(ActionEvent actionEvent) {
        try {
            getView().getPayment().setDate(GlobalConstants.getDateFormat().parse(getView().getPaymentDateField().getText()));
        } catch (ParseException e) {
            e.printStackTrace(); // This should be physically impossible to ever happen.
        }
        getView().getPayment().setAmount(Double.parseDouble(getView().getPaymentCostField().getText()));
        getView().getWindow().close();
    }

}
