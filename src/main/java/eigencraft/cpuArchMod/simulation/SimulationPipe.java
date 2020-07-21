package eigencraft.cpuArchMod.simulation;

import eigencraft.cpuArchMod.dataObject.DataObject;
import net.minecraft.util.math.BlockPos;

public interface SimulationPipe {
    public void connect(SimulationPipe pipe);
    public void removeConnection(SimulationPipe pipe);
    public void publish(DataObject dataObject);
    void interPipePublish(DataObject dataObject, SimulationTransferPipe src);
    public void tick();
    public BlockPos getPos();

    String getTypeName();
}
