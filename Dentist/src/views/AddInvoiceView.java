package views;

import controllers.AddInvoiceController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Invoice;

/**
 * Created by Shane on 19/03/2017.
 */
public class AddInvoiceView extends View<AddInvoiceController> {

    private InvoiceListView invoiceListView;
    private GridPane root;
    private Label invoiceDateLabel;
    private TextField invoiceDateField;
    private GridPane buttonsGridPane;
    private Button cancelBtn;
    private Button addInvoiceBtn;

    public AddInvoiceView(InvoiceListView invoiceListView) {
        this(new Stage(), invoiceListView);
    }

    public AddInvoiceView(Stage window, InvoiceListView invoiceListView) {
        super(window);
        setInvoiceListView(invoiceListView);
        assignChildren();
        assignController(new AddInvoiceController(this)); // The controller when instantiated will add event handlers to the view's nodes.
    }

    public InvoiceListView getInvoiceListView() {
        return invoiceListView;
    }

    public void setInvoiceListView(InvoiceListView invoiceListView) {
        this.invoiceListView = invoiceListView;
    }

    @Override
    public GridPane getRoot() {
        return root;
    }

    public void setRoot(GridPane root) {
        this.root = root;
    }

    public Label getInvoiceDateLabel() {
        return invoiceDateLabel;
    }

    public void setInvoiceDateLabel(Label invoiceDateLabel) {
        this.invoiceDateLabel = invoiceDateLabel;
    }

    public TextField getInvoiceDateField() {
        return invoiceDateField;
    }

    public void setInvoiceDateField(TextField invoiceDateField) {
        this.invoiceDateField = invoiceDateField;
    }

    public GridPane getButtonsGridPane() {
        return buttonsGridPane;
    }

    public void setButtonsGridPane(GridPane buttonsGridPane) {
        this.buttonsGridPane = buttonsGridPane;
    }

    public Button getCancelBtn() {
        return cancelBtn;
    }

    public void setCancelBtn(Button cancelBtn) {
        this.cancelBtn = cancelBtn;
    }
    
    public Button getAddInvoiceBtn() {
        return addInvoiceBtn;
    }

    public void setAddInvoiceBtn(Button addInvoiceBtn) {
        this.addInvoiceBtn = addInvoiceBtn;
    }
    
    @Override
    public void assignChildren() {
        setRoot(new GridPane());
        getRoot().setHgap(15);
        getRoot().setVgap(10);
        getRoot().setPadding(new Insets(15));

        ColumnConstraints rootColumnConstraint = new ColumnConstraints();
        rootColumnConstraint.setHgrow(Priority.SOMETIMES);
        getRoot().getColumnConstraints().addAll(rootColumnConstraint, rootColumnConstraint);

        RowConstraints rootRowConstraint = new RowConstraints();
        rootRowConstraint.setVgrow(Priority.SOMETIMES);
        getRoot().getRowConstraints().addAll(rootRowConstraint, rootRowConstraint, rootRowConstraint, rootRowConstraint);

        setInvoiceDateLabel(new Label());
        getInvoiceDateLabel().setText("Invoice Date");
        getRoot().add(getInvoiceDateLabel(), 0, 0);

        setInvoiceDateField(new TextField());
        getRoot().add(getInvoiceDateField(), 1, 0);

        setButtonsGridPane(new GridPane());
        getButtonsGridPane().setHgap(8);
        getRoot().add(getButtonsGridPane(), 1, 3);

        ColumnConstraints buttonsColumnConstraint = new ColumnConstraints();
        buttonsColumnConstraint.setHgrow(Priority.SOMETIMES);
        buttonsColumnConstraint.setPrefWidth(100);
        getButtonsGridPane().getColumnConstraints().addAll(buttonsColumnConstraint, buttonsColumnConstraint);

        RowConstraints buttonsRowConstraint = new RowConstraints();
        buttonsRowConstraint.setVgrow(Priority.SOMETIMES);
        getButtonsGridPane().getRowConstraints().addAll(buttonsRowConstraint);

        setCancelBtn(new Button());
        getCancelBtn().setText("Cancel");
        getCancelBtn().setCancelButton(true);
        getCancelBtn().setMaxWidth(Double.POSITIVE_INFINITY);
        getButtonsGridPane().add(getCancelBtn(), 0, 0);

        setAddInvoiceBtn(new Button());
        getAddInvoiceBtn().setText("Add Invoice");
        getAddInvoiceBtn().setDefaultButton(true);
        getAddInvoiceBtn().setDisable(true);
        getAddInvoiceBtn().setMaxWidth(Double.POSITIVE_INFINITY);
        getButtonsGridPane().add(getAddInvoiceBtn(), 1, 0);

        getWindow().setTitle("Add Invoice - Dentistry");
        getWindow().setScene(new Scene(root));
        getWindow().setResizable(false);
        getWindow().initModality(Modality.APPLICATION_MODAL);
        getWindow().sizeToScene(); // Known JavaFX bug workaround. Required because setting resizable to false results in the window being larger than it should be.
    }

}
