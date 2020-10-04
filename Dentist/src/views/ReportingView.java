package views;

import controllers.PatientListController;
import controllers.ReportingController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Created by Shane on 26/03/2017.
 */
public class ReportingView extends View<ReportingController> {

    private GeneralManagementView generalManagementView; // The parent view of this view. One view per tab. Used to access parent node and window.
    private GridPane root;
    private Label reportByNameLabel;
    private Button reportByNameBtn;
    private Label reportByOwedLabel;
    private Button reportByOwedBtn;

    public ReportingView(GeneralManagementView generalManagementView) {
        this(new Stage(), generalManagementView);
    }

    public ReportingView(Stage window, GeneralManagementView generalManagementView) {
        super(window);
        setGeneralManagementView(generalManagementView);
        assignChildren();
        assignController(new ReportingController(this));
    }

    public GeneralManagementView getGeneralManagementView() {
        return generalManagementView;
    }

    public void setGeneralManagementView(GeneralManagementView generalManagementView) {
        this.generalManagementView = generalManagementView;
    }

    @Override
    public GridPane getRoot() {
        return root;
    }

    public void setRoot(GridPane root) {
        this.root = root;
    }

    public Label getReportByNameLabel() {
        return reportByNameLabel;
    }

    public void setReportByNameLabel(Label reportByNameLabel) {
        this.reportByNameLabel = reportByNameLabel;
    }

    public Button getReportByNameBtn() {
        return reportByNameBtn;
    }

    public void setReportByNameBtn(Button reportByNameBtn) {
        this.reportByNameBtn = reportByNameBtn;
    }

    public Label getReportByOwedLabel() {
        return reportByOwedLabel;
    }

    public void setReportByOwedLabel(Label reportByOwedLabel) {
        this.reportByOwedLabel = reportByOwedLabel;
    }

    public Button getReportByOwedBtn() {
        return reportByOwedBtn;
    }

    public void setReportByOwedBtn(Button reportByOwedBtn) {
        this.reportByOwedBtn = reportByOwedBtn;
    }

    @Override
    public void assignChildren() {
        setRoot(new GridPane());
        getRoot().setHgap(10);
        getRoot().setVgap(15);
        getRoot().setPadding(new Insets(15, 40, 15, 40));

        ColumnConstraints columnConstraint1 = new ColumnConstraints();
        columnConstraint1.setHgrow(Priority.ALWAYS);

        ColumnConstraints columnConstraint2 = new ColumnConstraints();
        columnConstraint2.setHgrow(Priority.SOMETIMES);
        columnConstraint2.setMinWidth(Double.NEGATIVE_INFINITY);

        getRoot().getColumnConstraints().addAll(columnConstraint1, columnConstraint2);

        setReportByNameLabel(new Label());
        getReportByNameLabel().setText("Report on all patients sorted by name.");
        getReportByNameLabel().setWrapText(true);
        getRoot().add(getReportByNameLabel(), 0, 0);

        setReportByNameBtn(new Button());
        getReportByNameBtn().setText("Generate Report");
        getRoot().add(getReportByNameBtn(), 1, 0);

        setReportByOwedLabel(new Label());
        getReportByOwedLabel().setText("Report on patients who owe money but have not made a payment in 6 months.");
        getReportByOwedLabel().setWrapText(true);
        getRoot().add(getReportByOwedLabel(), 0, 1);

        setReportByOwedBtn(new Button());
        getReportByOwedBtn().setText("Generate Report");
        getRoot().add(getReportByOwedBtn(), 1, 1);
    }

}
