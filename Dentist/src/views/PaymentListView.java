package views;

import controllers.PaymentListController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Shane on 19/03/2017.
 */
public class PaymentListView extends View<PaymentListController> {

    private InvoiceManagementView invoiceManagementView;
    private VBox root;
    private ScrollPane scrollPane;
    private VBox paymentsVBox;
    private HBox buttonsHBox;
    private Button addPaymentBtn;

    public PaymentListView(InvoiceManagementView invoiceManagementView) {
        this(new Stage(), invoiceManagementView);
    }

    public PaymentListView(Stage window, InvoiceManagementView invoiceManagementView) {
        super(window);
        setInvoiceManagementView(invoiceManagementView);
        assignChildren();
        assignController(new PaymentListController(this));
    }

    public InvoiceManagementView getInvoiceManagementView() {
        return invoiceManagementView;
    }

    public void setInvoiceManagementView(InvoiceManagementView invoiceManagementView) {
        this.invoiceManagementView = invoiceManagementView;
    }

    @Override
    public VBox getRoot() {
        return root;
    }

    public void setRoot(VBox root) {
        this.root = root;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public VBox getPaymentsVBox() {
        return paymentsVBox;
    }

    public void setPaymentsVBox(VBox paymentsVBox) {
        this.paymentsVBox = paymentsVBox;
    }

    public HBox getButtonsHBox() {
        return buttonsHBox;
    }

    public void setButtonsHBox(HBox buttonsHBox) {
        this.buttonsHBox = buttonsHBox;
    }

    public Button getAddPaymentBtn() {
        return addPaymentBtn;
    }

    public void setAddPaymentBtn(Button addPaymentBtn) {
        this.addPaymentBtn = addPaymentBtn;
    }

    @Override
    public void assignChildren() {
        setRoot(new VBox());
        getRoot().setMaxHeight(Double.POSITIVE_INFINITY);
        getRoot().setSpacing(10);
        getRoot().setPadding(new Insets(15));

        setScrollPane(new ScrollPane());
        getScrollPane().setFitToHeight(true);
        getScrollPane().setFitToWidth(true);
        VBox.setVgrow(getScrollPane(), Priority.ALWAYS);
        getRoot().getChildren().add(getScrollPane());

        setPaymentsVBox(new VBox());
        getPaymentsVBox().setSpacing(10);
        getPaymentsVBox().setPadding(new Insets(15));
        getScrollPane().setContent(getPaymentsVBox());

        setButtonsHBox(new HBox());
        getButtonsHBox().setAlignment(Pos.TOP_RIGHT);
        getRoot().getChildren().add(getButtonsHBox());

        setAddPaymentBtn(new Button());
        getAddPaymentBtn().setText("Add Payment");
        getAddPaymentBtn().setPrefWidth(120);
        getButtonsHBox().getChildren().add(getAddPaymentBtn());
    }
}
