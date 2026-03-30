package models;

public class GameState {
    private Tetrimino currentTetrimino;
    private Tetrimino nextTetrimino;
    private int score = 0;
    private  int level = 1;
    private int cantLines = 0;

    public void initialize() {
        currentTetrimino = Tetrimino.create();
        nextTetrimino = Tetrimino.create();
    }

    public void changeTetrimino() {
        currentTetrimino = (nextTetrimino != null) ? nextTetrimino : Tetrimino.create();
        nextTetrimino = Tetrimino.create();
    }

    public void addLines( int cantLines ) {
        this.cantLines += cantLines;
    }

    public int getLines() { return cantLines; }

    public Tetrimino getCurrentTetrimino() { return currentTetrimino; }
    public Tetrimino getNextTetrimino() { return nextTetrimino; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
}