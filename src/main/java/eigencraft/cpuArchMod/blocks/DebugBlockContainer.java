package eigencraft.cpuArchMod.blocks;

import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.entity.player.PlayerEntity;

public class DebugBlockContainer extends Container {
    protected DebugBlockContainer(int syncId) {
        super(ContainerType<>,syncId);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
