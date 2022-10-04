package org.example.logic;

import java.beans.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


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
    private boolean canTurn;

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
        if (canTurn && Math.abs(newDirection.getDegree() - direction.getDegree()) % 180  == 90) {
            direction = newDirection;
            canTurn = false;
        }
    }

    public Cords[] getLocation() {
        return location.toArray(new Cords[0]);
    }

    void setLocation(Cords[] location) {
        this.location = Arrays.stream(location).
                collect(Collectors.toCollection(LinkedList::new));
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
            canTurn = true;
            Cords[] locationBefore = getLocation();
            location.addFirst(headCords);
            tailTrace = location.removeLast();
            Cords[] locationAfter = getLocation();
            support.firePropertyChange("location",
                    locationBefore, locationAfter);
        }
    }

    public void grow() {
        if (tailTrace != null) {
            location.addLast(tailTrace);
            tailTrace = null;
            support.firePropertyChange("size",
                    null, location.peekLast());
        }
    }

    public void die() {
        support.firePropertyChange("alive", true, false);

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> {
                if (!location.isEmpty()) {
                    support.firePropertyChange("size",
                            location.removeLast(), null);
                } else {
                    service.shutdown();
                }
        } , 600, 200, TimeUnit.MILLISECONDS);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
