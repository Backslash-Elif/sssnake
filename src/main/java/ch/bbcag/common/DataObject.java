package ch.bbcag.common;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DataObject {
    public DataObject() {
    }

    private GraphicsContext graphicsContext = null;
    private Canvas canvas = null;
    private final Set<KeyCode> keysPressed = new HashSet<>();

    public enum GameSettingType {
        GRID_WIDTH, GRID_HEIGHT, TILE_SIZE, SNAKE_LENGTH, FRUIT_CHANCE, FRUIT_MAX, EDGE_WRAP, INVINCIBILITY;
    }

    private final HashMap<GameSettingType, Double> gameSettings = new HashMap<GameSettingType, Double>();

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public Set<KeyCode> getKeysPressed() {
        return keysPressed;
    }

    public HashMap<GameSettingType, Double> getGameSettings() {
        return gameSettings;
    }
}
