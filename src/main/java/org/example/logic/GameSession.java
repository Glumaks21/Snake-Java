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
    private final Field field;
    private final Snake snake;
    private int score;
    private State state;

    public GameSession(Field field, Snake snake) {
        Objects.requireNonNull(field, "field is null");
        Objects.requireNonNull(snake, "snake is null");

        support = new PropertyChangeSupport(this);
        this.field = field;
        this.snake = snake;
        snake.addPropertyChangeListener(this);
    }

    public void start() {
        state = State.RUNNING;

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> {
            if (state == State.FINISHED || state == State.PAUSED) {
                service.shutdown();
            }

            snake.move();
        }, 600, 300, TimeUnit.MILLISECONDS);
    }

    public void pause() {
        if (state == State.RUNNING) {
            state = State.PAUSED;
        } else if (state == State.PAUSED) {
            state = State.RUNNING;
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
