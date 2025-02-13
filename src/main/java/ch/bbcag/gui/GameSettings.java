package ch.bbcag.gui;

import ch.bbcag.common.DataObject;
import ch.bbcag.common.SceneNavigator;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

import static ch.bbcag.gui.ItemCreator.createButton;
import static ch.bbcag.gui.ItemCreator.createLabel;

public class GameSettings {
    private final SceneNavigator sceneNavigator;
    private final TextField gridWidthInput;
    private final TextField gridHeightInput;
    private final TextField tileSizeInput;
    private final TextField snakeLengthInput;
    private final TextField fruitChanceInput;
    private final TextField fruitMaxInput;
    private final CheckBox edgeWrapInput;
    private final CheckBox invincibilityInput;

    private final Label settingsInfoLabel;

    private final VBox settingsVBox;

    public GameSettings(SceneNavigator sceneNavigator) {
        this.sceneNavigator = sceneNavigator;

        Label gridSizeLabel = new Label("Grid size (?)");
        Tooltip.install(gridSizeLabel, new Tooltip("Adjusts size of the playfield. (width(0-64) x height(0-64))"));
        gridWidthInput = new TextField();
        Label gridSizeInputSeperator = new Label("X");
        gridHeightInput = new TextField();
        HBox gridSizeHBox = new HBox(gridWidthInput, gridSizeInputSeperator, gridHeightInput);

        Label tileSizeLabel = new Label("Tile Size (?)");
        Tooltip.install(tileSizeLabel, new Tooltip("Adjusts the visual size of each tile on the playfield. Bigger tiles result in better visibility. (8-256)"));
        tileSizeInput = new TextField();

        Label snakeLengthLabel = new Label("Snake length (?)");
        Tooltip.install(snakeLengthLabel, new Tooltip("Adjusts the initial size of the snake (1-4096)"));
        snakeLengthInput = new TextField();

        Label fruitChanceLabel = new Label("Fruit chance (?)");
        Tooltip.install(fruitChanceLabel, new Tooltip("Fraction of likeliness of a fruit generating per move. (0-1)"));
        fruitChanceInput = new TextField();

        Label fruitMaxLabel = new Label("Max fruit (?)");
        Tooltip.install(fruitMaxLabel, new Tooltip("Adjusts how many fruits are allowed to be on the field maximally (0-4096)"));
        fruitMaxInput = new TextField();

        Label edgeWrapLabel = new Label("Edge-wrap (?)");
        Tooltip.install(edgeWrapLabel, new Tooltip("Sets if snake is allowed to wrap around the field"));
        edgeWrapInput = new CheckBox();

        Label invincibilityLabel = new Label("Invincibility (?)");
        Tooltip.install(invincibilityLabel, new Tooltip("Sets if snake is allowed to go over it's own body"));
        invincibilityInput = new CheckBox();

        TilePane settingsTilePane = new TilePane(gridSizeLabel, gridSizeHBox, tileSizeLabel, tileSizeInput, snakeLengthLabel, snakeLengthInput, fruitChanceLabel, fruitChanceInput, fruitMaxLabel, fruitMaxInput, edgeWrapLabel, edgeWrapInput, invincibilityLabel, invincibilityInput);
        settingsTilePane.setPrefTileWidth(200);
        settingsTilePane.setPrefColumns(2);
        settingsTilePane.setPrefWidth(400);
        settingsTilePane.setAlignment(Pos.CENTER);

        Button loadSettings = createButton("Load", ItemCreator.ButtonType.MEDIUM);
        Label loadPresetLabel = createLabel("Select preset:", ItemCreator.LabelType.TEXT);

        VBox loadVBox = new VBox(loadSettings, loadPresetLabel);
        loadVBox.setAlignment(Pos.CENTER);

        Button loadBeginnerPreset = createButton("Beginner", ItemCreator.ButtonType.SMALL);
        Button loadEasyPreset = createButton("Easy", ItemCreator.ButtonType.SMALL);
        Button loadMediumPreset = createButton("Medium", ItemCreator.ButtonType.SMALL);
        Button loadHardPreset = createButton("Hard", ItemCreator.ButtonType.SMALL);

        HBox loadHBox = new HBox(loadBeginnerPreset, loadEasyPreset, loadMediumPreset, loadHardPreset);
        loadHBox.setAlignment(Pos.CENTER);

        settingsInfoLabel = new Label("Ready.");
        settingsInfoLabel.setAlignment(Pos.CENTER);

        settingsVBox = new VBox(settingsTilePane, loadVBox, loadHBox, settingsInfoLabel);
        settingsVBox.setVisible(false);
        settingsVBox.setManaged(false);

        loadSettings.setOnAction(e->{
            if (validateValues()) {
                saveValues();
                settingsInfoLabel.setText("Settings successfully applied.");
            } else {
                settingsInfoLabel.setText("Error while applying settings, check values.");
            }
        });
        loadBeginnerPreset.setOnAction(e->setPreset(0));
        loadEasyPreset.setOnAction(e->setPreset(1));
        loadMediumPreset.setOnAction(e->setPreset(2));
        loadHardPreset.setOnAction(e->setPreset(3));

        setDefaultSettings();
        loadValues();
    }

