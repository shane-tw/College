package views;

import controllers.PaymentListItemController;
import controllers.ProcedureListItemController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Procedure;

/**
 * Created by Shane on 20/03/2017.
 */
public class ProcedureListItemView extends View<ProcedureListItemController> {

    private Procedure procedure;
    private VBox root;
    private GridPane gridPane;
    private Label procedureNameLabel;
    private Label procedureNameField;
    private Label procedureCostLabel;
    private Label procedureCostField;
    private HBox buttonsHBox;
    private Button editProcedureBtn;
    private Button deleteProcedureBtn;
    private Separator separator;

    public ProcedureListItemView(Procedure procedure) {
        this(new Stage(), procedure);
    }

    public ProcedureListItemView(Stage window, Procedure procedure) {
        super(window);
        setProcedure(procedure);
        assignChildren();
        assignController(new ProcedureListItemController(this));
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
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

    public Label getProcedureNameLabel() {
        return procedureNameLabel;
    }

    public void setProcedureNameLabel(Label procedureNameLabel) {
        this.procedureNameLabel = procedureNameLabel;
    }

    public Label getProcedureNameField() {
        return procedureNameField;
    }

    public void setProcedureNameField(Label procedureNameField) {
        this.procedureNameField = procedureNameField;
    }

    public Label getProcedureCostLabel() {
        return procedureCostLabel;
    }

    public void setProcedureCostLabel(Label procedureCostLabel) {
        this.procedureCostLabel = procedureCostLabel;
    }

    public Label getProcedureCostField() {
        return procedureCostField;
    }

    public void setProcedureCostField(Label procedureCostField) {
        this.procedureCostField = procedureCostField;
    }

    public HBox getButtonsHBox() {
        return buttonsHBox;
    }

    public void setButtonsHBox(HBox buttonsHBox) {
        this.buttonsHBox = buttonsHBox;
    }

    public Button getEditProcedureBtn() {
        return editProcedureBtn;
    }

    public void setEditProcedureBtn(Button editProcedureBtn) {
        this.editProcedureBtn = editProcedureBtn;
    }

    public Button getDeleteProcedureBtn() {
        return deleteProcedureBtn;
    }

    public void setDeleteProcedureBtn(Button deleteProcedureBtn) {
        this.deleteProcedureBtn = deleteProcedureBtn;
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

        setProcedureNameLabel(new Label());
        getProcedureNameLabel().setText("Procedure Name");
        getGridPane().add(getProcedureNameLabel(), 0, 0);

        setProcedureNameField(new Label());
        getProcedureNameField().setTextFill(Color.web("#909090"));
        getGridPane().add(getProcedureNameField(), 1, 0);

        setProcedureCostLabel(new Label());
        getProcedureCostLabel().setText("Cost");
        getGridPane().add(getProcedureCostLabel(), 0, 1);

        setProcedureCostField(new Label());
        getProcedureCostField().setTextFill(Color.web("#909090"));
        getGridPane().add(getProcedureCostField(), 1, 1);

        setButtonsHBox(new HBox());
        getButtonsHBox().setAlignment(Pos.TOP_RIGHT);
        getButtonsHBox().setSpacing(8);
        getGridPane().add(getButtonsHBox(), 2, 0);

        setEditProcedureBtn(new Button());
        getEditProcedureBtn().setText("Edit");
        getEditProcedureBtn().setPrefWidth(78);
        getButtonsHBox().getChildren().add(getEditProcedureBtn());

        setDeleteProcedureBtn(new Button());
        getDeleteProcedureBtn().setText("Delete");
        getDeleteProcedureBtn().setPrefHeight(31);
        getDeleteProcedureBtn().setPrefWidth(78);
        getButtonsHBox().getChildren().add(getDeleteProcedureBtn());

        setSeparator(new Separator());
        getSeparator().setPrefWidth(200);
        getRoot().getChildren().add(getSeparator());
    }
}
