package org.example.gui;

import org.example.logic.Field;
import org.example.logic.GameSession;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class GameFrame extends JFrame implements PropertyChangeListener {
    private final GameSession gameSession;
    private final ScorePanel scorePanel;
    private final FieldPanel fieldPanel;

    public GameFrame(GameSession gameSession, Field field) {
        Objects.requireNonNull(gameSession, "game session is null");
        Objects.requireNonNull(field, "field is null");

        this.gameSession = gameSession;
        gameSession.addPropertyChangeListener(this);
        scorePanel = new ScorePanel(gameSession);
        fieldPanel = new FieldPanel(field);

        getContentPane().add(scorePanel, BorderLayout.NORTH);
        getContentPane().add(fieldPanel, BorderLayout.CENTER);

        setTitle("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
