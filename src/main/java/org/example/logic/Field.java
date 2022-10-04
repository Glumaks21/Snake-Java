package org.example.logic;

import java.beans.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Field implements PropertyChangeListener {
    public enum CellState {
        SNAKE_HEAD, SNAKE_BODY, CHERRY, EMPTY;
    }
    private static final int GRID_LENGTH = 15;

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
        Cords[] snakeLocation = snake.getLocation();
        setCellStateAt(CellState.SNAKE_HEAD, snakeLocation[0]);
        for (int i = 1; i < snakeLocation.length; i++) {
            setCellStateAt(CellState.SNAKE_BODY, snakeLocation[i]);
        }

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
        support.firePropertyChange("cords_state", null, cords);
    }

    private void generateCherryCords() {
        Cords[] snakeLocation = snake.getLocation();
        boolean occupied = true;
        Cords newCords = null;

        while (occupied) {
            int x = (int) (Math.random() * getGridLength());
            int y = (int) (Math.random() * getGridLength());
            newCords = new Cords(x, y);

            occupied = false;
            for (Cords snakeCords : snakeLocation) {
                if (snakeCords.equals(newCords)) {
                    occupied = true;
                    break;
                }
            }
        }

        cherry = newCords;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "location":
                Cords[] newLocation = (Cords[]) evt.getNewValue();
                Cords[] oldLocation = (Cords[]) evt.getOldValue();

                if (!isCordsBelong(newLocation[0])) {
                    snake.setLocation(oldLocation);
                    snake.die();
                } else {
                    setCellStateAt(CellState.SNAKE_HEAD, newLocation[0]);
                    setCellStateAt(CellState.SNAKE_BODY, oldLocation[0]);

                    if (cherry.equals(newLocation[0])) {
                        snake.grow();
                        generateCherryCords();
                        setCellStateAt(CellState.CHERRY, cherry);
                    } else {
                        setCellStateAt(CellState.EMPTY, oldLocation[oldLocation.length - 1]);
                    }
                }
                break;
            case "size":
                Cords newTailCords = (Cords) evt.getNewValue();
                Cords oldTailCords = (Cords) evt.getOldValue();

                if (newTailCords == null) {
                    setCellStateAt(CellState.EMPTY, oldTailCords);
                } else if (oldTailCords == null) {
                    setCellStateAt(CellState.SNAKE_BODY, newTailCords);
                }

                break;
        }
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
