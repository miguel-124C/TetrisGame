package models;

public class GameState {
    private Tetrimino currentTetrimino;
    private Tetrimino nextTetrimino;
    private int score = 0;
    private  int level = 1;
    private int cantLines = 0;
    private float velocity = 1.5f;
    private float limitVelocity = 0.3f;

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
        levelUp();
    }

    private void levelUp() {
        var levels = this.level * 10;
        if (levels > this.cantLines) return;
                        
        this.level += 1;
        if (velocity > limitVelocity) {
            velocity -= 0.1;
        }
    }

    public int getLines() { return cantLines; }

    public Tetrimino getCurrentTetrimino() { return currentTetrimino; }
    public Tetrimino getNextTetrimino() { return nextTetrimino; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getLevel() { return level; }
}