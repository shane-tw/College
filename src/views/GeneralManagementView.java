package views;

import controllers.GeneralManagementController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Patient;

/**
 * Created by Shane on 19/03/2017.
 */
public class GeneralManagementView extends View<GeneralManagementController> {

    private VBox root;
    private TabPane tabPane;
    private Tab patientsTab;
    private Tab reportingTab;
    private Tab globalProceduresTab;
    private Separator separator;
    private HBox buttonsHBox;
    private Button quitBtn;
    private Button saveBtn;

    public GeneralManagementView() {
        this(new Stage());
    }

    public GeneralManagementView(Stage window) {
        super(window);
        assignChildren();
        assignController(new GeneralManagementController(this));
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

    public Tab getPatientsTab() {
        return patientsTab;
    }

    public void setPatientsTab(Tab patientsTab) {
        this.patientsTab = patientsTab;
    }

    public Tab getReportingTab() {
        return reportingTab;
    }

    public void setReportingTab(Tab reportingTab) {
        this.reportingTab = reportingTab;
    }

    public Tab getGlobalProceduresTab() {
        return globalProceduresTab;
    }

    public void setGlobalProceduresTab(Tab globalProceduresTab) {
        this.globalProceduresTab = globalProceduresTab;
    }

    public Separator getSeparator() {
        return separator;
    }

    public void setSeparator(Separator separator) {
        this.separator = separator;
    }

    public HBox getButtonsHBox() {
        return buttonsHBox;
    }

    public void setButtonsHBox(HBox buttonsHBox) {
        this.buttonsHBox = buttonsHBox;
    }

    public Button getQuitBtn() {
        return quitBtn;
    }

    public void setQuitBtn(Button quitBtn) {
        this.quitBtn = quitBtn;
    }

    public Button getSaveBtn() {
        return saveBtn;
    }

    public void setSaveBtn(Button saveBtn) {
        this.saveBtn = saveBtn;
    }

    @Override
    public void assignChildren() {
        setRoot(new VBox());
        getRoot().setPrefHeight(400);
        getRoot().setPrefWidth(600);

        setTabPane(new TabPane());
        getTabPane().setPrefWidth(Double.POSITIVE_INFINITY);
        getTabPane().setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        VBox.setVgrow(getTabPane(), Priority.ALWAYS);
        getRoot().getChildren().add(getTabPane());

        setPatientsTab(new Tab());
        getPatientsTab().setText("Manage Patients");
        getPatientsTab().setContent(new PatientListView(this).getRoot());
        getTabPane().getTabs().add(getPatientsTab());

        setGlobalProceduresTab(new Tab());
        getGlobalProceduresTab().setText("Global Procedures");
        getGlobalProceduresTab().setContent(new GlobalProcedureListView(this).getRoot());
        getTabPane().getTabs().add(getGlobalProceduresTab());

        setReportingTab(new Tab());
        getReportingTab().setText("Manage Reports");
        getReportingTab().setContent(new ReportingView(this).getRoot());
        getTabPane().getTabs().add(getReportingTab());

        setSeparator(new Separator());
        getSeparator().setPrefWidth(200);
        getRoot().getChildren().add(getSeparator());

        setButtonsHBox(new HBox());
        getButtonsHBox().setAlignment(Pos.TOP_RIGHT);
        getButtonsHBox().setSpacing(8);
        getButtonsHBox().setPadding(new Insets(8));
        getRoot().getChildren().add(getButtonsHBox());

        setQuitBtn(new Button());
        getQuitBtn().setText("Quit Without Saving");
        getQuitBtn().setCancelButton(true);
        getQuitBtn().setPrefWidth(200);
        getButtonsHBox().getChildren().add(getQuitBtn());

        setSaveBtn(new Button());
        getSaveBtn().setText("Save & Quit");
        getSaveBtn().setPrefWidth(200);
        getButtonsHBox().getChildren().add(getSaveBtn());

        getWindow().setTitle("General Management - Dentistry");
        getWindow().setScene(new Scene(root));
        getWindow().setResizable(false);
        getWindow().sizeToScene();
    }

}
