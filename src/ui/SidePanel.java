package ui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;

import enums.BlockColor;
import enums.TetriminoType;
import models.GameState;

public class SidePanel extends JPanel {
    private final GameState gameState;
    private JLabel jlabelScore;
    private JLabel jlabelLevel;
    private JLabel jlabelLines;

    public SidePanel(GameState gameState) {
        this.gameState = gameState;
        setPreferredSize(new Dimension(200, 600));
        setBackground(new Color(45, 45, 45));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Título
        JLabel title = new JLabel("TETRIS");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.CYAN);
        title.setAlignmentX(CENTER_ALIGNMENT);
        add(title);

        add(Box.createRigidArea(new Dimension(0, 20)));
        // Panel de puntuación
        this.jlabelScore = new JLabel(String.valueOf(gameState.getScore()), SwingConstants.CENTER);
        add( createInfoPanel("SCORE", this.jlabelScore) );
        add(Box.createRigidArea(new Dimension(0, 15)));
        // Panel de nivel
        this.jlabelLevel = new JLabel(String.valueOf(gameState.getLevel()), SwingConstants.CENTER);
        add(createInfoPanel("LEVEL", this.jlabelLevel));
        add(Box.createRigidArea(new Dimension(0, 15)));
        // Panel de líneas
        this.jlabelLines = new JLabel(String.valueOf(gameState.getCantLines()), SwingConstants.CENTER);
        add(createInfoPanel("LINES", this.jlabelLines));
        add(Box.createRigidArea(new Dimension(0, 30)));
        // Panel siguiente pieza
        add(createNextPiecePanel( gameState ));
        add(Box.createRigidArea(new Dimension(0, 30)));

        // Panel de controles
        add(createControlsPanel());
        add(Box.createVerticalGlue());

        // Botón de salida
        JButton exitButton = new JButton("EXIT");
        exitButton.setAlignmentX(CENTER_ALIGNMENT);
        exitButton.setBackground(new Color(200, 50, 50));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        add(exitButton);
    }

    public void updatePanel() {
        var score = String.valueOf(gameState.getScore());
        var level = String.valueOf(gameState.getLevel());
        var lines = String.valueOf(gameState.getCantLines());

        jlabelScore.setText( score );
        jlabelLevel.setText( level );
        jlabelLines.setText( lines );
        repaint();
    }
    private JPanel createInfoPanel(String title, JLabel label) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(60, 60, 60));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));

        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.YELLOW);
        label.setBorder(BorderFactory.createEmptyBorder(2, 10, 5, 10));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createNextPiecePanel( GameState gameState ) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                var nextPiece = gameState.getNextTetrimino();
                var cordsNextPiece = TetriminoType.getCordsByType(nextPiece.getTetriminoType());
                for (var coord : cordsNextPiece) {
                    coord.x -= 3;
                }

                int cellSize = 25;
                int startX = (getWidth() - 3 * cellSize) / 2;
                int startY = 20;

                assert cordsNextPiece != null;
                for (var coord : cordsNextPiece) {
                    int x = startX + coord.x * cellSize;
                    int y = startY + coord.y * cellSize;

                    // DrawBlock
                    var colorTetrimino = BlockColor.getColor(nextPiece.getColor());
                    g2d.setColor(colorTetrimino);
                    g2d.fillRect(x + 1, y + 1, cellSize - 2, cellSize - 2);

                    // Draw border
                    g2d.setColor(colorTetrimino.brighter());
                    g2d.drawRect(x + 1, y + 1, cellSize - 3, cellSize - 3);   
                }
            }
        };

        panel.setPreferredSize(new Dimension(180, 100));
        panel.setBackground(new Color(35, 35, 35));

        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.CYAN, 2),
                "NEXT PIECE"
        );
        border.setTitleColor(Color.WHITE);
        border.setTitleFont(new Font("Arial", Font.BOLD, 14));
        border.setTitleJustification(TitledBorder.CENTER);
        panel.setBorder(border);

        return panel;
    }

    private JPanel createControlsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 5, 5));
        panel.setBackground(new Color(45, 45, 45));

        String[][] controls = {
                {"← →", "MOVE LEFT/RIGHT"},
                {"↓", "MOVE DOWN"},
                {"↑", "ROTATE"},
                {"SPACE", "DROP"}
        };

        for (String[] control : controls) {
            final JPanel controlPanel = getjPanel(control);
            panel.add(controlPanel);
        }

        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "CONTROLS"
        );
        border.setTitleColor(Color.WHITE);
        border.setTitleFont(new Font("Arial", Font.BOLD, 12));
        panel.setBorder(border);

        return panel;
    }

    private static JPanel getjPanel(String[] control) {
        JPanel controlPanel = new JPanel(new BorderLayout(10, 0));
        controlPanel.setBackground(new Color(60, 60, 60));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel keyLabel = new JLabel(control[0]);
        keyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        keyLabel.setForeground(Color.GREEN);

        JLabel descLabel = new JLabel(control[1]);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(Color.LIGHT_GRAY);

        controlPanel.add(keyLabel, BorderLayout.WEST);
        controlPanel.add(descLabel, BorderLayout.CENTER);
        return controlPanel;
    }
}