    private void setDefaultSettings() {
        HashMap<DataObject.GameSettingType, Double> gameSettings = sceneNavigator.getDataObject().getGameSettings();

        if (gameSettings.isEmpty()) {
            gameSettings.put(DataObject.GameSettingType.GRID_WIDTH, 10d);
            gameSettings.put(DataObject.GameSettingType.GRID_HEIGHT, 10d);
            gameSettings.put(DataObject.GameSettingType.TILE_SIZE, 32d);
            gameSettings.put(DataObject.GameSettingType.SNAKE_LENGTH, 3d);
            gameSettings.put(DataObject.GameSettingType.FRUIT_CHANCE, 1d);
            gameSettings.put(DataObject.GameSettingType.FRUIT_MAX, 1d);
            gameSettings.put(DataObject.GameSettingType.EDGE_WRAP, 1d);
            gameSettings.put(DataObject.GameSettingType.INVINCIBILITY, 0d);
        }
    }

    private void loadValues() {
        HashMap<DataObject.GameSettingType, Double> gameSettings = sceneNavigator.getDataObject().getGameSettings();

        gridWidthInput.setText(String.valueOf(gameSettings.get(DataObject.GameSettingType.GRID_WIDTH)));
        gridHeightInput.setText(String.valueOf(gameSettings.get(DataObject.GameSettingType.GRID_HEIGHT)));
        tileSizeInput.setText(String.valueOf(gameSettings.get(DataObject.GameSettingType.TILE_SIZE)));
        snakeLengthInput.setText(String.valueOf(gameSettings.get(DataObject.GameSettingType.SNAKE_LENGTH)));
        fruitChanceInput.setText(String.valueOf(gameSettings.get(DataObject.GameSettingType.FRUIT_CHANCE)));
        fruitMaxInput.setText(String.valueOf(gameSettings.get(DataObject.GameSettingType.FRUIT_MAX)));
        edgeWrapInput.setSelected(gameSettings.get(DataObject.GameSettingType.EDGE_WRAP) > 0);
        invincibilityInput.setSelected(gameSettings.get(DataObject.GameSettingType.INVINCIBILITY) > 0);
    }

    private boolean validateValues() {
        boolean valid = true;

        // input array
        Object[][] inputs = {
                {gridWidthInput, 3d, 64d},
                {gridHeightInput, 3d, 64d},
                {tileSizeInput, 8d, 256d},
                {snakeLengthInput, 1d, 4096d},
                {fruitChanceInput, 0d, 1d},
                {fruitMaxInput, 1d, 4096d}
        };

        for (Object[] input : inputs) {
            TextField textField = (TextField) input[0];
            double min = (double) input[1];
            double max = (double) input[2];

            try {
                double value = Double.parseDouble(textField.getText());
                if (validateValue(value, min, max)) {
                    textField.setStyle("-fx-text-fill: black;");
                } else {
                    textField.setStyle("-fx-text-fill: red;");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                valid = false;
                textField.setStyle("-fx-text-fill: red;");
            }
        }

        return valid;
    }


    private boolean validateValue(double value, double min, double max) {
        return value <= max && value >= min;
    }

    private void saveValues() {
        HashMap<DataObject.GameSettingType, Double> gameSettings = sceneNavigator.getDataObject().getGameSettings();

        gameSettings.put(DataObject.GameSettingType.GRID_WIDTH, Double.valueOf(gridWidthInput.getText()));
        gameSettings.put(DataObject.GameSettingType.GRID_HEIGHT, Double.valueOf(gridHeightInput.getText()));
        gameSettings.put(DataObject.GameSettingType.TILE_SIZE, Double.valueOf(tileSizeInput.getText()));
        gameSettings.put(DataObject.GameSettingType.SNAKE_LENGTH, Double.valueOf(snakeLengthInput.getText()));
        gameSettings.put(DataObject.GameSettingType.FRUIT_CHANCE, Double.valueOf(fruitChanceInput.getText()));
        gameSettings.put(DataObject.GameSettingType.FRUIT_MAX, Double.valueOf(fruitMaxInput.getText()));
        gameSettings.put(DataObject.GameSettingType.EDGE_WRAP, edgeWrapInput.isSelected() ? 1d : 0d);
        gameSettings.put(DataObject.GameSettingType.INVINCIBILITY, invincibilityInput.isSelected() ? 1d : 0d);
    }

    private void setPreset(int presetNum) {
        HashMap<DataObject.GameSettingType, Double> gameSettings = sceneNavigator.getDataObject().getGameSettings();

        gameSettings.put(DataObject.GameSettingType.GRID_WIDTH, new double[]{10, 10, 18, 40}[presetNum]);
        gameSettings.put(DataObject.GameSettingType.GRID_HEIGHT,  new double[]{10, 10, 18, 25}[presetNum]);
        gameSettings.put(DataObject.GameSettingType.SNAKE_LENGTH,  new double[]{10, 3, 3, 3}[presetNum]);
        gameSettings.put(DataObject.GameSettingType.FRUIT_CHANCE,  new double[]{0.8, 1, 0.5, 0.1}[presetNum]);
        gameSettings.put(DataObject.GameSettingType.FRUIT_MAX,  new double[]{3, 1, 1, 1}[presetNum]);
        gameSettings.put(DataObject.GameSettingType.EDGE_WRAP,  1d);
        gameSettings.put(DataObject.GameSettingType.INVINCIBILITY,  new double[]{1, 0, 0, 0}[presetNum]);

        loadValues();
        validateValues(); // to clear color
        settingsInfoLabel.setText("Loaded " + new String[]{"beginner", "easy", "medium", "hard"}[presetNum] + " preset.");
    }

    public VBox getSettingsRoot() {
        return settingsVBox;
    }
}
