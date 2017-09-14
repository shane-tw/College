package views;

/**
 * Created by Shane on 18/03/2017.
 */
public class Controller<V extends View> { // Generics used to ensure view matches controller. Controller must also match view.

    private V view;

    public Controller(V view) {
        setView(view);
    } // The view instantiates the controller by passing the view's instance to it.

    public void setView(V view) {
        this.view = view;
        if (getView().getController() != this) {
            getView().assignController(this);
        }
    }

    public V getView() {
        return view;
    } // Get the view's instance.

}
