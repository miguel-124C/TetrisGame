package enums;

import java.util.concurrent.ThreadLocalRandom;

import models.Coordinate;

public enum TetriminoType {
    I, J, L, O, S, Z, T;

    public static TetriminoType random() {
        var types = values();
        int index = ThreadLocalRandom.current().nextInt(types.length);
        return types[index];
    }

    public static Coordinate[] getCordsByType(TetriminoType type) {
        switch (type) {
            case I -> {
                return new Coordinate[] {
                        new Coordinate(3, 0, Direction.LEFT), new Coordinate(4, 0, Direction.UP),
                        new Coordinate(5, 0, Direction.DOWN, true), new Coordinate(6, 0, Direction.RIGHT)};
            }
            case J -> {
                return new Coordinate[] {
                        new Coordinate(3, 0, Direction.UP), new Coordinate(3, 1, Direction.LEFT),
                        new Coordinate(4, 1, Direction.DOWN, true), new Coordinate(5, 1, Direction.RIGHT)};
            }
            case L -> {
                return new Coordinate[] {
                        new Coordinate(5, 0, Direction.UP), new Coordinate(5, 1, Direction.RIGHT),
                        new Coordinate(4, 1, Direction.DOWN, true), new Coordinate(3, 1, Direction.LEFT)};
            }
            case O -> {
                return new Coordinate[] {
                        new Coordinate(4, 0, Direction.LEFT), new Coordinate(5, 0, Direction.UP),
                        new Coordinate(4, 1, Direction.DOWN), new Coordinate(5, 1, Direction.RIGHT)};
            }
            case S -> {
                return new Coordinate[] {
                        new Coordinate(3, 1, Direction.LEFT), new Coordinate(4, 1, Direction.DOWN, true),
                        new Coordinate(4, 0, Direction.UP), new Coordinate(5, 0, Direction.RIGHT)};
            }
            case Z -> {
                return new Coordinate[] {
                        new Coordinate(3, 0, Direction.LEFT), new Coordinate(4, 0, Direction.UP),
                        new Coordinate(4, 1, Direction.DOWN, true), new Coordinate(5, 1, Direction.RIGHT)};
            }
            case T -> {
                return new Coordinate[] {
                        new Coordinate(3, 1, Direction.LEFT), new Coordinate(4, 0, Direction.UP),
                        new Coordinate(4, 1, Direction.DOWN, true), new Coordinate(5, 1, Direction.RIGHT)};
            }
            default -> {
            }
        }
        return null;
    }
}