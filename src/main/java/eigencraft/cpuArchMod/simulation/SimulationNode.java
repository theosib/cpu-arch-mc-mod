package eigencraft.cpuArchMod.simulation;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

public abstract class SimulationNode{

    private static final HashMap<String, Function<BlockPos,SimulationNode>> nodeTypeRegistry = new HashMap<>();

    HashSet<SimulationPipe> connectedPipes = new HashSet<>();
    public BlockPos position;
    private final HashSet<PipeMessage> ownMessagesBuffer1 = new HashSet<>();
    private final HashSet<PipeMessage> ownMessagesBuffer2 = new HashSet<>();

    public SimulationNode(BlockPos position){
        this.position = position;
    }

    public static void register(String type,Function<BlockPos,SimulationNode> constructor){
        nodeTypeRegistry.put(type,constructor);
    }

    public static Function<BlockPos,SimulationNode> getFromName(String name){
        return nodeTypeRegistry.getOrDefault(name, null);
    }

    public abstract void process(PipeMessage message,SimulationIOManager ioManager);

    public void processDirectInput(PipeMessage inMessage, SimulationIOManager ioManager){
        this.process(inMessage,ioManager);
    }

    public void addPipe(SimulationPipe pipe){
        connectedPipes.add(pipe);
    }

    public void publish(PipeMessage message){
        ownMessagesBuffer1.add(message);
        for (SimulationPipe pipe:connectedPipes){
            pipe.publish(message);
        }
    }

    public void tick(SimulationIOManager ioManager){
        //Buffer swapping, required to sort out own messages
        ownMessagesBuffer2.clear();
        ownMessagesBuffer2.addAll(ownMessagesBuffer1);
        ownMessagesBuffer1.clear();
        //We need to collect the messages and sort out duplicates, for example if it is in an inner corner of a pipe(connected to two pipes, which are connect to each other)
        HashSet<PipeMessage> allMessages = new HashSet<>();
        for (SimulationPipe pipe:connectedPipes){
            if (pipe instanceof SimulationMessageProvidingPipe){
                for (Object message : ((SimulationMessageProvidingPipe) pipe).getNewMessages()) {
                    allMessages.add((PipeMessage) message);
                }
            }
        }
        for (PipeMessage message:allMessages){
            process(message,ioManager);
        }
    }

    public void removePipe(SimulationPipe toRemove){
        connectedPipes.remove(toRemove);
    }
    public void onUse(SimulationIOManager ioManager, PlayerEntity player){};
}
