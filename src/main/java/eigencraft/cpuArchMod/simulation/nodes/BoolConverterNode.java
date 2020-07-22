package eigencraft.cpuArchMod.simulation.nodes;

import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.dataObject.DataObjectConverter;
import eigencraft.cpuArchMod.dataObject.DataObjectConverterRegistry;
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
        publish(DataObjectConverterRegistry.convert(inMessages, DataObjectTypes.BOOLEAN_TYPE));
    }

    public static class ByteToBoolConverter extends DataObjectConverter {
        @Override
        public DataObject convert(DataObject in) {
            return (in.getByte("data")==0)?DataObjectTypes.FALSE_BOOLEAN:DataObjectTypes.TRUE_BOOLEAN;
        }
    }
    public static class IntToBoolConverter extends DataObjectConverter {
        @Override
        public DataObject convert(DataObject in) {
            return (in.getInt("data")==0)?DataObjectTypes.FALSE_BOOLEAN:DataObjectTypes.TRUE_BOOLEAN;
        }
    }
    public static class ByteArrayToBoolConverter extends DataObjectConverter {
        @Override
        public DataObject convert(DataObject in) {
            return (in.getByteArray("data")[0]==0)?DataObjectTypes.FALSE_BOOLEAN:DataObjectTypes.TRUE_BOOLEAN;
        }
    }
    public static class IntArrayToBoolConverter extends DataObjectConverter {
        @Override
        public DataObject convert(DataObject in) {
            return (in.getIntArray("data")[0]==0)?DataObjectTypes.FALSE_BOOLEAN:DataObjectTypes.TRUE_BOOLEAN;
        }
    }

    @Override
    public void onUse(SimulationIOManager ioManager, PlayerEntity player) {

    }


}
