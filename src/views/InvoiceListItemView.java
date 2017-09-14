package views;

import controllers.InvoiceListItemController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by Shane on 19/03/2017.
 */
public class InvoiceListItemView extends View<InvoiceListItemController> {

    private VBox root;
    private GridPane gridPane;
    private Label invoiceDateLabel;
    private Label invoiceDateField;
    private HBox buttonsHBox;
    private Button editInvoiceBtn;
    private Button deleteInvoiceBtn;
    private Separator separator;

    public InvoiceListItemView() {
        this(new Stage());
    }

    public InvoiceListItemView(Stage window) {
        super(window);
        assignChildren();
        assignController(new InvoiceListItemController(this));
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

    public Label getInvoiceDateLabel() {
        return invoiceDateLabel;
    }

    public void setInvoiceDateLabel(Label invoiceDateLabel) {
        this.invoiceDateLabel = invoiceDateLabel;
    }

    public Label getInvoiceDateField() {
        return invoiceDateField;
    }

    public void setInvoiceDateField(Label invoiceDateField) {
        this.invoiceDateField = invoiceDateField;
    }

    public HBox getButtonsHBox() {
        return buttonsHBox;
    }

    public void setButtonsHBox(HBox buttonsHBox) {
        this.buttonsHBox = buttonsHBox;
    }

    public Button getEditInvoiceBtn() {
        return editInvoiceBtn;
    }

    public void setEditInvoiceBtn(Button editInvoiceBtn) {
        this.editInvoiceBtn = editInvoiceBtn;
    }

    public Button getDeleteInvoiceBtn() {
        return deleteInvoiceBtn;
    }

    public void setDeleteInvoiceBtn(Button deleteInvoiceBtn) {
        this.deleteInvoiceBtn = deleteInvoiceBtn;
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
        getRoot().setSpacing(10);

        setGridPane(new GridPane());
        getGridPane().setHgap(20);
        getRoot().getChildren().add(getGridPane());

        ColumnConstraints columnConstraint1 = new ColumnConstraints();
        getGridPane().getColumnConstraints().add(columnConstraint1);

        ColumnConstraints columnConstraint2 = new ColumnConstraints();
        columnConstraint2.setPrefWidth(215);
        getGridPane().getColumnConstraints().add(columnConstraint2);

        ColumnConstraints columnConstraint3 = new ColumnConstraints();
        columnConstraint3.setHgrow(Priority.SOMETIMES);
        getGridPane().getColumnConstraints().add(columnConstraint3);

        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setPrefHeight(30);
        rowConstraint.setVgrow(Priority.SOMETIMES);
        getGridPane().getRowConstraints().add(rowConstraint);

        setInvoiceDateLabel(new Label());
        getInvoiceDateLabel().setText("Date");
        getGridPane().add(getInvoiceDateLabel(), 0, 0);

        setInvoiceDateField(new Label());
        getInvoiceDateField().setTextFill(Color.web("#909090"));
        getGridPane().add(getInvoiceDateField(), 1, 0);

        setButtonsHBox(new HBox());
        getButtonsHBox().setAlignment(Pos.TOP_RIGHT);
        getButtonsHBox().setSpacing(8);
        getGridPane().add(getButtonsHBox(), 2, 0);

        setEditInvoiceBtn(new Button());
        getEditInvoiceBtn().setText("Edit");
        getEditInvoiceBtn().setPrefWidth(78);
        getButtonsHBox().getChildren().add(getEditInvoiceBtn());

        setDeleteInvoiceBtn(new Button());
        getDeleteInvoiceBtn().setText("Delete");
        getDeleteInvoiceBtn().setPrefWidth(78);
        getButtonsHBox().getChildren().add(getDeleteInvoiceBtn());

        setSeparator(new Separator());
        getRoot().getChildren().add(getSeparator());
    }

}
