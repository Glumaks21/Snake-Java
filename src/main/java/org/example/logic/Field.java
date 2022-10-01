package org.example.logic;

import java.beans.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Field implements PropertyChangeListener {
    public enum CellState {
        SNAKE, CHERRY, EMPTY;
    }
    private static final int GRID_LENGTH = 13;

    private final PropertyChangeSupport support;

    private final CellState[][] grid;
    private final Snake snake;
    private Cords cherry;

    public Field(Snake snake) {
        Objects.requireNonNull(snake, "snake is null");

        support = new PropertyChangeSupport(this);
        grid = new CellState[GRID_LENGTH][GRID_LENGTH];
        Arrays.stream(grid).
                forEach(line -> Arrays.fill(line, CellState.EMPTY));

        this.snake = snake;
        snake.addPropertyChangeListener(this);
        snake.getLocation().
                forEach(cords -> setCellStateAt(CellState.SNAKE, cords));

        generateCherryCords();
        setCellStateAt(CellState.CHERRY, cherry);
    }

    public int getGridLength() {
        return grid.length;
    }

    public CellState getCellStateAt(Cords cords) {
        if (!isCordsBelong(cords)) {
            throw new IllegalArgumentException("Cords are not belong");
        }

        return grid[cords.getY()][cords.getX()];
    }

    private void setCellStateAt(CellState state, Cords cords) {
        if (!isCordsBelong(cords)) {
            throw new IllegalArgumentException("Cords are not belong");
        }

        grid[cords.getY()][cords.getX()] = state;
    }

    private void generateCherryCords() {
        Collection<Cords> snakeLocation = snake.getLocation();
        Cords cords;
        do {
            int x = (int) (Math.random() * getGridLength());
            int y = (int) (Math.random() * getGridLength());
            cords = new Cords(x, y);
        } while (snakeLocation.contains(cords));

        cherry = cords;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "location":
                List<Cords> newLocations = (List<Cords>) evt.getNewValue();
                List<Cords> oldLocations = (List<Cords>) evt.getOldValue();

                Cords newHeadCords = newLocations.get(0);
                if (!isCordsBelong(newHeadCords)) {
                    snake.setLocation(oldLocations);
                    snake.die();
                } else {
                    setCellStateAt(CellState.SNAKE, newHeadCords);

                    if (cherry.equals(newHeadCords)) {
                        snake.grow();
                        generateCherryCords();
                        setCellStateAt(CellState.CHERRY, cherry);
                    } else {
                        Cords oldTailCords = oldLocations.get(oldLocations.size() - 1);
                        setCellStateAt(CellState.EMPTY, oldTailCords);
                    }
                }

                support.firePropertyChange("grid", null, null);
                break;
            case "size":
                Cords newTailCords = (Cords) evt.getNewValue();
                Cords oldTailCords = (Cords) evt.getOldValue();

                if (newTailCords == null) {
                    setCellStateAt(CellState.EMPTY, oldTailCords);
                } else if (oldTailCords == null) {
                    setCellStateAt(CellState.SNAKE, newTailCords);
                }

                support.firePropertyChange("grid", null, null);
                break;
        }
    }

    @Override
    public String toString() {
        StringBuilder strb = new StringBuilder();
        for (int y = 0; y < getGridLength(); y++) {
            for (int x = 0; x < getGridLength(); x++) {
                switch (grid[y][x]) {
                    case SNAKE:
                        strb.append(" @ ");
                        break;
                    case CHERRY:
                        strb.append(" * ");
                        break;
                    case EMPTY:
                        strb.append(" . ");
                        break;
                }
            }
            strb.append("\n");
        }
        return strb.toString();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    private boolean isCordsBelong(Cords cords) {
        return cords != null &&
                cords.getX() >= 0 && cords.getX() < getGridLength() &&
                cords.getY() >= 0 && cords.getY() < getGridLength();
    }
}
