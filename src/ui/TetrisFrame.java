package ui;

import javax.swing.*;

import controllers.GameController;

import java.awt.*;
import java.awt.event.*;

import enums.BlockColor;
import models.Board;
import models.GameState;

public class TetrisFrame extends JFrame {
    private final GameController gameController;
    private final Board board = new Board(20, 10, BlockColor.BLACK);
    private final GameState gameState = new GameState();
    private JPanel mainPanel;
    private final GamePanel gamePanel;
    private final SidePanel sidePanel;

    public TetrisFrame() {
        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Crear el panel principal
        gamePanel = new GamePanel(board, gameState);
        // Crear panel lateral con información
        sidePanel = new SidePanel(gameState);

        gameController = new GameController(board, gameState, gamePanel, sidePanel, new MusicPlayer());

        // Usar BorderLayout para organizar
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(30, 30, 30));

        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.EAST);

        gameController.addEvents(mainPanel);
        // Asegurar que el panel tenga foco
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                mainPanel.requestFocusInWindow();
            }
        });

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);

        gameController.startGame();
    }
}
