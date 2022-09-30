package org.example;

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
    private boolean isAlive;

    public Snake(Cords startPosition) {
        support = new PropertyChangeSupport(this);
        location = new LinkedList<>();
        location.add(startPosition);
        direction = Direction.LEFT;
        isAlive = true;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction newDirection) {
        if (!isAlive) {
            throw new IllegalStateException("Already died");
        }

        if (Math.abs(newDirection.degree - direction.degree) % 180  == 90) {
            direction = newDirection;
        }
    }

    public List<Cords> getLocation() {
        return new ArrayList<>(location);
    }

    void setLocation(List<Cords> location) {
        this.location = new LinkedList<>(location);
    }

    public void move() {
        if (!isAlive) {
            throw new IllegalStateException("Already died");
        }

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

    public boolean isAlive() {
        return isAlive;
    }

    public void grow() {
        if (!isAlive) {
            throw new IllegalStateException("Already died");
        }

        if (tailTrace != null) {
            location.addLast(tailTrace);
            tailTrace = null;
            support.firePropertyChange("size",
                    location.size() - 1, location.size());
        }
    }

    public void die() {
        if (!isAlive) {
            throw new IllegalStateException("Already died");
        }

        isAlive = false;
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> {
            if (location.isEmpty()) {
                service.shutdown();
            }

            System.out.println(location.size());
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
