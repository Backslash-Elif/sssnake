package ch.bbcag.game;

import java.util.UUID;

public class Grid {
    private int snakeLength;
    public enum SnakeStatus {
        RUNNING, PAUSED, WON, LOST, QUIT;
    }
    private SnakeStatus status = SnakeStatus.RUNNING;

    private final int GRID_WIDTH;
    private final int GRID_HEIGHT;
    private final int NODE_COUNT;

    private Node[][] gridNodes;
    private Node snakeHead;

    private int fruitCount = 0;
    private final double FRUIT_CHANCE;
    private final int MAX_FRUIT;

    private final boolean EDGE_WRAP;
    private final boolean INVINCIBILITY;

    public Grid(int width, int height, int snakeLength, double fruitChance, int maxFruit, boolean edgeWrap, boolean invincibility) {
        this.snakeLength = snakeLength;
        this.GRID_WIDTH = width;
        this.GRID_HEIGHT = height;
        this.NODE_COUNT = width*height;
        this.FRUIT_CHANCE = fruitChance;
        this.MAX_FRUIT = maxFruit;
        this.EDGE_WRAP = edgeWrap;
        this.INVINCIBILITY = invincibility;

        this.init();
    }

    private void init() {
        gridNodes = new Node[GRID_HEIGHT][GRID_WIDTH];
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                gridNodes[i][j] = new Node(i * j, this);
            }
        }

        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                // set neighbours
                gridNodes[i][j].setNeighbourNorth(gridNodes[i == 0 ? GRID_HEIGHT - 1 : i - 1][j]);
                gridNodes[i][j].setNeighbourEast(gridNodes[i][j == GRID_WIDTH - 1 ? 0 : j + 1]);
                gridNodes[i][j].setNeighbourSouth(gridNodes[i == GRID_HEIGHT - 1 ? 0 : i + 1][j]);
                gridNodes[i][j].setNeighbourWest(gridNodes[i][j == 0 ? GRID_WIDTH - 1 : j - 1]);

                //set edges
                if (i == 0) {
                    gridNodes[i][j].getEdges().add(NodeOrientation.UP);
                }
                if (j == 0) {
                    gridNodes[i][j].getEdges().add(NodeOrientation.LEFT);
                }
                if (i == GRID_HEIGHT - 1) {
                    gridNodes[i][j].getEdges().add(NodeOrientation.DOWN);
                }
                if (j == GRID_WIDTH - 1) {
                    gridNodes[i][j].getEdges().add(NodeOrientation.RIGHT);
                }
            }
        }

        snakeHead = gridNodes[GRID_HEIGHT / 2][GRID_WIDTH / 2];
        snakeHead.setSnakeCounter(snakeLength);
        snakeHead.setOrientation(NodeOrientation.LEFT);
        snakeHead.setSnakeState(NodeSnakeState.HEAD);
    }

    public void registerHead(Node head){
        snakeHead = head;
    }

    double calcNodeFruitChance() {
        return FRUIT_CHANCE / (NODE_COUNT - snakeLength);
    }

    public void tick() {
        if (status == SnakeStatus.RUNNING){
            UUID tickUuid = UUID.randomUUID();
            snakeHead.tick(tickUuid);
        }
    }

    public void update(Node.UpdateType updateType) {
        if (status == SnakeStatus.RUNNING){
            UUID updatekUuid = UUID.randomUUID();
            snakeHead.update(updatekUuid, updateType);
        }
    }

    public int getSnakeLength() {
        return snakeLength;
    }

    public void setSnakeLength(int snakeLength) {
        this.snakeLength = snakeLength;
    }

    public Node[][] getGridNodes() {
        return gridNodes;
    }

    public int getGRID_WIDTH() {
        return GRID_WIDTH;
    }

    public int getGRID_HEIGHT() {
        return GRID_HEIGHT;
    }

    public double getFRUIT_CHANCE() {
        return FRUIT_CHANCE;
    }

    public int getFruitCount() {
        return fruitCount;
    }

    public void setFruitCount(int fruitCount) {
        this.fruitCount = fruitCount;
    }

    public int getNODE_COUNT() {
        return NODE_COUNT;
    }

    public int getMAX_FRUIT() {
        return MAX_FRUIT;
    }

    public boolean isEDGE_WRAP() {
        return EDGE_WRAP;
    }

    public boolean isINVINCIBILITY() {
        return INVINCIBILITY;
    }

    public SnakeStatus getStatus() {
        return status;
    }

    public void setStatus(SnakeStatus status) {
        this.status = status;
    }
}
