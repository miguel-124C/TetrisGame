import javax.swing.SwingUtilities;

import ui.TetrisFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TetrisFrame frame = new TetrisFrame();
            frame.setVisible(true);
        });
    }
}
