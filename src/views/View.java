package views;

import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Created by Shane on 18/03/2017.
 */
public abstract class View<C extends Controller> { // Generics are used to tell the class the type that the Controller is.
// An instance of view is created, the nodes are added to a stage, and then that view instance creates a controller instance and associates it with the view.
// The controller then handles any of the events that occur, eg. button clicks.
    private C controller;
    private Stage window; // The stage/window that the view is shown on.

    public C getController() {
        return controller;
    }

    public void assignController(C controller) { // Bi-directional relationship. Controller knows view and view knows controller.
        this.controller = controller;
        if (getController().getView() != this) {
            getController().setView(this);
        }
    }

    public Stage getWindow() {
        return window;
    }

    public void setWindow(Stage window) {
        this.window = window;
    }

    public Node lookupNode(String selector) {
        return getWindow().getScene().lookup(selector);
    } // Find node by selector. Currently unused.

    public View() {
        this(new Stage());
    }

    public View(Stage window) {
        setWindow(window);
        // assignChildren();
    }

    public abstract void assignChildren(); // Instantiate the node attributes that the subclass has. Assign children to the root.

    public abstract Node getRoot(); // This is the root element of the view, as its name implies. Will be overridden by subclass.

}
