package org.example.logic;

import java.beans.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Field implements PropertyChangeListener {
    public enum GridCell {
        SNAKE, CHERRY, EMPTY;
    }
    private static final int GRID_LENGTH = 13;

    private final PropertyChangeSupport support;

    private final GridCell[][] grid;
    private final Snake snake;
    private Cords cherry;

    public Field(Snake snake) {
        Objects.requireNonNull(snake, "snake is null");

        support = new PropertyChangeSupport(this);
        grid = new GridCell[GRID_LENGTH][GRID_LENGTH];
        this.snake = snake;
        snake.addPropertyChangeListener(this);
        generateCherryCords();
        mark();
    }

    public int getGridLength() {
        return grid.length;
    }

    Snake getSnake() {
        return snake;
    }

    public GridCell getCellStateAt(Cords cords) {
        if (!isCordsBelong(cords)) {
            throw new IllegalArgumentException();
        }

        return grid[cords.getY()][cords.getX()];
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

    private void mark() {
        Arrays.stream(grid).
                forEach(line -> Arrays.fill(line, GridCell.EMPTY));
        snake.getLocation().
                forEach(cords -> grid[cords.getY()][cords.getX()] = GridCell.SNAKE);
        grid[cherry.getY()][cherry.getX()] = GridCell.CHERRY;

        support.firePropertyChange("grid", null, null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "location":
                List<Cords> newLocations = (List<Cords>) evt.getNewValue();
                List<Cords> oldLocations = (List<Cords>) evt.getOldValue();

                Cords headCords = newLocations.get(0);
                if (cherry.equals(headCords)) {
                    snake.grow();
                    generateCherryCords();
                } else if (!isCordsBelong(headCords)) {
                    snake.setHeadCords(oldLocations);
                    snake.die();
                }

                mark();
                break;
            case "size":
                mark();
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
