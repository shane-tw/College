package views;

import controllers.InvoiceManagementController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Invoice;

/**
 * Created by Shane on 19/03/2017.
 */
public class InvoiceManagementView extends View<InvoiceManagementController> {

    private PaymentListView paymentListView;
    private ProcedureListView procedureListView;
    private Invoice invoice;
    private VBox root;
    private TabPane tabPane;
    private Tab proceduresTab;
    private Tab paymentsTab;
    private Separator separator;
    private HBox doneBtnHbox;
    private Button doneBtn;

    public InvoiceManagementView(Invoice invoice) {
        this(new Stage(), invoice);
    }

    public InvoiceManagementView(Stage window, Invoice invoice) {
        super(window);
        setInvoice(invoice);
        assignChildren();
        assignController(new InvoiceManagementController(this));
    }

    public PaymentListView getPaymentListView() {
        return paymentListView;
    }

    public void setPaymentListView(PaymentListView paymentListView) {
        this.paymentListView = paymentListView;
    }

    public ProcedureListView getProcedureListView() {
        return procedureListView;
    }

    public void setProcedureListView(ProcedureListView procedureListView) {
        this.procedureListView = procedureListView;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public VBox getRoot() {
        return root;
    }

    public void setRoot(VBox root) {
        this.root = root;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public Tab getProceduresTab() {
        return proceduresTab;
    }

    public void setProceduresTab(Tab proceduresTab) {
        this.proceduresTab = proceduresTab;
    }

    public Tab getPaymentsTab() {
        return paymentsTab;
    }

    public void setPaymentsTab(Tab paymentsTab) {
        this.paymentsTab = paymentsTab;
    }

    public Separator getSeparator() {
        return separator;
    }

    public void setSeparator(Separator separator) {
        this.separator = separator;
    }

    public HBox getDoneBtnHbox() {
        return doneBtnHbox;
    }

    public void setDoneBtnHbox(HBox doneBtnHbox) {
        this.doneBtnHbox = doneBtnHbox;
    }

    public Button getDoneBtn() {
        return doneBtn;
    }

    public void setDoneBtn(Button doneBtn) {
        this.doneBtn = doneBtn;
    }

    @Override
    public void assignChildren() {
        setRoot(new VBox());
        getRoot().setPrefHeight(400);
        getRoot().setPrefWidth(600);

        setTabPane(new TabPane());
        getTabPane().setPrefWidth(Double.POSITIVE_INFINITY);
        getTabPane().setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        VBox.setVgrow(getTabPane(), Priority.ALWAYS);
        getRoot().getChildren().add(getTabPane());

        setProceduresTab(new Tab());
        getProceduresTab().setText("Manage Procedures");
        setProcedureListView(new ProcedureListView(this));
        getProceduresTab().setContent(getProcedureListView().getRoot());
        getTabPane().getTabs().add(getProceduresTab());

        setPaymentsTab(new Tab());
        getPaymentsTab().setText("Manage Payments");
        setPaymentListView(new PaymentListView(this));
        getPaymentsTab().setContent(getPaymentListView().getRoot());
        getTabPane().getTabs().add(getPaymentsTab());

        setSeparator(new Separator());
        getRoot().getChildren().add(getSeparator());

        setDoneBtnHbox(new HBox());
        getDoneBtnHbox().setAlignment(Pos.TOP_RIGHT);
        getDoneBtnHbox().setSpacing(8);
        getDoneBtnHbox().setPadding(new Insets(8));
        getRoot().getChildren().add(getDoneBtnHbox());

        setDoneBtn(new Button());
        getDoneBtn().setText("Done");
        getDoneBtn().setPrefHeight(31);
        getDoneBtn().setPrefWidth(67);
        getDoneBtnHbox().getChildren().add(getDoneBtn());

        getWindow().setTitle("Edit Invoice - Dentistry");
        getWindow().setScene(new Scene(root));
        getWindow().setResizable(false);
        getWindow().initModality(Modality.APPLICATION_MODAL);
        getWindow().sizeToScene();
    }
}
