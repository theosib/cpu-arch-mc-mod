package eigencraft.cpuArchMod.simulation;

import net.minecraft.block.MaterialColor;
import net.minecraft.util.DyeColor;

public enum PipeLane {
    ALL,
    DIRECT_INPUT,
    WHITE,
    ORANGE,
    MAGENTA,
    LIGHT_BLUE,
    YELLOW,
    LIME,
    PINK,
    GRAY,
    LIGHT_GRAY,
    CYAN,
    PURPLE,
    BLUE,
    BROWN,
    GREEN,
    RED,
    BLACK;
    public static PipeLane convert(DyeColor dyeColor){
        switch (dyeColor) {
            case WHITE: return WHITE;
            case ORANGE: return ORANGE;
            case MAGENTA: return MAGENTA;
            case LIGHT_BLUE: return LIGHT_BLUE;
            case YELLOW: return YELLOW;
            case LIME: return LIME;
            case PINK: return PINK;
            case GRAY: return GRAY;
            case LIGHT_GRAY: return LIGHT_GRAY;
            case CYAN: return CYAN;
            case PURPLE: return PURPLE;
            case BLUE: return BLUE;
            case BROWN: return BROWN;
            case GREEN: return GREEN;
            case RED: return RED;
            case BLACK: return BLACK;
        }
        return null;
    }
}
