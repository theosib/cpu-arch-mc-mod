package eigencraft.cpuArchMod.simulation;

import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.simulation.pipes.TransferPipe;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.function.Function;

public interface SimulationPipe {
    public void connect(SimulationPipe pipe);
    public void removeConnection(SimulationPipe pipe);
    public void publish(PipeMessage dataObject);
    void interPipePublish(PipeMessage message);
    public void tick();
    public BlockPos getPos();

    String getTypeName();

    static HashMap<String, Function<BlockPos,SimulationPipe>> pipeTypeRegistry = new HashMap<>();
    public static void register(String name, Function<BlockPos, SimulationPipe> constructor){
        pipeTypeRegistry.put(name,constructor);
    }
    public static Function<BlockPos,SimulationPipe> getFromName(String name){
        return pipeTypeRegistry.getOrDefault(name, null);
    }
}
