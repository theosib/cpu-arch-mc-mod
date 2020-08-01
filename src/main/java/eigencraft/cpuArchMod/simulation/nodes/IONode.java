package eigencraft.cpuArchMod.simulation.nodes;

import eigencraft.cpuArchMod.CpuArchMod;
import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.simulation.PipeMessage;
import eigencraft.cpuArchMod.simulation.SimulationIOManager;
import eigencraft.cpuArchMod.simulation.SimulationNode;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;

public class IONode extends SimulationNode {
    public IONode(BlockPos position) {
        super(position);
    }

    @Override
    public void process(PipeMessage inMessage, SimulationIOManager ioManager) {
        ioManager.addMainTickRunnable(new SimulationIOManager.MinecraftServerRunnable() {
            @Override
            public void run(MinecraftServer server) {
                ItemStack dataObjectItem = new ItemStack(new ItemConvertible() {
                    @Override
                    public Item asItem() {
                        return CpuArchMod.DEBUG_DATA_OBJECT_ITEM;
                    }
                });
                dataObjectItem.putSubTag("dataObject",inMessage.getDataObject().getCompoundTag());
                ItemScatterer.spawn(ioManager.getWorld(),position.getX(),position.getY()+1,position.getZ(),dataObjectItem);
            }
        });
    }

    @Override
    public void processDirectInput(PipeMessage inMessage, SimulationIOManager ioManager) {
        publish(inMessage);
    }
}
