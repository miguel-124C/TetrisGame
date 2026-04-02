package models;

import enums.*;

public class Board {
    private final BlockColor empty;
    private final int ROW;
    private final int COL;
    private int numTerm = 0;
    private final int[] vectorRow;
    private final int[] vectorCol;
    private final BlockColor[] vectorValue;

    private int highestRow;
    private Coordinate[] shadowCoords;
    private boolean showShadow = true;

    public Board(int rows, int columns, BlockColor empty) {
        this.ROW = rows;
        this.COL = columns;
        this.empty = empty;
        this.highestRow = rows;

        vectorRow = new int[ROW * COL];
        vectorCol = new int[ROW * COL];
        vectorValue = new BlockColor[ROW * COL];

        initializeMatrix();
    }

    private void initializeMatrix() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                insert(i, j, this.empty);
            }
        }
    }

    private int search( int x, int y ) {
        if (!existPos(x, y)) return -1;
        if (numTerm != 0) {
            for (int i = 0; i < numTerm; i++) {
                if ((vectorRow[i] == x) && (vectorCol[i] == y)) {
                    return i;
                }
            }
        }

        return -1;
    }

    public void insert(int x, int y, BlockColor value) {
        int lug = search(x, y);
        if (lug != -1) vectorValue[lug] = value;
        else {
            numTerm++;
            vectorValue[numTerm-1] = value;
            vectorRow[numTerm-1] = x;
            vectorCol[numTerm-1] = y;
        }
    }

    public int getCantLines() {
        int cantLines = 0;
        for (int i = highestRow; i < ROW; i++) {
            var cantValues = 0;
            for (int j = 0; j < COL; j++) {
                if (getValue(i, j) == empty) break;
                cantValues++;
            }

            if ( cantValues == COL ) {
                destroyLines(i);
                cantLines++;
            };
        }

        return cantLines;
    }

    public BlockColor getValue(int x, int y) {
        if (!existPos(x, y)) return empty;

        int lug = search(x, y);
        return (lug != -1) ? vectorValue[lug] : empty;
    }
    private boolean existPos(int x, int y) {
        return ((x >= 0) && (x < ROW)) && ((y >= 0) && (y < COL));
    }

    public int getCOL() {
        return COL;
    }
    public int getROW() { return ROW; }

    public void insertTetrimino(Tetrimino  tetrimino) {
        tetrimino.setCanMove(false);
        for (var cord : tetrimino.getCords()) {
            insert(cord.y, cord.x, tetrimino.getColor());
        }

        // Actualiza el valor del tope donde hay piezas fijas
        var cordUp = tetrimino.getCoordMaxBottom();
        if (cordUp < highestRow) highestRow = cordUp;
    }

    public boolean hasCollision( Tetrimino tetrimino ) {
        if (tetrimino.getCoordMaxTop() > ROW - 1) return true;

        for(var cord : tetrimino.getCords()) {
            // Alguna coordenada se sale de la matriz
            if (cord.x < 0 || cord.x > COL - 1) return true;
            // Alguna coordenada sobrepasa a un bloque
            var value = getValue(cord.y, cord.x);
            if (value != empty) return true;
        }

        return false;
    }

    private void destroyLines(int row) {
        for (int j = 0; j < COL; j++) {
            insert(row, j, empty);
        }
        // Mueve todas las filas superiores hacia abajo
        for (int i = row - 1; i >= highestRow; i--) {
            for (int j = 0; j < COL; j++) {
                var color = getValue(i, j);
                if (color != empty) {
                    insert(i + 1, j, color);
                    insert(i, j, empty);
                }
            }
        }
    }

    public int getHighestRow() { return highestRow; }
    public BlockColor getEmpty() { return empty; }

    public Coordinate[] getShadowCoords() { return shadowCoords; }
    public void setShadowCoords(Coordinate[] coords) {
        this.shadowCoords = coords;
    }

    public boolean isShowShadow() { return showShadow; }
    public void setShowShadow(boolean showShadow) { this.showShadow = showShadow; }
}