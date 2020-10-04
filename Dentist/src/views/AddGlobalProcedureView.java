package views;

import controllers.AddGlobalProcedureController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Shane on 20/03/2017.
 */
public class AddGlobalProcedureView extends View<AddGlobalProcedureController> {

    private GeneralManagementView generalManagementView;
    private VBox root;
    private GridPane gridPane;
    private Label procedureNameLabel;
    private TextField procedureNameField;
    private Label procedureCostLabel;
    private TextField procedureCostField;
    private HBox addProcedureBtnHBox;
    private Button addProcedureBtn;

    public AddGlobalProcedureView(GeneralManagementView generalManagementView) {
        this(new Stage(), generalManagementView);
    }

    public AddGlobalProcedureView(Stage view, GeneralManagementView generalManagementView) {
        super(view);
        setGeneralManagementView(generalManagementView);
        assignChildren();
        assignController(new AddGlobalProcedureController(this));
    }

    public GeneralManagementView getGeneralManagementView() {
        return generalManagementView;
    }

    public void setGeneralManagementView(GeneralManagementView generalManagementView) {
        this.generalManagementView = generalManagementView;
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

    public TextField getProcedureNameField() {
        return procedureNameField;
    }

    public void setProcedureNameField(TextField procedureNameField) {
        this.procedureNameField = procedureNameField;
    }

    public Label getProcedureCostLabel() {
        return procedureCostLabel;
    }

    public void setProcedureCostLabel(Label procedureCostLabel) {
        this.procedureCostLabel = procedureCostLabel;
    }

    public TextField getProcedureCostField() {
        return procedureCostField;
    }

    public void setProcedureCostField(TextField procedureCostField) {
        this.procedureCostField = procedureCostField;
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
        getGridPane().getRowConstraints().addAll(rowConstraint, rowConstraint);

        setProcedureNameLabel(new Label());
        getProcedureNameLabel().setText("Procedure Name");
        getGridPane().add(getProcedureNameLabel(), 0, 0);

        setProcedureNameField(new TextField());
        getGridPane().add(getProcedureNameField(), 1, 0);

        setProcedureCostLabel(new Label());
        getProcedureCostLabel().setText("Cost");
        getGridPane().add(getProcedureCostLabel(), 0, 1);

        setProcedureCostField(new TextField());
        getProcedureCostField().setPromptText("Enter a decimal");
        getGridPane().add(getProcedureCostField(), 1, 1);

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
