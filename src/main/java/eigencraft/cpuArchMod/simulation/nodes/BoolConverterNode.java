package eigencraft.cpuArchMod.simulation.nodes;

import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.dataObject.DataObjectTypes;
import eigencraft.cpuArchMod.simulation.SimulationIOManager;
import eigencraft.cpuArchMod.simulation.SimulationNode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class BoolConverterNode extends SimulationNode {
    public BoolConverterNode(BlockPos position) {
        super(position);
    }

    @Override
    public void process(DataObject inMessages, SimulationIOManager ioManager) {
        if (inMessages.matchType(DataObjectTypes.INT_TYPE)){
            if (inMessages.getInt("data")==0){
                publish(DataObjectTypes.FALSE_BOOLEAN);
            } else {
                publish(DataObjectTypes.TRUE_BOOLEAN);
            }
        } else if (inMessages.matchType(DataObjectTypes.BYTE_TYPE)){
            if (inMessages.getByte("data")==0){
                publish(DataObjectTypes.FALSE_BOOLEAN);
            } else {
                publish(DataObjectTypes.TRUE_BOOLEAN);
            }
        } else if (inMessages.matchType(DataObjectTypes.BOOLEAN_TYPE)){
            publish(inMessages);
            return;
        } else if (inMessages.matchType(DataObjectTypes.INT_ARRAY_TYPE)){
            if (inMessages.getIntArray("data")[0]==0){
                publish(DataObjectTypes.FALSE_BOOLEAN);
            } else {
                publish(DataObjectTypes.TRUE_BOOLEAN);
            }
        } else if (inMessages.matchType(DataObjectTypes.BYTE_ARRAY_TYPE)){
            if (inMessages.getByteArray("data")[0]==0){
                publish(DataObjectTypes.FALSE_BOOLEAN);
            } else {
                publish(DataObjectTypes.TRUE_BOOLEAN);
            }
        }
    }

    @Override
    public void onUse(SimulationIOManager ioManager, PlayerEntity player) {

    }


}
