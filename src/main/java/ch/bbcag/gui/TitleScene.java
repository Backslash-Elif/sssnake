package ch.bbcag.gui;

import ch.bbcag.common.SceneNavigator;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import static ch.bbcag.gui.ItemCreator.createButton;
import static ch.bbcag.gui.ItemCreator.createLabel;

public class TitleScene extends SceneExtension {
    private final SceneNavigator sceneNavigator;
    private final GameSettings settings;
    public TitleScene(SceneNavigator sceneNavigator) {
        super(new BorderPane());

        this.sceneNavigator = sceneNavigator;

        if (!this.sceneNavigator.getPrimaryStage().isMaximized()) {
            this.sceneNavigator.setStageHeight(350); //350
            this.sceneNavigator.setStageWidth(500);
        }

        //Title
        Label title = createLabel("Sssnake", ItemCreator.LabelType.HEADING1);
        Button startGame = createButton("Play", ItemCreator.ButtonType.NORMAL);
        Button configure = createButton("Configure...", ItemCreator.ButtonType.MEDIUM);
        Button exit = createButton("Exit", ItemCreator.ButtonType.SMALL);

        settings = new GameSettings(sceneNavigator);

        VBox menuVBox = new VBox(16);

        menuVBox.getChildren().addAll(title, startGame, configure, settings.getSettingsRoot(), exit);
        menuVBox.setAlignment(Pos.CENTER);

        startGame.setOnAction(e-> sceneNavigator.switchScene(SceneNavigator.Scenes.GAME));
        configure.setOnAction(e-> toggleSettings());
        exit.setOnAction(e-> Platform.exit());

        BorderPane root = (BorderPane) getRoot();
        root.setCenter(menuVBox);
    }

    private void toggleSettings() {
        VBox settingsRoot = settings.getSettingsRoot();
        if (settingsRoot.isVisible()) {
            settingsRoot.setVisible(false);
            settingsRoot.setManaged(false);
            if (!this.sceneNavigator.getPrimaryStage().isMaximized()) {
                this.sceneNavigator.setStageHeight(350);
            }
        } else {
            settingsRoot.setVisible(true);
            settingsRoot.setManaged(true);
            if (!this.sceneNavigator.getPrimaryStage().isMaximized()) {
                this.sceneNavigator.setStageHeight(650);
            }
        }
        this.sceneNavigator.setStageWidth(500);
    }
}
