package controllers;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import enums.Direction;
import helpers.ActionHelper;
import models.Board;
import models.Coordinate;
import models.GameState;
import models.Tetrimino;
import ui.*;

// start() crea el Timer y lo arranca.

// Acciones de teclado llaman a métodos del controller que:
// Intentan mover/rotar sobre copia y validan con board.
// Actualizan score/lines si corresponde y piden repintado.

public class GameController {
    private final Board board;
    private final GameState gameState;
    private final GamePanel gamePanel;
    private final SidePanel sidePanel;
    private final MusicPlayer musicPlayer;
    private final Runnable onGameOver;
    private final Timer timer;

    private Tetrimino currentTetrimino;

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
        am.put("moveDown", ActionHelper.create(this::moveDown) );
        am.put("moveLeft", ActionHelper.create(this::moveLeft) );
        am.put("moveRight", ActionHelper.create(this::moveRight) );
        am.put("drop", ActionHelper.create(this::drop) );
    }

    public void start() {
        musicPlayer.reproduce("assets/tetrisTheme.wav");
        timer.start();
    }

    public void tick() {
        currentTetrimino.move(Direction.DOWN);

        if (board.hasCollision(currentTetrimino)) {
            currentTetrimino.move(Direction.UP);
            board.insertTetrimino(currentTetrimino);
            board.setShowShadow(false);
            board.setShadowCoords(null);

            var countLines = board.getCantLines();
            gameState.addLines(countLines);
            gameState.changeTetrimino();
            currentTetrimino = gameState.getCurrentTetrimino();

            sidePanel.updatePanel();
        } else {
            updatedShadow();
        }
        
        gamePanel.repaint();
        checkGameOver();
    }

    public Coordinate[] getShadowCoords() {
        var shadow = new Tetrimino(this.currentTetrimino);
        var cantDrop = 0;

        while (!board.hasCollision(shadow)) {
            shadow.move(Direction.DOWN);
            cantDrop += 1;
        }

        board.setShowShadow(cantDrop >= 2);
            
        shadow.move(Direction.UP);
        return shadow.getCords();
    }

    private void tryRotate() {
        var rotated = new Tetrimino(this.currentTetrimino);
        rotated.rotate();

        if (!board.hasCollision(rotated)) {
            this.currentTetrimino.rotate();
            updatedShadow();
            gamePanel.repaint();
        }
    }

    public void moveDown() {
        currentTetrimino.move(Direction.DOWN);

        if (board.hasCollision(currentTetrimino)) {
            currentTetrimino.move(Direction.UP);
            board.insertTetrimino(currentTetrimino);
            board.setShowShadow(false);
            board.setShadowCoords(null);

            var countLines = board.getCantLines();
            gameState.addLines(countLines);
            gameState.changeTetrimino();
            currentTetrimino = gameState.getCurrentTetrimino();

            gameState.setScore(gameState.getScore() + 1);
            sidePanel.updatePanel();
        } else {
            updatedShadow();
        }

        gamePanel.repaint();
    }

    public void moveLeft() {
        if (board.canMoveX( currentTetrimino, Direction.LEFT )) {
            currentTetrimino.move(Direction.LEFT);
            updatedShadow();

            gamePanel.repaint();
        }
    }

    public void moveRight() {
        if (board.canMoveX( currentTetrimino, Direction.RIGHT )) {
            currentTetrimino.move(Direction.RIGHT);
            updatedShadow();

            gamePanel.repaint();
        }
    }

    public void drop() {
        var shadowCoords = getShadowCoords();
        currentTetrimino.moveToShadow(shadowCoords);


        board.insertTetrimino(currentTetrimino);
        board.setShowShadow(false);
        board.setShadowCoords(null);

        var countLines = board.getCantLines();
        gameState.addLines(countLines);
        gameState.changeTetrimino();
        currentTetrimino = gameState.getCurrentTetrimino();
        gameState.setScore(gameState.getScore() + 1);
        
        sidePanel.updatePanel();
        gamePanel.repaint();
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
    }
}
