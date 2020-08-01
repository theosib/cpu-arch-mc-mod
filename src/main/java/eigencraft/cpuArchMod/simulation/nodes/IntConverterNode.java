package eigencraft.cpuArchMod.simulation.nodes;

import eigencraft.cpuArchMod.dataObject.DataObject;

import eigencraft.cpuArchMod.dataObject.DataObjectConverter;
import eigencraft.cpuArchMod.dataObject.DataObjectConverterRegistry;
import eigencraft.cpuArchMod.dataObject.DataObjectTypes;
import eigencraft.cpuArchMod.simulation.PipeMessage;
import eigencraft.cpuArchMod.simulation.SimulationIOManager;
import eigencraft.cpuArchMod.simulation.SimulationNode;
import net.minecraft.util.math.BlockPos;

public class IntConverterNode extends SimulationNode {
    public IntConverterNode(BlockPos position) {
        super(position);
    }

    @Override
    public void process(PipeMessage inMessages, SimulationIOManager ioManager) {
        publish(new PipeMessage(DataObjectConverterRegistry.convert(inMessages.getDataObject(), DataObjectTypes.INT_TYPE),inMessages.getLane()));
    }

    public static class ByteToIntConverter extends DataObjectConverter{
        @Override
        public DataObject convert(DataObject in) {
            DataObject o = new DataObject(DataObjectTypes.INT_TYPE);
            o.setInt("data",in.getByte("data"));
            return o;
        }
    }
    public static class IntArrayToIntConverter extends DataObjectConverter{
        @Override
        public DataObject convert(DataObject in) {
            DataObject o = new DataObject(DataObjectTypes.INT_TYPE);
            o.setInt("data",in.getIntArray("data")[0]);
            return o;
        }
    }
    public static class ByteArrayToIntConverter extends DataObjectConverter{
        @Override
        public DataObject convert(DataObject in) {
            DataObject o = new DataObject(DataObjectTypes.INT_TYPE);
            o.setInt("data",in.getByteArray("data")[0]);
            return o;
        }
    }
    public static class BoolToIntConverter extends DataObjectConverter{
        @Override
        public DataObject convert(DataObject in) {
            DataObject o = new DataObject(DataObjectTypes.INT_TYPE);
            o.setInt("data",in.getByte("bool"));
            return o;
        }
    }
}
