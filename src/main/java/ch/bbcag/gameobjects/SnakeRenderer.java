package ch.bbcag.gameobjects;

import ch.bbcag.common.DataObject;
import ch.bbcag.common.SceneNavigator;
import ch.bbcag.game.Grid;
import ch.bbcag.game.Node;
import ch.bbcag.game.NodeOrientation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;

import java.util.Random;

public class SnakeRenderer extends GameObject {
    private final GameObjectType type = GameObjectType.SNAKE;
    private final SceneNavigator sceneNavigator;

    private final Grid grid;

    private final Image headSprite = new Image(this.getClass().getResourceAsStream("/media/snake/head.png"));
    private final Image[] bodySprites = new Image[]{new Image(this.getClass().getResourceAsStream("/media/snake/body1.png")), new Image(this.getClass().getResourceAsStream("/media/snake/body2.png")), new Image(this.getClass().getResourceAsStream("/media/snake/body3.png")), new Image(this.getClass().getResourceAsStream("/media/snake/body4.png")), new Image(this.getClass().getResourceAsStream("/media/snake/body5.png"))};
    private final Image tailSprite = new Image(this.getClass().getResourceAsStream("/media/snake/tail.png"));
    private final Image fruitSprite = new Image(this.getClass().getResourceAsStream("/media/snake/apple.png"));

    private ImageView imageTransformer = new ImageView();

    private double delta = 0;

    private final double TILE_SIZE;
    private final double SPRITE_SCALE;

    public SnakeRenderer(SceneNavigator sceneNavigator, Grid grid) {
        this.sceneNavigator = sceneNavigator;
        this.grid = grid;

        TILE_SIZE = sceneNavigator.getDataObject().getGameSettings().get(DataObject.GameSettingType.TILE_SIZE);
        SPRITE_SCALE = TILE_SIZE / 16;

        imageTransformer.setSmooth(false);
    }

    @Override
    public void update(double deltaInSec) {
        delta += deltaInSec;
        if (delta >= 0.5) {
            delta = 0;
            grid.tick();
        }

        if (deltaInSec >= 0.033) {
            System.out.println("WARN: LAG FRAME: (Took " + deltaInSec + "s to compute)");
        }

        for(KeyCode key : sceneNavigator.getDataObject().getKeysPressed()) {
            switch (key) {
                case UP -> grid.update(Node.UpdateType.DIR_UP);
                case DOWN -> grid.update(Node.UpdateType.DIR_DOWN);
                case LEFT -> grid.update(Node.UpdateType.DIR_LEFT);
                case RIGHT -> grid.update(Node.UpdateType.DIR_RIGHT);
            }
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        for (int i = 0; i < grid.getGRID_HEIGHT(); i++) {
            for (int j = 0; j < grid.getGRID_WIDTH(); j++) {
                Node node = grid.getGridNodes()[i][j];
                if (node.isFruit()) {
                    computeImage(fruitSprite, SPRITE_SCALE, node.getOrientation(), gc, j*TILE_SIZE, i*TILE_SIZE);
                }
                switch (node.getSnakeState()) {
                    case HEAD: {
                        computeImage(headSprite, SPRITE_SCALE, node.getOrientation(), gc, j*TILE_SIZE, i*TILE_SIZE);
                        break;
                    }
                    case BODY: {
                        Random random = new Random(i*j*12345+i+j);

                        int r = Math.min(4, random.nextInt(500) / 100);
                        computeImage(bodySprites[r], SPRITE_SCALE, node.getOrientation(), gc, j*TILE_SIZE, i*TILE_SIZE);
                        break;
                    }
                    case TAIL: {
                        computeImage(tailSprite, SPRITE_SCALE, node.getOrientation(), gc, j*TILE_SIZE, i*TILE_SIZE);
                        break;
                    }
                }
            }
        }
    }

    private void computeImage(Image input, double scale, NodeOrientation orientation, GraphicsContext gc, double x, double y) {
        gc.save();

        double rotationAngle = switch (orientation) {
            case UP -> 180;
            case RIGHT -> 270;
            case LEFT -> 90;
            default -> 0;
        };

        double centerX = input.getWidth() / 2;
        double centerY = input.getHeight() / 2;

        gc.translate(x + centerX * scale, y + centerY * scale);
        gc.rotate(rotationAngle);
        gc.scale(scale, scale);
        gc.translate(-centerX, -centerY);

        gc.setImageSmoothing(false);

        gc.drawImage(input, 0, 0);

        gc.restore();
    }

    public void handlePressedKeys(KeyCode keyCode) {
        switch (keyCode) {
            case ESCAPE -> togglePause();
            case Q -> quit();
        }
    }

    private void togglePause(){
        if (grid.getStatus() == Grid.SnakeStatus.RUNNING) {
            grid.setStatus(Grid.SnakeStatus.PAUSED);
        } else if (grid.getStatus() == Grid.SnakeStatus.PAUSED) {
            grid.setStatus(Grid.SnakeStatus.RUNNING);
        }
    }

    private void quit(){
        System.out.println(grid.getStatus());
        if (grid.getStatus() != Grid.SnakeStatus.RUNNING) {
            grid.setStatus(Grid.SnakeStatus.QUIT);
        }
    }
}
