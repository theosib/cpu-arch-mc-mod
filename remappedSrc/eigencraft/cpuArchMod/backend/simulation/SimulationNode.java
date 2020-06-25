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
    private HashSet<DataObject> ownMessagesBuffer1 = new HashSet<>();
    private HashSet<DataObject> ownMessagesBuffer2 = new HashSet<>();

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
            ownMessagesBuffer1.add(dataObject);
            pipe.publish(dataObject);
        }
    }

    public void tick(SimulationIOManager ioManager){
        //Buffer swapping, required to sort out own messages
        ownMessagesBuffer2.clear();
        ownMessagesBuffer2.addAll(ownMessagesBuffer1);
        ownMessagesBuffer1.clear();
        //We need to collect the messages and sort out duplicates, for example if it is in an inner corner of a pipe(connected to two pipes, which are connect to each other)
        HashSet<DataObject> allMessages = new HashSet<>();
        for (SimulationPipe pipe:connectedPipes){
            for (DataObject dataObject:pipe.getNewMessages()) {
                //If not own message
                if (!ownMessagesBuffer2.contains(dataObject)){
                    allMessages.add(dataObject);
                }
            }
        }
        for (DataObject dataObject:allMessages){
            process(dataObject,ioManager);
        }
    }

    public boolean removePipe(SimulationPipe toRemove){
        return connectedPipes.remove(toRemove);
    }
}
