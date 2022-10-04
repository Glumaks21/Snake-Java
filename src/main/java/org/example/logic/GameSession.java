package org.example.logic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameSession implements PropertyChangeListener {
    public enum State {
        RUNNING, PAUSED, FINISHED;
    }
    private final PropertyChangeSupport support;
    private final Snake snake;
    private int score;
    private State state;

    public GameSession(Snake snake) {
        Objects.requireNonNull(snake, "snake is null");

        support = new PropertyChangeSupport(this);

        this.snake = snake;
        snake.addPropertyChangeListener(this);
    }

    public State getState() {
        return state;
    }

    public void start() {
        if (state == State.FINISHED) {
            throw new IllegalStateException("Already finished");
        }

        state = State.RUNNING;
        support.firePropertyChange("state", null, State.RUNNING);

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> {
            if (state == State.FINISHED || state == State.PAUSED) {
                service.shutdown();
            }

            snake.move();
        }, 1000, 300, TimeUnit.MILLISECONDS);
    }

    public void pause() {
        if (state == State.RUNNING) {
            state = State.PAUSED;
            support.firePropertyChange("state", null, State.PAUSED);
        } else if (state == State.PAUSED) {
            start();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "size":
                if (state == State.RUNNING && evt.getNewValue() != null) {
                    score++;
                    support.firePropertyChange("score", null, score);
                }
                break;
            case "alive":
                if (!(Boolean) evt.getNewValue()) {
                    state = State.FINISHED;
                    support.firePropertyChange("state", null, State.FINISHED);
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
}
