package enums;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public enum BlockColor {
    BLACK, CYAN, BLUE, ORANGE, YELLOW, GREEN, PURPLE, RED;

    public static BlockColor random() {
        var types = Arrays.stream(values())
                .filter(color -> color != BlockColor.BLACK)
                .toArray(BlockColor[]::new);
        int index = ThreadLocalRandom.current().nextInt(types.length);
        return types[index];
    }

    public static Color getColor(BlockColor color) {
        if (color == BlockColor.BLACK)
            return Color.BLACK;
        if (color == BlockColor.YELLOW)
            return Color.YELLOW;
        if (color == BlockColor.GREEN)
            return Color.GREEN;
        if (color == BlockColor.BLUE)
            return Color.BLUE;
        if (color == BlockColor.CYAN)
            return Color.CYAN;
        if (color == BlockColor.RED)
            return Color.RED;
        if (color == BlockColor.PURPLE)
            return Color.MAGENTA;
        if (color == BlockColor.ORANGE)
            return Color.ORANGE;

        return Color.BLACK;
    }
}
