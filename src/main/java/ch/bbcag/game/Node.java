package ch.bbcag.game;

import ch.bbcag.common.SceneNavigator;
import javafx.geometry.Orientation;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Node {
    private final int ID;
    private final Grid parentGrid;

    private NodeSnakeState snakeState = NodeSnakeState.NONE;
    private NodeOrientation orientation = NodeOrientation.NONE;
    private NodeOrientation originOrientation = NodeOrientation.NONE;
    private Set<NodeOrientation> edges = new HashSet<>();
    private int snakeCounter = 0;
    private boolean isFruit = false;

    private Node neighbourNorth;
    private Node neighbourEast;
    private Node neighbourSouth;
    private Node neighbourWest;

    private UUID currentTickId = null;
    private UUID currentUpdateId = null;

    public enum UpdateType {
        UNSET, SNAKE_GROW, DIR_UP, DIR_RIGHT, DIR_DOWN, DIR_LEFT;
    }

    public Node(int ID, Grid grid) {
        this.ID = ID;
        this.parentGrid = grid;
    }

    public void tick(UUID tickId){
        if (tickId == currentTickId) {
            return;
        }

        currentTickId = tickId;

        switch (snakeState) {
            case HEAD: {
                snakeState = NodeSnakeState.BODY;
                if (isFruit) {
                    isFruit = false;
                    parentGrid.setFruitCount(parentGrid.getFruitCount() - 1);
                    snakeCounter++;
                    parentGrid.setSnakeLength(parentGrid.getSnakeLength()+1);
                    currentUpdateId = UUID.randomUUID();
                    neighbourNorth.update(currentUpdateId, UpdateType.SNAKE_GROW);
                }
                Node newHead = switch (orientation) {
                    case UP -> neighbourNorth;
                    case RIGHT -> neighbourEast;
                    case DOWN -> neighbourSouth;
                    default -> neighbourWest;
                };

                if ((!parentGrid.isINVINCIBILITY() && !(newHead.getSnakeState() == NodeSnakeState.NONE) || (edgeDetect() && !parentGrid.isEDGE_WRAP()))) {
                    parentGrid.setStatus(Grid.SnakeStatus.LOST);
                    snakeState = NodeSnakeState.HEAD;
                    return;
                }

                parentGrid.registerHead(newHead);
                newHead.setSnakeState(NodeSnakeState.HEAD);
                newHead.setOrientation(orientation);
                newHead.setSnakeCounter(snakeCounter);
                newHead.setCurrentTickId(currentTickId);
                snakeCounter --;
                break;
            }
            case BODY: {
                snakeCounter --;
                if (snakeCounter == 1) {
                    snakeState = NodeSnakeState.TAIL;
                }
                break;
            }
            case TAIL: {
                snakeCounter = 0;
                snakeState = NodeSnakeState.NONE;
            }
            case NONE: {
                orientation = NodeOrientation.NONE;
                if (Math.random() <= parentGrid.calcNodeFruitChance() && parentGrid.getFruitCount() < parentGrid.getMAX_FRUIT() && !isFruit) {
                    isFruit = true;
                    parentGrid.setFruitCount(parentGrid.getFruitCount() + 1);
                }
            }
        }

        neighbourNorth.tick(tickId);
        neighbourEast.tick(tickId);
    }

    public void update(UUID updateId, UpdateType updateType) {
        if (updateId == currentUpdateId) {
            return;
        }
        currentUpdateId = updateId;
        switch (updateType) {
            case SNAKE_GROW: {
                snakeCounter ++;
                if (snakeState == NodeSnakeState.TAIL) {
                    snakeState = NodeSnakeState.BODY;
                }
                neighbourNorth.update(updateId, updateType);
                neighbourEast.update(updateId, updateType);
                break;
            }
            case DIR_UP: {
                if (!(neighbourNorth.getSnakeCounter() + 1 == snakeCounter)) {
                    orientation = NodeOrientation.UP;
                }
                break;
            }
            case DIR_DOWN: {
                if (!(neighbourSouth.getSnakeCounter() + 1 == snakeCounter)) {
                    orientation = NodeOrientation.DOWN;
                }
                break;
            }

            case DIR_LEFT: {
                if (!(neighbourWest.getSnakeCounter() + 1 == snakeCounter)) {
                    orientation = NodeOrientation.LEFT;
                }
                break;
            }

            case DIR_RIGHT: {
                if (!(neighbourEast.getSnakeCounter() + 1 == snakeCounter)) {
                    orientation = NodeOrientation.RIGHT;
                }
                break;
            }
        }
    }

    private boolean edgeDetect() {
        for (NodeOrientation edgeOrientation : edges) {
            if ((orientation == edgeOrientation)) {
                return true;
            }
        }
        return false;
    }

    public Node getNeighbourNorth() {
        return neighbourNorth;
    }

    public void setNeighbourNorth(Node neighbourNorth) {
        this.neighbourNorth = neighbourNorth;
    }

    public Node getNeighbourEast() {
        return neighbourEast;
    }

    public void setNeighbourEast(Node neighbourEast) {
        this.neighbourEast = neighbourEast;
    }

    public Node getNeighbourSouth() {
        return neighbourSouth;
    }

    public void setNeighbourSouth(Node neighbourSouth) {
        this.neighbourSouth = neighbourSouth;
    }

    public Node getNeighbourWest() {
        return neighbourWest;
    }

    public void setNeighbourWest(Node neighbourWest) {
        this.neighbourWest = neighbourWest;
    }

    public Set<NodeOrientation> getEdges() {
        return edges;
    }

    public void setEdges(Set<NodeOrientation> edges) {
        this.edges = edges;
    }

    public NodeOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(NodeOrientation orientation) {
        this.orientation = orientation;
    }

    public int getID() {
        return ID;
    }

    public NodeSnakeState getSnakeState() {
        return snakeState;
    }

    public void setSnakeState(NodeSnakeState snakeState) {
        this.snakeState = snakeState;
    }

    public int getSnakeCounter() {
        return snakeCounter;
    }

    public void setSnakeCounter(int snakeCounter) {
        this.snakeCounter = snakeCounter;
    }

    public boolean isFruit() {
        return isFruit;
    }

    public void setFruit(boolean fruit) {
        isFruit = fruit;
    }

    public UUID getCurrentTickId() {
        return currentTickId;
    }

    public void setCurrentTickId(UUID currentTickId) {
        this.currentTickId = currentTickId;
    }

    public UUID getCurrentUpdateId() {
        return currentUpdateId;
    }

    public void setCurrentUpdateId(UUID currentUpdateId) {
        this.currentUpdateId = currentUpdateId;
    }
}
