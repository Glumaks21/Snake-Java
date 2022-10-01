package org.example;

import org.example.gui.FieldPanel;
import org.example.gui.ScorePanel;
import org.example.logic.Field;
import org.example.logic.GameSession;
import org.example.logic.Snake;
import javax.swing.*;
import java.awt.*;

public class Launcher {
    public static void main(String[] args) {
        Snake snake = new Snake();
        Field field = new Field(snake);
        GameSession gameSession = new GameSession(field, snake);

        JFrame frame = new JFrame();
        ScorePanel scorePanel = new ScorePanel(gameSession);
        FieldPanel fieldPanel = new FieldPanel(field);
        frame.getContentPane().add(BorderLayout.NORTH, scorePanel);
        frame.getContentPane().add(BorderLayout.CENTER, fieldPanel);

        frame.addKeyListener(new SnakeController(snake));
        frame.addKeyListener(new GameSessionController(gameSession));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        gameSession.start();
    }
}
