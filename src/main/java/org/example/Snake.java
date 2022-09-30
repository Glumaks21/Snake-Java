package org.example;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Snake {
    public enum Direction {
        RIGHT(0), UP(90), LEFT(180), DOWN(270);

        private final int degree;

        Direction(int degree) {
            this.degree = degree;
        }

        public int getDegree() {
            return degree;
        }
    }

    private final PropertyChangeSupport support;
    private final Deque<Cords> location;
    private Direction direction;
    private Cords tailTrace;

    public Snake(Cords startPosition) {
        support = new PropertyChangeSupport(this);
        location = new LinkedList<>();
        location.add(startPosition);
        direction = Direction.LEFT;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction newDirection) {
        if (Math.abs(newDirection.degree - direction.degree) % 180  == 90) {
            direction = newDirection;
        }
    }

    public Cords[] getLocation() {
        return location.toArray(new Cords[0]);
    }

    public void move() {
        Cords headCords = location.getFirst();
        switch (direction) {
            case RIGHT:
                headCords = new Cords(headCords.getX() + 1, headCords.getY());
                break;
            case UP:
                headCords = new Cords(headCords.getX(), headCords.getY() - 1);
                break;
            case LEFT:
                headCords = new Cords(headCords.getX() - 1, headCords.getY());
                break;
            case DOWN:
                headCords = new Cords(headCords.getX(), headCords.getY() + 1);
                break;
        }

        tailTrace = location.removeLast();
        location.addFirst(headCords);
        support.firePropertyChange("location", null, headCords);
    }

    public boolean isAlive() {
        return !location.isEmpty();
    }

    public void grow() {
        if (!isAlive()) {
            throw new IllegalArgumentException("Already died");
        }

        if (tailTrace != null) {
            location.addLast(tailTrace);
            tailTrace = null;
            support.firePropertyChange("size",
                    location.size() - 1, location.size());
        }
    }

    public void die() {
        if (!isAlive()) {
            throw new IllegalArgumentException("Already died");
        }

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> {
            if (location.isEmpty()) {
                service.shutdown();
            }

            location.removeLast();
            support.firePropertyChange("size",
                    location.size() + 1, location.size());
        }, 500, 300, TimeUnit.MILLISECONDS);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
