package org.example.controllers;

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
        switch (e.getKeyCode()) {
            case  KeyEvent.VK_SPACE:
                gameSession.pause();
                break;
            case KeyEvent.VK_ESCAPE:
                gameSession.start();
                break;
        }
    }
}
