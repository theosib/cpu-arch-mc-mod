package eigencraft.cpuArchMod.simulation.nodes;

import eigencraft.cpuArchMod.dataObject.DataObject;

import eigencraft.cpuArchMod.dataObject.DataObjectTypes;
import eigencraft.cpuArchMod.simulation.SimulationIOManager;
import eigencraft.cpuArchMod.simulation.SimulationNode;
import net.minecraft.util.math.BlockPos;

public class IntConverterNode extends SimulationNode {
    public IntConverterNode(BlockPos position) {
        super(position);
    }

    @Override
    public void process(DataObject inMessages, SimulationIOManager ioManager) {
        DataObject dataObject = new DataObject(DataObjectTypes.INT_TYPE);
        if (inMessages.matchType(DataObjectTypes.INT_TYPE)){
            publish(inMessages);
            return;
        } else if (inMessages.matchType(DataObjectTypes.BYTE_TYPE)){
            dataObject.setInt("data",inMessages.getByte("data"));
        } else if (inMessages.matchType(DataObjectTypes.BOOLEAN_TYPE)){
            dataObject.setInt("data",inMessages.getByte("bool"));
        } else if (inMessages.matchType(DataObjectTypes.INT_ARRAY_TYPE)){
            dataObject.setInt("data",inMessages.getIntArray("data")[0]);
        } else if (inMessages.matchType(DataObjectTypes.BYTE_ARRAY_TYPE)){
            dataObject.setInt("data",inMessages.getByteArray("data")[0]);
        }
        publish(dataObject);
    }
}
