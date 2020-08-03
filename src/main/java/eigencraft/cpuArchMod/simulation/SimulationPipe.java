package eigencraft.cpuArchMod.simulation;

import java.util.HashMap;
import java.util.function.Function;

public interface SimulationPipe {
    public void connect(SimulationPipe pipe);
    public void removeConnection(SimulationPipe pipe);
    public void publish(PipeMessage dataObject);
    void interPipePublish(PipeMessage message);
    public void tick();
    public SimulationPipeContext getContext();

    String getTypeName();

    static HashMap<String, Function<SimulationPipeContext,SimulationPipe>> pipeTypeRegistry = new HashMap<>();
    public static void register(String name, Function<SimulationPipeContext, SimulationPipe> constructor){
        pipeTypeRegistry.put(name,constructor);
    }
    public static Function<SimulationPipeContext,SimulationPipe> getFromName(String name){
        return pipeTypeRegistry.getOrDefault(name, null);
    }
}
