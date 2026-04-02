package enums;

import models.Coordinate;

public enum TetriminoType {
    I, J, L, O, S, Z, T;

    public static Coordinate[] getCordsByType(TetriminoType type) {
        switch (type) {
            case I -> {
                return new Coordinate[] {
                        new Coordinate(3, 0), new Coordinate(4, 0),
                        new Coordinate(5, 0, true), new Coordinate(6, 0)};
            }
            case J -> {
                return new Coordinate[] {
                        new Coordinate(3, 0), new Coordinate(3, 1),
                        new Coordinate(4, 1, true), new Coordinate(5, 1)};
            }
            case L -> {
                return new Coordinate[] {
                        new Coordinate(5, 0), new Coordinate(5, 1),
                        new Coordinate(4, 1, true), new Coordinate(3, 1)};
            }
            case O -> {
                return new Coordinate[] {
                        new Coordinate(4, 0), new Coordinate(5, 0),
                        new Coordinate(4, 1), new Coordinate(5, 1)};
            }
            case S -> {
                return new Coordinate[] {
                        new Coordinate(3, 1), new Coordinate(4, 1, true),
                        new Coordinate(4, 0), new Coordinate(5, 0)};
            }
            case Z -> {
                return new Coordinate[] {
                        new Coordinate(3, 0), new Coordinate(4, 0),
                        new Coordinate(4, 1, true), new Coordinate(5, 1)};
            }
            case T -> {
                return new Coordinate[] {
                        new Coordinate(3, 1), new Coordinate(4, 0),
                        new Coordinate(4, 1, true), new Coordinate(5, 1)};
            }
            default -> {
            }
        }
        return null;
    }
}