package models;

public class Coordinate {
    public int x = 0;
    public int y = 0;

    private boolean isPivot = false;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(int x, int y, boolean isPivot) {
        this.x = x;
        this.y = y;
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

    public boolean isPivot() {
        return isPivot;
    }

}