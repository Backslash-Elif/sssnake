package ch.bbcag.gameobjects;

import ch.bbcag.game.Grid;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ScreenRenderer extends GameObject{
    private final Grid grid;

    private final double CANVAS_WIDTH;
    private final double CANVAS_HEIGTH;

    private final Image WIN_SCREEN = new Image(this.getClass().getResourceAsStream("/media/screen/winscreen.png"));
    private final Image LOSE_SCREEN = new Image(this.getClass().getResourceAsStream("/media/screen/losescreen.png"));
    private final Image PAUSE_SCREEN = new Image(this.getClass().getResourceAsStream("/media/screen/pausescreen.png"));

    public ScreenRenderer(Grid grid, double tileSize) {
        this.grid = grid;
        this.CANVAS_WIDTH = grid.getGRID_WIDTH()*tileSize;
        this.CANVAS_HEIGTH = grid.getGRID_HEIGHT()*tileSize;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (grid.getStatus() == Grid.SnakeStatus.RUNNING) {
            return;
        }

        Image image = switch (grid.getStatus()) {
            case WON -> WIN_SCREEN;
            case LOST -> LOSE_SCREEN;
            default -> PAUSE_SCREEN;
        };

        // Calculate the scale factor to make the image 50% of the canvas size
        double scale = 0.5 * Math.min(CANVAS_WIDTH / image.getWidth(), CANVAS_HEIGTH / image.getHeight());

        // Calculate the top-left corner to center the image
        double xOffset = (CANVAS_WIDTH - image.getWidth() * scale) / 2;
        double yOffset = (CANVAS_HEIGTH - image.getHeight() * scale) / 2;

        // Apply scaling and translation to the GraphicsContext
        gc.save(); // Save the current state of the GraphicsContext
        gc.translate(xOffset, yOffset);
        gc.scale(scale, scale);

        // Draw the image without smoothening
        gc.setImageSmoothing(false);
        gc.drawImage(image, 0, 0);

        gc.restore(); // Restore the original state of the GraphicsContext
    }
}
