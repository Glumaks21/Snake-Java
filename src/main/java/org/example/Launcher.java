package org.example;

import org.example.controllers.GameSessionController;
import org.example.controllers.SnakeController;
import org.example.gui.GameFrame;
import org.example.logic.Field;
import org.example.logic.GameSession;
import org.example.logic.Snake;
import javax.swing.*;

public class Launcher {
    public static void main(String[] args) {
        Snake snake = new Snake();
        Field field = new Field(snake);
        GameSession gameSession = new GameSession(snake);

        JFrame frame = new GameFrame(gameSession, field);

        frame.addKeyListener(new SnakeController(snake, gameSession));
        frame.addKeyListener(new GameSessionController(gameSession));

        frame.setVisible(true);
        gameSession.start();
    }
}
