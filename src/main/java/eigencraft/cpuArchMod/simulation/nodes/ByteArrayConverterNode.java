package eigencraft.cpuArchMod.simulation.nodes;

import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.dataObject.DataObjectTypes;
import eigencraft.cpuArchMod.simulation.SimulationIOManager;
import eigencraft.cpuArchMod.simulation.SimulationNode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class ByteArrayConverterNode extends SimulationNode {
    public ByteArrayConverterNode(BlockPos position) {
        super(position);
    }

    @Override
    public void process(DataObject inMessages, SimulationIOManager ioManager) {
        DataObject dataObject = new DataObject(DataObjectTypes.BYTE_ARRAY_TYPE);
        if (inMessages.matchType(DataObjectTypes.INT_TYPE)){
            dataObject.setByteArray("data",new byte[]{(byte)inMessages.getInt("data")});
        } else if (inMessages.matchType(DataObjectTypes.BYTE_TYPE)){
            dataObject.setByteArray("data",new byte[]{inMessages.getByte("data")});
        } else if (inMessages.matchType(DataObjectTypes.BOOLEAN_TYPE)){
            dataObject.setIntArray("data",new int[]{inMessages.getByte("bool")});
        } else if (inMessages.matchType(DataObjectTypes.INT_ARRAY_TYPE)){
            int[] inData = inMessages.getIntArray("data");
            byte[] outData = new byte[inData.length];
            for (int i = 0; i < inData.length; i++) {
                outData[i]=(byte)inData[i];
            }
            dataObject.setByteArray("data",outData);
        } else if (inMessages.matchType(DataObjectTypes.BYTE_ARRAY_TYPE)){
            publish(inMessages);
            return;
        }
        publish(dataObject);
    }
}
