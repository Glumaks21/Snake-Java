package org.example.controllers;

import org.example.logic.GameSession;
import org.example.logic.Snake;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class SnakeController extends KeyAdapter {
    private final Snake snake;
    private final GameSession gameSession;

    public SnakeController(Snake snake, GameSession gameSession) {
        Objects.requireNonNull(snake, "snake is null");
        Objects.requireNonNull(gameSession, "game session is null");

        this.snake = snake;
        this.gameSession = gameSession;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameSession.getState() == GameSession.State.RUNNING) {
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
}
