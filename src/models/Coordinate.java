package models;

import enums.Direction;

public class Coordinate {
    public int x = 0;
    public int y = 0;

    // Coordinate max and min for x and y
    private Direction direction = null;
    private boolean isPivot = false;

    public Coordinate(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public Coordinate(int x, int y, Direction direction, boolean isPivot) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.isPivot = isPivot;
    }

    public void rotateCord(Coordinate pivot) {
        // Si es el pivot, no se rota
        if (isPivot) return;

        var xRel = this.x - pivot.x;
        var yRel = this.y - pivot.y;

        var xRotateRel = -1 * yRel;

        var xAbs = xRotateRel + pivot.x;
        var yAbs = xRel + pivot.y;

        this.x = xAbs;
        this.y = yAbs;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isPivot() {
        return isPivot;
    }

}
