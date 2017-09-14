package views;

import controllers.AddProcedureController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.GlobalProcedure;

/**
 * Created by Shane on 20/03/2017.
 */
public class AddProcedureView extends View<AddProcedureController> {

    private InvoiceManagementView invoiceManagementView;
    private VBox root;
    private GridPane gridPane;
    private Label procedureDropdownLabel;
    private ComboBox<GlobalProcedure> procedureDropdown;
    private HBox addProcedureBtnHBox;
    private Button addProcedureBtn;

    public AddProcedureView(InvoiceManagementView invoiceManagementView) {
        this(new Stage(), invoiceManagementView);
    }

    public AddProcedureView(Stage view, InvoiceManagementView invoiceManagementView) {
        super(view);
        setInvoiceManagementView(invoiceManagementView);
        assignChildren();
        assignController(new AddProcedureController(this));
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

    public GridPane getGridPane() {
        return gridPane;
    }

    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }

    public Label getProcedureDropdownLabel() {
        return procedureDropdownLabel;
    }

    public void setProcedureDropdownLabel(Label procedureDropdownLabel) {
        this.procedureDropdownLabel = procedureDropdownLabel;
    }

    public ComboBox<GlobalProcedure> getProcedureDropdown() {
        return procedureDropdown;
    }

    public void setProcedureDropdown(ComboBox procedureDropdown) {
        this.procedureDropdown = procedureDropdown;
    }

    public HBox getAddProcedureBtnHBox() {
        return addProcedureBtnHBox;
    }

    public void setAddProcedureBtnHBox(HBox addProcedureBtnHBox) {
        this.addProcedureBtnHBox = addProcedureBtnHBox;
    }

    public Button getAddProcedureBtn() {
        return addProcedureBtn;
    }

    public void setAddProcedureBtn(Button addProcedureBtn) {
        this.addProcedureBtn = addProcedureBtn;
    }

    @Override
    public void assignChildren() {
        setRoot(new VBox());
        getRoot().setPrefWidth(325);
        getRoot().setSpacing(10);
        getRoot().setPadding(new Insets(15));

        setGridPane(new GridPane());
        getGridPane().setHgap(15);
        getGridPane().setVgap(10);
        getRoot().getChildren().add(getGridPane());

        ColumnConstraints columnConstraint1 = new ColumnConstraints();
        ColumnConstraints columnConstraint2 = new ColumnConstraints();
        columnConstraint2.setHgrow(Priority.SOMETIMES);
        columnConstraint2.setPrefWidth(100);
        getGridPane().getColumnConstraints().addAll(columnConstraint1, columnConstraint2);

        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setPrefHeight(30);
        rowConstraint.setVgrow(Priority.SOMETIMES);
        getGridPane().getRowConstraints().addAll(rowConstraint);

        setProcedureDropdownLabel(new Label());
        getProcedureDropdownLabel().setText("Procedure Name");
        getGridPane().add(getProcedureDropdownLabel(), 0, 0);

        setProcedureDropdown(new ComboBox());
        getGridPane().add(getProcedureDropdown(), 1, 0);
        getProcedureDropdown().setPrefWidth(Double.MAX_VALUE);

        setAddProcedureBtnHBox(new HBox());
        getAddProcedureBtnHBox().setAlignment(Pos.TOP_RIGHT);
        getRoot().getChildren().add(getAddProcedureBtnHBox());

        setAddProcedureBtn(new Button());
        getAddProcedureBtn().setText("Add Procedure");
        getAddProcedureBtn().setDefaultButton(true);
        getAddProcedureBtn().setDisable(true);
        getAddProcedureBtn().setPrefHeight(31);
        getAddProcedureBtnHBox().getChildren().add(getAddProcedureBtn());

        getWindow().setTitle("Add Procedure - Dentistry");
        getWindow().setScene(new Scene(root));
        getWindow().setResizable(false);
        getWindow().initModality(Modality.APPLICATION_MODAL);
        getWindow().sizeToScene();
    }
}
