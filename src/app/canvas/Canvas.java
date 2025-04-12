package app.canvas;

import app.fsm.FiniteStateMachine;
import javafx.scene.layout.Pane;

public class Canvas extends Pane {
    // the component currently being held in the mouse
    static Component current = null;

    public Canvas(FiniteStateMachine<String, String> backing) {

    }
}
