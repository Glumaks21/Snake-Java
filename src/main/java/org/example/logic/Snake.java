package org.example.logic;

import java.beans.*;
import java.util.*;
import java.util.concurrent.*;


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
    private Deque<Cords> location;
    private Direction direction;
    private Cords tailTrace;

    public Snake() {
        support = new PropertyChangeSupport(this);
        location = new LinkedList<>();
        location.add(new Cords(0, 0));
        direction = Direction.RIGHT;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction newDirection) {
        if (Math.abs(newDirection.getDegree() - direction.getDegree()) % 180  == 90) {
            direction = newDirection;
        }
    }

    public List<Cords> getLocation() {
        return new ArrayList<>(location);
    }

    void setHeadCords(List<Cords> location) {
        this.location = new LinkedList<>(location);
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

        if (location.contains(headCords)) {
            die();
        } else {
            List<Cords> locationBefore = getLocation();
            location.addFirst(headCords);
            tailTrace = location.removeLast();
            List<Cords> locationAfter = getLocation();
            support.firePropertyChange("location",
                    locationBefore, locationAfter);
        }
    }

    public void grow() {
        if (tailTrace != null) {
            location.addLast(tailTrace);
            tailTrace = null;
            support.firePropertyChange("size",
                    location.size() - 1, location.size());
        }
    }

    public void die() {
        support.firePropertyChange("alive", true, false);
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
