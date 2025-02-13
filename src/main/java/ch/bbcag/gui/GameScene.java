package ch.bbcag.gui;

import ch.bbcag.common.DataObject;
import ch.bbcag.common.SceneNavigator;
import ch.bbcag.game.Grid;
import ch.bbcag.gameobjects.ScreenRenderer;
import ch.bbcag.gameobjects.SnakeRenderer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class GameScene extends SceneExtension{
    private final SceneNavigator sceneNavigator;

    private final Grid gameGrid;
    private final SnakeRenderer snake;
    private final ScreenRenderer screenRenderer;

    public GameScene(SceneNavigator sceneNavigator) {
        super(new BorderPane());

        this.sceneNavigator = sceneNavigator;

        HashMap<DataObject.GameSettingType, Double> gameSettings = sceneNavigator.getDataObject().getGameSettings();

        sceneNavigator.getDataObject().getCanvas().setHeight(gameSettings.get(DataObject.GameSettingType.TILE_SIZE)*gameSettings.get(DataObject.GameSettingType.GRID_HEIGHT));
        sceneNavigator.getDataObject().getCanvas().setWidth(gameSettings.get(DataObject.GameSettingType.TILE_SIZE)*gameSettings.get(DataObject.GameSettingType.GRID_WIDTH));

        BorderPane root = (BorderPane) getRoot();
        root.setCenter(sceneNavigator.getDataObject().getCanvas());

        sceneNavigator.getPrimaryStage().sizeToScene();

        gameGrid = new Grid(gameSettings.get(DataObject.GameSettingType.GRID_WIDTH).intValue(), gameSettings.get(DataObject.GameSettingType.GRID_HEIGHT).intValue(), gameSettings.get(DataObject.GameSettingType.SNAKE_LENGTH).intValue(), gameSettings.get(DataObject.GameSettingType.FRUIT_CHANCE), gameSettings.get(DataObject.GameSettingType.FRUIT_MAX).intValue(), gameSettings.get(DataObject.GameSettingType.EDGE_WRAP) > 0, gameSettings.get(DataObject.GameSettingType.INVINCIBILITY) > 0);
        snake = new SnakeRenderer(sceneNavigator, gameGrid);
        sceneNavigator.getSceneController().getGameObjects().add(snake);
        screenRenderer = new ScreenRenderer(gameGrid, sceneNavigator.getDataObject().getGameSettings().get(DataObject.GameSettingType.TILE_SIZE));
        sceneNavigator.getSceneController().getGameObjects().add(screenRenderer);

        this.setOnKeyPressed((e) -> {
            sceneNavigator.getDataObject().getKeysPressed().add(e.getCode());
            snake.handlePressedKeys(e.getCode());
        });
        this.setOnKeyReleased((e) -> {
            sceneNavigator.getDataObject().getKeysPressed().remove(e.getCode());
        });
    }

    @Override
    public void update(double deltaInSec) {
        if (gameGrid.getSnakeLength() == gameGrid.getGRID_HEIGHT() * gameGrid.getGRID_WIDTH() && !(gameGrid.getStatus() == Grid.SnakeStatus.QUIT)) {
            gameGrid.setStatus(Grid.SnakeStatus.WON);
        }
        if (gameGrid.getStatus() == Grid.SnakeStatus.QUIT) {
            sceneNavigator.switchScene(SceneNavigator.Scenes.TITLE);
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Define the size of each square
        int squareSize = sceneNavigator.getDataObject().getGameSettings().get(DataObject.GameSettingType.TILE_SIZE).intValue();

        // Define custom hex colors
        String color1Hex = "#9fff6a"; // Example hex color 1
        String color2Hex = "#77ff2d"; // Example hex color 2
        // Draw the checkerboard pattern
        for (int col = 0; col < sceneNavigator.getDataObject().getCanvas().getHeight()/squareSize; col++) {
            for (int row = 0; row < sceneNavigator.getDataObject().getCanvas().getWidth()/squareSize; row++) {
                // Determine the color based on the row and column
                if ((col + row) % 2 == 0) {
                    gc.setFill(Color.web(color1Hex)); // Use custom hex color 1
                } else {
                    gc.setFill(Color.web(color2Hex)); // Use custom hex color 2
                }
                // Draw the square
                gc.fillRect(row * squareSize, col * squareSize, squareSize, squareSize);
            }
        }
    }
}
