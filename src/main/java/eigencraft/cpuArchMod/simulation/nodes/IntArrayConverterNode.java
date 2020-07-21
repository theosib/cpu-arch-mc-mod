package eigencraft.cpuArchMod.simulation.nodes;

import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.dataObject.DataObjectTypes;
import eigencraft.cpuArchMod.simulation.SimulationIOManager;
import eigencraft.cpuArchMod.simulation.SimulationNode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class IntArrayConverterNode extends SimulationNode {
    public IntArrayConverterNode(BlockPos position) {
        super(position);
    }

    @Override
    public void process(DataObject inMessages, SimulationIOManager ioManager) {
        DataObject dataObject = new DataObject(DataObjectTypes.INT_ARRAY_TYPE);
        if (inMessages.matchType(DataObjectTypes.INT_TYPE)){
            dataObject.setIntArray("data",new int[]{inMessages.getInt("data")});
        } else if (inMessages.matchType(DataObjectTypes.BYTE_TYPE)){
            dataObject.setIntArray("data",new int[]{inMessages.getByte("data")});
        } else if (inMessages.matchType(DataObjectTypes.BOOLEAN_TYPE)){
            dataObject.setIntArray("data",new int[]{inMessages.getByte("bool")});
        } else if (inMessages.matchType(DataObjectTypes.INT_ARRAY_TYPE)){
            publish(inMessages);
            return;
        } else if (inMessages.matchType(DataObjectTypes.BYTE_ARRAY_TYPE)){
            byte[] inData = inMessages.getByteArray("data");
            int[] outData = new int[inData.length];
            for (int i = 0; i < inData.length; i++) {
                outData[i]=inData[i];
            }
            dataObject.setIntArray("data",outData);
        }
        publish(dataObject);
    }

    @Override
    public void onUse(SimulationIOManager ioManager, PlayerEntity player) {

    }


}
