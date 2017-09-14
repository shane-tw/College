package views;

import controllers.PatientListController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Shane on 19/03/2017.
 */
public class PatientListView extends View<PatientListController> {

    private GeneralManagementView generalManagementView;
    private VBox root;
    private ScrollPane scrollPane;
    private VBox patientsVBox;
    private HBox innerButtonsHBox;
    private Button addPatientBtn;

    public PatientListView(GeneralManagementView generalManagementView) {
        this(new Stage(), generalManagementView);
    }

    public PatientListView(Stage window, GeneralManagementView generalManagementView) {
        super(window);
        setGeneralManagementView(generalManagementView);
        assignChildren();
        assignController(new PatientListController(this));
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

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public VBox getPatientsVBox() {
        return patientsVBox;
    }

    public void setPatientsVBox(VBox patientsVBox) {
        this.patientsVBox = patientsVBox;
    }

    public HBox getInnerButtonsHBox() {
        return innerButtonsHBox;
    }

    public void setInnerButtonsHBox(HBox innerButtonsHBox) {
        this.innerButtonsHBox = innerButtonsHBox;
    }

    public Button getAddPatientBtn() {
        return addPatientBtn;
    }

    public void setAddPatientBtn(Button addPatientBtn) {
        this.addPatientBtn = addPatientBtn;
    }

    @Override
    public void assignChildren() {
        setRoot(new VBox());
        getRoot().setPadding(new Insets(15));
        getRoot().setSpacing(10);
        VBox.setVgrow(getRoot(), Priority.ALWAYS);

        setScrollPane(new ScrollPane());
        getScrollPane().setFitToHeight(true);
        getScrollPane().setFitToWidth(true);
        VBox.setVgrow(getScrollPane(), Priority.ALWAYS);
        getRoot().getChildren().add(getScrollPane());

        setPatientsVBox(new VBox());
        getPatientsVBox().setSpacing(10);
        getPatientsVBox().setPadding(new Insets(15));
        getScrollPane().setContent(getPatientsVBox());

        setInnerButtonsHBox(new HBox());
        getInnerButtonsHBox().setAlignment(Pos.TOP_RIGHT);
        getInnerButtonsHBox().setSpacing(8);
        getRoot().getChildren().add(getInnerButtonsHBox());

        setAddPatientBtn(new Button());
        getAddPatientBtn().setText("Add Patient");
        getInnerButtonsHBox().getChildren().add(getAddPatientBtn());
    }

}
