package views;

import controllers.PatientManagementController;
import controllers.PaymentListItemController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Patient;
import models.Payment;

/**
 * Created by Shane on 21/03/2017.
 */
public class PaymentListItemView extends View<PaymentListItemController> {

    private Payment payment;
    private VBox root;
    private GridPane gridPane;
    private Label paymentDateLabel;
    private Label paymentDateField;
    private Label paymentCostLabel;
    private Label paymentCostField;
    private HBox buttonsHBox;
    private Button editPaymentBtn;
    private Button deletePaymentBtn;
    private Separator separator;

    public PaymentListItemView(Payment payment) {
        this(new Stage(), payment);
    }

    public PaymentListItemView(Stage window, Payment payment) {
        super(window);
        setPayment(payment);
        assignChildren();
        assignController(new PaymentListItemController(this));
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public VBox getRoot() {
        return root;
    }

    public void setRoot(VBox root) {
        this.root = root;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }

    public Label getPaymentDateLabel() {
        return paymentDateLabel;
    }

    public void setPaymentDateLabel(Label paymentDateLabel) {
        this.paymentDateLabel = paymentDateLabel;
    }

    public Label getPaymentDateField() {
        return paymentDateField;
    }

    public void setPaymentDateField(Label paymentDateField) {
        this.paymentDateField = paymentDateField;
    }

    public Label getPaymentCostLabel() {
        return paymentCostLabel;
    }

    public void setPaymentCostLabel(Label paymentCostLabel) {
        this.paymentCostLabel = paymentCostLabel;
    }

    public Label getPaymentCostField() {
        return paymentCostField;
    }

    public void setPaymentCostField(Label paymentCostField) {
        this.paymentCostField = paymentCostField;
    }

    public HBox getButtonsHBox() {
        return buttonsHBox;
    }

    public void setButtonsHBox(HBox buttonsHBox) {
        this.buttonsHBox = buttonsHBox;
    }

    public Button getEditPaymentBtn() {
        return editPaymentBtn;
    }

    public void setEditPaymentBtn(Button editPaymentBtn) {
        this.editPaymentBtn = editPaymentBtn;
    }

    public Button getDeletePaymentBtn() {
        return deletePaymentBtn;
    }

    public void setDeletePaymentBtn(Button deletePaymentBtn) {
        this.deletePaymentBtn = deletePaymentBtn;
    }

    public Separator getSeparator() {
        return separator;
    }

    public void setSeparator(Separator separator) {
        this.separator = separator;
    }

    @Override
    public void assignChildren() {
        setRoot(new VBox());
        getRoot().setPrefHeight(34);
        getRoot().setSpacing(10);

        setGridPane(new GridPane());
        getGridPane().setHgap(20);
        getRoot().getChildren().add(getGridPane());

        ColumnConstraints columnConstraint1 = new ColumnConstraints();
        getGridPane().getColumnConstraints().add(columnConstraint1);

        ColumnConstraints columnConstraint2 = new ColumnConstraints();
        columnConstraint2.setPrefWidth(205);
        getGridPane().getColumnConstraints().add(columnConstraint2);

        ColumnConstraints columnConstraint3 = new ColumnConstraints();
        columnConstraint3.setHgrow(Priority.SOMETIMES);
        getGridPane().getColumnConstraints().add(columnConstraint3);

        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setPrefHeight(30);
        rowConstraint.setVgrow(Priority.SOMETIMES);
        getGridPane().getRowConstraints().addAll(rowConstraint, rowConstraint);

        setPaymentDateLabel(new Label());
        getPaymentDateLabel().setText("Payment Date");
        getGridPane().add(getPaymentDateLabel(), 0, 0);

        setPaymentDateField(new Label());
        getPaymentDateField().setTextFill(Color.web("#909090"));
        getGridPane().add(getPaymentDateField(), 1, 0);

        setPaymentCostLabel(new Label());
        getPaymentCostLabel().setText("Cost");
        getGridPane().add(getPaymentCostLabel(), 0, 1);

        setPaymentCostField(new Label());
        getPaymentCostField().setTextFill(Color.web("#909090"));
        getGridPane().add(getPaymentCostField(), 1, 1);

        setButtonsHBox(new HBox());
        getButtonsHBox().setAlignment(Pos.TOP_RIGHT);
        getButtonsHBox().setSpacing(8);
        getGridPane().add(getButtonsHBox(), 2, 0);

        setEditPaymentBtn(new Button());
        getEditPaymentBtn().setText("Edit");
        getEditPaymentBtn().setPrefWidth(78);
        getButtonsHBox().getChildren().add(getEditPaymentBtn());

        setDeletePaymentBtn(new Button());
        getDeletePaymentBtn().setText("Delete");
        getDeletePaymentBtn().setPrefHeight(31);
        getDeletePaymentBtn().setPrefWidth(78);
        getButtonsHBox().getChildren().add(getDeletePaymentBtn());

        setSeparator(new Separator());
        getSeparator().setPrefWidth(200);
        getRoot().getChildren().add(getSeparator());
    }
    
}
