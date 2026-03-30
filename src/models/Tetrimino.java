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

    public static Tetrimino create() {
        var type = TetriminoType.random();
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

    //public void moveToShadow() {
    //    if (this.cordsShadow == null) return;

    //    Coordinate[] newCords = new Coordinate[this.cordsShadow.length];
    //    for (int i = 0; i < cordsShadow.length; i++) {
    //        newCords[i] = cordsShadow[i];
    //    }

    //    this.cords = newCords;
    //}

    public void rotate() {
        if (!this.canRotate) return;

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

    public int getLimitCords( Direction direction ) {
        for (var cord : cords) {
            if (cord.getDirection() == direction)
                return switch (direction) {
                    case DOWN -> cord.y;
                    case LEFT -> cord.x;
                    case RIGHT -> cord.x;
                    case UP -> cord.y;
                    default -> -1;
                };
        }

        return -1;
    }

    public Coordinate[] getCords() { return cords; }

    public TetriminoType getTetriminoType() { return tetriminoType; }

    public BlockColor getColor() { return color; }

    public boolean canMove() { return canMove; }

    public void setCanMove(boolean canMove) { this.canMove = canMove; }

}