package views;

import controllers.ProcedureListController;
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
public class ProcedureListView extends View<ProcedureListController> {

    private InvoiceManagementView invoiceManagementView;
    private VBox root;
    private ScrollPane scrollPane;
    private VBox proceduresVBox;
    private HBox buttonsHBox;
    private Button addProcedureBtn;

    public ProcedureListView(InvoiceManagementView invoiceManagementView) {
        this(new Stage(), invoiceManagementView);
    }

    public ProcedureListView(Stage window, InvoiceManagementView invoiceManagementView) {
        super(window);
        setInvoiceManagementView(invoiceManagementView);
        assignChildren();
        assignController(new ProcedureListController(this));
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

    public VBox getProceduresVBox() {
        return proceduresVBox;
    }

    public void setProceduresVBox(VBox proceduresVBox) {
        this.proceduresVBox = proceduresVBox;
    }

    public HBox getButtonsHBox() {
        return buttonsHBox;
    }

    public void setButtonsHBox(HBox buttonsHBox) {
        this.buttonsHBox = buttonsHBox;
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
        getRoot().setMaxHeight(Double.POSITIVE_INFINITY);
        getRoot().setSpacing(10);
        getRoot().setPadding(new Insets(15));

        setScrollPane(new ScrollPane());
        getScrollPane().setFitToHeight(true);
        getScrollPane().setFitToWidth(true);
        VBox.setVgrow(getScrollPane(), Priority.ALWAYS);
        getRoot().getChildren().add(getScrollPane());

        setProceduresVBox(new VBox());
        getProceduresVBox().setSpacing(10);
        getProceduresVBox().setPadding(new Insets(15));
        getScrollPane().setContent(getProceduresVBox());

        setButtonsHBox(new HBox());
        getButtonsHBox().setAlignment(Pos.TOP_RIGHT);
        getRoot().getChildren().add(getButtonsHBox());

        setAddProcedureBtn(new Button());
        getAddProcedureBtn().setText("Add Procedure");
        getAddProcedureBtn().setPrefWidth(120);
        getButtonsHBox().getChildren().add(getAddProcedureBtn());
    }
}
