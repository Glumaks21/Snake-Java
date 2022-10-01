package org.example;

import org.example.logic.Snake;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class SnakeController extends KeyAdapter {
    private final Snake snake;

    public SnakeController(Snake snake) {
        Objects.requireNonNull(snake, "snake is null");
        this.snake = snake;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                snake.setDirection(Snake.Direction.RIGHT);
                break;
            case KeyEvent.VK_UP:
                snake.setDirection(Snake.Direction.UP);
                break;
            case KeyEvent.VK_LEFT:
                snake.setDirection(Snake.Direction.LEFT);
                break;
            case KeyEvent.VK_DOWN:
                snake.setDirection(Snake.Direction.DOWN);
                break;
        }
    }
}
