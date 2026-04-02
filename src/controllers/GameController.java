package controllers;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import enums.Direction;
import helpers.ActionHelper;
import models.*;
import ui.*;

public class GameController {
    private final Board board;
    private final GameState gameState;
    private final GamePanel gamePanel;
    private final SidePanel sidePanel;
    private final MusicPlayer musicPlayer;
    private final Runnable onGameOver;
    private final Timer timer;

    private Tetrimino currentTetrimino;

    private int cantDrop;

    public GameController(
        Board board, GameState gameState, GamePanel gamePanel,
        SidePanel sidePanel, MusicPlayer musicPlayer,
        Runnable onGameOver
    ) {
        this.board = board;
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.sidePanel = sidePanel;
        this.musicPlayer = musicPlayer;
        this.onGameOver = onGameOver;
        this.timer = new Timer(1000, e -> tick());

        gameState.initialize();
        this.currentTetrimino = gameState.getCurrentTetrimino();
    }

    public void registerKeyBindings( JPanel mainPanel ) {
        // Usamos WHEN_IN_FOCUSED_WINDOW para que funcione aunque el foco no esté exactamente ahí
        InputMap im = mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = mainPanel.getActionMap();

        // 2. Vincular la tecla con un ID (un String cualquiera)
        im.put(KeyStroke.getKeyStroke("UP"), "rotate");
        im.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        im.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        im.put(KeyStroke.getKeyStroke("SPACE"), "drop");

        // 3. Vincular el ID con la acción lógica
        am.put("rotate", ActionHelper.create(this::tryRotate) );
        am.put("moveDown", ActionHelper.create(this::softDrop) );
        am.put("moveLeft", ActionHelper.create(this::moveLeft) );
        am.put("moveRight", ActionHelper.create(this::moveRight) );
        am.put("drop", ActionHelper.create(this::hardDrop) );
    }

    public void start() {
        musicPlayer.reproduce("assets/tetrisTheme.wav", true);
        timer.start();
    }

    public void tick() {
        currentTetrimino.move(Direction.DOWN);

        if (board.hasCollision(currentTetrimino)) {
            currentTetrimino.move(Direction.UP);
            curretnTetriminoFinish();
        } else {
            updatedShadow();
        }
        checkGameOver();
    }

    public Coordinate[] getShadowCoords() {
        var shadow = new Tetrimino(this.currentTetrimino);
        cantDrop = 0;

        while (!board.hasCollision(shadow)) {
            shadow.move(Direction.DOWN);
            cantDrop += 1;
        }

        board.setShowShadow(cantDrop >= 2);
            
        shadow.move(Direction.UP);
        return shadow.getCords();
    }

    private void tryRotate() {
        if (!currentTetrimino.isCanRotate()) return;

        var rotated = new Tetrimino(this.currentTetrimino);
        rotated.rotate();

        if (!board.hasCollision(rotated)) {
            this.currentTetrimino.rotate();
            updatedShadow();
            musicPlayer.reproduce("assets/soundEffects/rotate_piece.wav", false);
        }
    }

    public void softDrop() {
        currentTetrimino.move(Direction.DOWN);

        if (board.hasCollision(currentTetrimino)) {
            currentTetrimino.move(Direction.UP);
            curretnTetriminoFinish();
        } else {
            updatedShadow();
            musicPlayer.reproduce("assets/soundEffects/move_piece.wav", false);
            scoreUpdated(1);
        }
    }

    public void moveLeft() {
        if (this.canMove( Direction.LEFT )) {
            currentTetrimino.move(Direction.LEFT);
            updatedShadow();
            musicPlayer.reproduce("assets/soundEffects/move_piece.wav", false);
        }
    }

    public void moveRight() {
        if (this.canMove( Direction.RIGHT )) {
            currentTetrimino.move(Direction.RIGHT);
            updatedShadow();
            musicPlayer.reproduce("assets/soundEffects/move_piece.wav", false);
        }
    }

    public void hardDrop() {
        var shadowCoords = getShadowCoords();
        currentTetrimino.moveToShadow(shadowCoords);

        scoreUpdated(this.cantDrop * 2);
        curretnTetriminoFinish();
        musicPlayer.reproduce("assets/soundEffects/piece_landed.wav", false);
    }

    private void curretnTetriminoFinish() {
        board.insertTetrimino(currentTetrimino);
        board.setShowShadow(false);
        board.setShadowCoords(null);

        var numberOfLines = board.getCantLines();
        gameState.addLines(numberOfLines);
        calculateScore(numberOfLines);
        if (gameState.getCantLines() >= gameState.getLevel() * 10 ) {
            levelUp();
        }

        gameState.changeTetrimino();
        currentTetrimino = gameState.getCurrentTetrimino();

        sidePanel.updatePanel();
        gamePanel.repaint();
    }

    private void levelUp() {
        gameState.addLevel();
        musicPlayer.reproduce("assets/soundEffects/level_up.wav", false);

        var timeDrop = gameState.getTimeDrop();
        int timeDropMs = (int) (timeDrop * 1000);
        timer.setDelay(timeDropMs);
        // gamePanel.showLevelUP(null, 1);
    }

    private void calculateScore( int numberOfLines ) {
        if (numberOfLines == 1) {
            scoreUpdated(100 * gameState.getLevel());
        } else if (numberOfLines == 2) {
            scoreUpdated(300 * gameState.getLevel());
        } else if (numberOfLines == 3) {
            scoreUpdated(500 * gameState.getLevel());
        } else if (numberOfLines == 4) {
            scoreUpdated(1200 * gameState.getLevel());
        }
    }

    private boolean canMove( Direction direction ) {
        var test = new Tetrimino(this.currentTetrimino);
        test.move(direction);

        return !this.board.hasCollision(test);
    }

    private void scoreUpdated(int cantScore) {
        gameState.addScore(cantScore);
        sidePanel.updatePanel();
    }

    public void checkGameOver() {
        if (board.getHighestRow() == 0) {
            timer.stop();
            onGameOver.run();
        }
    }

    private void updatedShadow() {
        var shadowCoords = getShadowCoords();
        board.setShadowCoords(shadowCoords);
        gamePanel.repaint();
    }
}