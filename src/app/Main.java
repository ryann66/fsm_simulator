package app;

import app.canvas.Canvas;
import app.canvas.ComponentTray;
import app.simluator.StepControls;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String PROG_NAME = "Finite State Machine Simulator";

    private enum SceneElement {
        MENU_BAR, CANVAS, COMPONENT_TRAY, STEP_CONTROLS
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // create menu bar
        // file
        Menu menuFile = new Menu("File");
        MenuItem itemNewMoore = new MenuItem("New Moore FSM");
        // todo: add button onclick handler
        menuFile.getItems().add(itemNewMoore);
        MenuItem itemNewMealy = new MenuItem("New Mealy FSM");
        // todo: add button onclick handler
        menuFile.getItems().add(itemNewMealy);
        MenuItem itemOpen = new MenuItem("Open");
        // todo: add button onclick handler
        menuFile.getItems().add(itemOpen);
        MenuItem itemSave = new MenuItem("Save");
        // todo: add button onclick handler
        menuFile.getItems().add(itemSave);
        MenuItem itemSaveAs = new MenuItem("Save As");
        // todo: add button onclick handler
        menuFile.getItems().add(itemSaveAs);
        MenuItem itemPrint = new MenuItem("Print");
        // todo: add button onclick handler
        menuFile.getItems().add(itemPrint);
        // edit
        Menu menuEdit = new Menu("Edit");
        MenuItem itemCut = new MenuItem("Cut");
        // todo: add button onclick handler
        menuEdit.getItems().add(itemCut);
        MenuItem itemCopy = new MenuItem("Copy");
        // todo: add button onclick handler
        menuEdit.getItems().add(itemCopy);
        MenuItem itemPaste = new MenuItem("Paste");
        // todo: add button onclick handler
        menuEdit.getItems().add(itemPaste);
        MenuItem itemDelete = new MenuItem("Delete");
        // todo: add button onclick handler
        menuEdit.getItems().add(itemDelete);
        MenuItem itemAutoBalance = new MenuItem("Auto Balance");
        // todo: add button onclick handler
        menuEdit.getItems().add(itemAutoBalance);
        // view
        Menu menuView = new Menu("View");
        MenuItem itemComponentTray = new MenuItem("Component Tray");
        // todo: add button onclick handler
        menuView.getItems().add(itemComponentTray);
        MenuItem itemStepControls = new MenuItem("Step Controls");
        // todo: add button onclick handler
        menuView.getItems().add(itemStepControls);
        MenuItem itemStateList = new MenuItem("State List");
        // todo: add button onclick handler
        menuView.getItems().add(itemStateList);
        MenuItem itemStateHighlighting = new MenuItem("State Highlighting");
        // todo: add button onclick handler
        menuView.getItems().add(itemStateHighlighting);
        MenuBar menuBar = new MenuBar(menuFile, menuEdit, menuView);

        Canvas canvas = new Canvas();
        ComponentTray componentTray = new ComponentTray();
        StepControls stepControls = new StepControls();

        // set position, dimension of top level scenes


        // assign nodes to scene
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(canvas);
        root.setLeft(componentTray);
        root.setRight(stepControls);

        // assign scene to stage
        stage.setScene(new Scene(root, 500, 500));
        stage.setTitle(PROG_NAME);
        stage.show();
    }
}