package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import enums.TetriminoType;

public class GameState {
    private Tetrimino currentTetrimino;
    private Tetrimino nextTetrimino;
    private int score = 0;
    private  int level = 1;
    private int cantLines = 0;

    private List<TetriminoType> bagTypes = new ArrayList<>();

    public void initialize() {
        fillBagTypes();

        var typeRandom = getTypeRandom();
        currentTetrimino = Tetrimino.create(typeRandom);
        typeRandom = getTypeRandom();
        nextTetrimino = Tetrimino.create(typeRandom);
    }

    public void changeTetrimino() {
        var typeRandom = getTypeRandom();

        currentTetrimino = nextTetrimino;
        nextTetrimino = Tetrimino.create(typeRandom);
    }

    public void addLines( int cantLines ) {
        this.cantLines += cantLines;
    }

    public double getTimeDrop() {
        return Math.pow((0.8 - ( (level - 1) * 0.007 )), (level - 1));
    }

    private void fillBagTypes() {
        bagTypes.add(TetriminoType.I);
        bagTypes.add(TetriminoType.J);
        bagTypes.add(TetriminoType.L);
        bagTypes.add(TetriminoType.O);
        bagTypes.add(TetriminoType.S);
        bagTypes.add(TetriminoType.T);
        bagTypes.add(TetriminoType.Z);
    }

    private TetriminoType getTypeRandom() {
        if (bagTypes.isEmpty()) fillBagTypes();

        Random random = new Random();

        var index = random.nextInt(bagTypes.size());
        var typeRandom = bagTypes.get(index);

        bagTypes.remove(index);

        return typeRandom;
    }

    public void addScore( int cantScore ) {
        score += cantScore;
    }

    public int getCantLines() { return cantLines; }

    public Tetrimino getCurrentTetrimino() { return currentTetrimino; }
    public Tetrimino getNextTetrimino() { return nextTetrimino; }
    public int getScore() { return score; }

    public void addLevel() {
        if (level >= 15) level = 15;
        level += 1;
    }
    public int getLevel() { return level; }
}