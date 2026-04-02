package models;

import enums.*;

public class Tetrimino {
    private Coordinate[] cords;
    private final BlockColor color;
    private final TetriminoType tetriminoType;
    private boolean canMove = true;
    private boolean canRotate = true;

    private Tetrimino(Coordinate[] cords, BlockColor color, TetriminoType type) {
        this.color = color;
        this.cords = cords;
        this.tetriminoType = type;
        
        // El pivot es el segundo bloque de cada tetrimino, excepto para el O que no tiene pivot
        if (type == TetriminoType.O) this.canRotate = false;
    }

    // Constructor de copia
    public Tetrimino(Tetrimino original) {
        this.cords = new Coordinate[original.getCords().length];
        for (int i = 0; i < original.getCords().length; i++) {
            var cord = original.getCords()[i];
            var x = cord.x;
            var y = cord.y;
            this.cords[i] = new Coordinate(x, y);
        }
        color = original.getColor();
        tetriminoType = original.getTetriminoType();
    }

    public static Tetrimino create(TetriminoType type) {
        var cords = TetriminoType.getCordsByType(type);
        return new Tetrimino(cords, BlockColor.random(), type);
    }

    public void move(Direction direction) {
        if (!this.canMove) return;

        for (var coordinate : this.cords) {
            if (direction == Direction.DOWN) {
                coordinate.y += 1;
            } else if (direction == Direction.RIGHT) {
                coordinate.x += 1;
            } else if (direction == Direction.LEFT) {
                coordinate.x -= 1;
            }
            // En el tetris no se mueve hacia arriba.
            // Esta direccion se usa para revertir el movimiento hacia abajo cuando hay colision, por eso se mueve hacia arriba en ese caso.
            else if (direction == Direction.UP) {
                coordinate.y -= 1;
            }
        }
    }

    public void moveToShadow(Coordinate[] shadowCords) {
       this.cords = shadowCords;
    }

    public void rotate() {
        var pivot = getCoordPivot();
        if (pivot == null) return;

        for (var cord : cords) {
            cord.rotateCord(pivot);
        }
    }

    private Coordinate getCoordPivot() {
        for (var cord : cords) {
            if (cord.isPivot()) return cord;
        }
        return null;
    }

    // Obtiene la coordenada mas cerca a la parte baja de la matriz
    public int getCoordMaxTop() {
        int max = Integer.MIN_VALUE;
        for (var cord : this.cords) {
            if (cord.y > max) max = cord.y;
        }

        return max;
    }

    // Obtiene la coordenada mas cerca a la parte alta de la matriz
    public int getCoordMaxBottom() {
        var men = Integer.MAX_VALUE;
        for (var cord : this.cords) {
            if (cord.y < men) men = cord.y;
        }

        return men;
    }

    public Coordinate[] getCords() { return cords; }

    public TetriminoType getTetriminoType() { return tetriminoType; }

    public BlockColor getColor() { return color; }

    public boolean canMove() { return canMove; }

    public void setCanMove(boolean canMove) { this.canMove = canMove; }

    public boolean isCanRotate() { return canRotate; }
}