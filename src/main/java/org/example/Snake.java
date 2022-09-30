package org.example;

import java.util.Deque;
import java.util.LinkedList;

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

    private final Deque<Cords> location;
    private Direction direction;

    public Snake(Cords startPosition) {
        location = new LinkedList<>();
        location.add(startPosition);
        direction = Direction.LEFT;
    }

    public void setDirection(Direction newDirection) {
        if (Math.abs(newDirection.degree - direction.degree) % 180  == 90) {
            direction = newDirection;
        }
    }

    public Direction getDirection() {
        return direction;
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

        location.addFirst(headCords);
        location.removeLast();
    }
}
