package eigencraft.cpuArchMod.backend.simulation;

import eigencraft.cpuArchMod.backend.dataObject.DataObject;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

public abstract class SimulationNode{

    private static HashMap<String, Function<BlockPos,SimulationNode>> nodeTypeRegistry = new HashMap<>();

    HashSet<SimulationPipe> connectedPipes = new HashSet<>();
    protected BlockPos position;

    public SimulationNode(BlockPos position){
        this.position = position;
    }

    public static void register(Class type,Function<BlockPos,SimulationNode> constructor){
        nodeTypeRegistry.put(type.getSimpleName(),constructor);
    }

    public static Function<BlockPos,SimulationNode> getFromName(String name){
        return (nodeTypeRegistry.containsKey(name))?nodeTypeRegistry.get(name):null;
    }

    public abstract void process(DataObject inMessages,SimulationIOManager ioManager);

    public void processDirectInput(DataObject inMessage, SimulationIOManager ioManager){
        this.process(inMessage,ioManager);
    }

    public void addPipe(SimulationPipe pipe){
        connectedPipes.add(pipe);
    }

    public void publish(DataObject dataObject){
        for (SimulationPipe pipe:connectedPipes){
            pipe.publish(dataObject);
        }
    }

    public void tick(SimulationIOManager ioManager){
        for (SimulationPipe pipe:connectedPipes){
            for (DataObject dataObject:pipe.getNewMessages()) {
                this.process(dataObject, ioManager);
            }
        }
    }

    public boolean removePipe(SimulationPipe toRemove){
        return connectedPipes.remove(toRemove);
    }
}
