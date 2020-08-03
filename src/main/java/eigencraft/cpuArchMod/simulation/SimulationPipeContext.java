package eigencraft.cpuArchMod.simulation;

import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

public class SimulationPipeContext {
    public SimulationPipeContext setColor(DyeColor color) {
        this.color = color;
        return this;
    }

    public SimulationPipeContext setPosition(BlockPos position) {
        this.position = position;
        return this;
    }

    private DyeColor color;

    public DyeColor getColor() {
        return color;
    }

    public BlockPos getPosition() {
        return position;
    }

    private BlockPos position;
}
