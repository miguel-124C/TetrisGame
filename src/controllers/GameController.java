package controllers;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import enums.Direction;
import models.Board;
import models.GameState;
import ui.*;

public class GameController {
    private final Board board;
    private final GameState gameState;
    private final GamePanel gamePanel;
    private final SidePanel sidePanel;
    private final MusicPlayer musicPlayer;

    public GameController(Board board, GameState gameState, GamePanel gamePanel, SidePanel sidePanel, MusicPlayer musicPlayer) {
        this.board = board;
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.sidePanel = sidePanel;
        this.musicPlayer = musicPlayer;
    }

    public void addEvents( JPanel gamePanelEvent ) {
        gamePanelEvent.addKeyListener(new KeyAdapter()  {
            @Override
            public void keyPressed(KeyEvent e) {
                var currentPiece = gameState.getCurrentTetrimino();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        currentPiece.rotate();
                        break;
                    case KeyEvent.VK_DOWN:
                        currentPiece.move(Direction.DOWN);
                        gameState.setScore(gameState.getScore() + 1);
                        sidePanel.updatePanel();
                        break;
                    case KeyEvent.VK_LEFT:
                        if (board.canMoveX( currentPiece, Direction.LEFT )) {
                            currentPiece.move(Direction.LEFT);
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (board.canMoveX( currentPiece, Direction.RIGHT )) {
                            currentPiece.move(Direction.RIGHT);
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        try {
                            currentPiece.moveToShadow();
                            sidePanel.updatePanel();
                            gameState.changeTetrimino();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                }
                
                gamePanel.updateMatrix();
            }
        });
    }

    public void startGame() {
        musicPlayer.reproduce("assets/tetrisTheme.wav");
        gameState.changeTetrimino();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            sidePanel.updatePanel();
            var currentPiece = gameState.getCurrentTetrimino();
            if (!currentPiece.canMove()) {
                gameState.changeTetrimino();
                currentPiece = gameState.getCurrentTetrimino();
            }
            currentPiece.move(Direction.DOWN);

            gamePanel.updateMatrix();
            checkGameOver(scheduler);
        };

        scheduler.scheduleAtFixedRate(task, 1, 1000, TimeUnit.MILLISECONDS);
    }

    public void checkGameOver(ScheduledExecutorService scheduler) {
        if (board.getMaxHeightValues() == 0) {
            scheduler.shutdown();
            // showGameOverDialog(this);
        }
    }

    public void showGameOverDialog(JFrame parentFrame) {
        musicPlayer.getClip().close();
        var confirmDialog = JOptionPane.showConfirmDialog(
                parentFrame,
                "¡Game Over!, Try again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION
        );

        switch (confirmDialog) {
            case JOptionPane.YES_OPTION:
                // restartGame(parentFrame);
                break;

            case JOptionPane.NO_OPTION:
            case JOptionPane.CLOSED_OPTION:
                parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                parentFrame.dispose();
                break;
        }
    }
}
