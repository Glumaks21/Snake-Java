package org.example;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

public class Field implements PropertyChangeListener {
    public enum GridCell {
        SNAKE, CHERRY, EMPTY;
    }
    private static final int GRID_LENGTH = 13;

    private final GridCell[][] grid;
    private final Snake snake;
    private Cords cherry;

    public Field() {
        grid = new GridCell[GRID_LENGTH][GRID_LENGTH];
        Arrays.stream(grid).
                forEach((line) -> Arrays.fill(line, GridCell.EMPTY));

        Cords snakeHeadCords = new Cords(getGridLength() / 2, getGridLength() / 2);
        snake = new Snake(snakeHeadCords);
        snake.addPropertyChangeListener(this);
        Arrays.stream(snake.getLocation()).
                forEach(cords -> grid[cords.getY()][cords.getX()] = GridCell.SNAKE);

        generateCherryCords();
        grid[cherry.getY()][cherry.getX()] = GridCell.CHERRY;
    }

    public Snake getSnake() {
        return snake;
    }

    public int getGridLength() {
        return grid.length;
    }

    private void generateCherryCords() {
        int x, y;
        do {
            x = (int) (Math.random() * getGridLength());
            y = (int) (Math.random() * getGridLength());
        } while (grid[y][x] != GridCell.EMPTY);

        cherry = new Cords(x, y);
    }

    private void mark() {
        Arrays.stream(grid).
                forEach((line) -> Arrays.fill(line, GridCell.EMPTY));
        Arrays.stream(snake.getLocation()).
                forEach(cords -> grid[cords.getY()][cords.getX()] = GridCell.SNAKE);
        grid[cherry.getY()][cherry.getX()] = GridCell.CHERRY;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "location" :
                Cords newHeadCords = (Cords) evt.getNewValue();

                if (cherry.equals(newHeadCords)) {
                    snake.grow();
                    grid[newHeadCords.getY()][newHeadCords.getX()] = GridCell.SNAKE;
                    generateCherryCords();
                }

                mark();
                break;
        }
    }

    @Override
    public String toString() {
        StringBuffer strb = new StringBuffer();
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
}
