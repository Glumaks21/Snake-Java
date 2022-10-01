package org.example.gui;

import org.example.logic.GameSession;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class ScorePanel extends JPanel implements PropertyChangeListener {
    private final GameSession gameSession;
    private final JLabel label;

    public ScorePanel(GameSession gameSession) {
        Objects.requireNonNull(gameSession, "game session is null");
        this.gameSession = gameSession;
        gameSession.addPropertyChangeListener(this);

        label = new JLabel();
        label.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 50));
        label.setText("0");
        add(label);

        setPreferredSize(new Dimension(100, 100));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("score")) {
            Integer newScore = (Integer) (evt.getNewValue());
            label.setText(newScore.toString());
        }
    }
}
