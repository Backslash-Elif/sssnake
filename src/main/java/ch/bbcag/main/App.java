package ch.bbcag.main;

import ch.bbcag.common.DataObject;
import ch.bbcag.common.SceneNavigator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {
    private final DataObject dataObject = new DataObject();
    private GraphicsContext gc;

    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sssnake");
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        dataObject.setGraphicsContext(gc);
        dataObject.setCanvas(canvas);
        SceneNavigator sceneNavigator = new SceneNavigator(primaryStage, SceneNavigator.Scenes.TITLE, dataObject);
        primaryStage.show();
    }
}