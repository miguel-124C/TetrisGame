package ui;


import javax.swing.*;

import enums.BlockColor;

import java.awt.*;

import models.Board;
import models.GameState;
import models.Tetrimino;

public class GamePanel extends JPanel {
    private final Board board;
    private final GameState gameState;
    private static final int CELL_SIZE = 30;

    public GamePanel(Board board, GameState gameState) {
        this.board = board;
        this.gameState = gameState;

        setPreferredSize(new Dimension(board.getCOL() * CELL_SIZE, board.getROW() * CELL_SIZE));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        try {
            drawGrid(g2d);
            drawAllPieces(g2d);
            drawCurrentTetrimino(g2d, gameState.getCurrentTetrimino());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void drawGrid(Graphics2D g2d) {
        g2d.setColor(new Color(40, 40, 40));
        // Líneas verticales
        for (int i = 0; i <= board.getCOL(); i++) {
            int x = i * CELL_SIZE;
            g2d.drawLine(x, 0, x, board.getROW() * CELL_SIZE);
        }

        // Líneas horizontales
        for (int i = 0; i <= board.getROW(); i++) {
            int y = i * CELL_SIZE;
            g2d.drawLine(0, y, board.getCOL() * CELL_SIZE, y);
        }
    }

    public void drawAllPieces(Graphics2D g2d) {
        for (int i = board.getMaxHeightValues(); i < board.getROW(); i++) {
            for (int j = 0; j < board.getCOL(); j++) {
                var value = board.getValue(i, j);
                if (value != board.getEmpty()) drawBlock( g2d, j, i, BlockColor.getColor( value ) );
            }
        }
    }

    public void drawCurrentTetrimino(Graphics2D g2d, Tetrimino currentTetrimino) {
        var tetriminoColor = currentTetrimino.getColor();
        for (var coord : currentTetrimino.getCords()) {
            drawBlock(g2d, coord.x, coord.y, BlockColor.getColor(tetriminoColor));   
        }
    }

    public void drawShadow(Graphics2D g2d, int row, int col) {
        int x = row * CELL_SIZE;
        int y = col * CELL_SIZE;
        int arc = 3; // Ratio

        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1, arc, arc);
        g2d.setStroke(new BasicStroke(1));
    }

    private void drawBlock(Graphics2D g2d, int row, int col, Color color) {
        int x = row * CELL_SIZE;
        int y = col * CELL_SIZE;

        GradientPaint gradient = new GradientPaint(
                x, y, color.brighter(),
                x + CELL_SIZE, y + CELL_SIZE, color.darker()
        );
        g2d.setPaint(gradient);
        g2d.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);

        g2d.setColor(color.brighter().brighter());
        g2d.drawRect(x + 1, y + 1, CELL_SIZE - 3, CELL_SIZE - 3);

        g2d.setColor(color.brighter());
        g2d.drawLine(x + 1, y + 1, x + CELL_SIZE - 2, y + 1);
        g2d.drawLine(x + 1, y + 1, x + 1, y + CELL_SIZE - 2);

        g2d.setColor(color.darker().darker());
        g2d.drawLine(x + CELL_SIZE - 2, y + 1, x + CELL_SIZE - 2, y + CELL_SIZE - 2);
        g2d.drawLine(x + 1, y + CELL_SIZE - 2, x + CELL_SIZE - 2, y + CELL_SIZE - 2);
    }

    public void updateMatrix() {
        repaint();
    }
}
