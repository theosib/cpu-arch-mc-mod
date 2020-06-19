package eigencraft.cpuArchMod.simulationNode;

import eigencraft.cpuArchMod.CpuArchMod;
import eigencraft.cpuArchMod.backend.dataObject.DataObject;
import eigencraft.cpuArchMod.backend.simulation.SimulationIOManager;
import eigencraft.cpuArchMod.backend.simulation.SimulationNode;
import eigencraft.cpuArchMod.backend.simulation.SimulationWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public class IONode extends SimulationNode {
    public IONode(BlockPos position) {
        super(position);
    }

    @Override
    public void process(DataObject inMessage, SimulationIOManager ioManager) {
        ioManager.addMainTickRunnable(new SimulationIOManager.MinecraftServerRunnable() {
            @Override
            public void run(MinecraftServer server) {
                ItemStack dataObjectItem = new ItemStack(new ItemConvertible() {
                    @Override
                    public Item asItem() {
                        return CpuArchMod.DEBUG_DATA_OBJECT_ITEM;
                    }
                });
                dataObjectItem.putSubTag("dataObject",inMessage.getCompoundTag());
                ItemScatterer.spawn(server.getWorld(DimensionType.OVERWORLD),position.getX(),position.getY()+1,position.getZ(),dataObjectItem);
            }
        });
    }

    @Override
    public void processDirectInput(DataObject inMessage, SimulationIOManager ioManager) {
        publish(inMessage);
    }
}
