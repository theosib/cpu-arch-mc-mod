package eigencraft.cpuArchMod.simulation.nodes;

import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.dataObject.DataObjectConverter;
import eigencraft.cpuArchMod.dataObject.DataObjectConverterRegistry;
import eigencraft.cpuArchMod.dataObject.DataObjectTypes;
import eigencraft.cpuArchMod.simulation.SimulationIOManager;
import eigencraft.cpuArchMod.simulation.SimulationNode;
import net.minecraft.util.math.BlockPos;

public class ByteConverterNode extends SimulationNode {
    public ByteConverterNode(BlockPos position) {
        super(position);
    }

    @Override
    public void process(DataObject inMessages, SimulationIOManager ioManager) {
        publish(DataObjectConverterRegistry.convert(inMessages, DataObjectTypes.BYTE_TYPE));
    }

    public static class IntToByteConverter extends DataObjectConverter {
        @Override
        public DataObject convert(DataObject in) {
            DataObject o = new DataObject(DataObjectTypes.BYTE_TYPE);
            o.setByte("data", (byte) in.getInt("data"));
            return o;
        }
    }
    public static class IntArrayToByteConverter extends DataObjectConverter {
        @Override
        public DataObject convert(DataObject in) {
            DataObject o = new DataObject(DataObjectTypes.BYTE_TYPE);
            o.setByte("data", (byte) in.getIntArray("data")[0]);
            return o;
        }
    }
    public static class ByteArrayToByteConverter extends DataObjectConverter {
        @Override
        public DataObject convert(DataObject in) {
            DataObject o = new DataObject(DataObjectTypes.BYTE_TYPE);
            o.setByte("data", (byte) in.getByteArray("data")[0]);
            return o;
        }
    }
    public static class BoolToByteConverter extends DataObjectConverter {
        @Override
        public DataObject convert(DataObject in) {
            DataObject o = new DataObject(DataObjectTypes.BYTE_TYPE);
            o.setByte("data", in.getByte("bool"));
            return o;
        }
    }
}
