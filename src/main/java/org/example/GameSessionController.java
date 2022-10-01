package org.example;

import org.example.logic.GameSession;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class GameSessionController extends KeyAdapter {
    private final GameSession gameSession;

    public GameSessionController(GameSession gameSession) {
        Objects.requireNonNull(gameSession, "game session is null");
        this.gameSession = gameSession;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            gameSession.pause();
        }
    }
}
