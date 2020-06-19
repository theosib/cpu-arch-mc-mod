package eigencraft.cpuArchMod.backend.simulation;

import eigencraft.cpuArchMod.backend.dataObject.DataObject;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class SimulationNode {
    private HashSet<SimulationPipe> connectedPipes = new HashSet<>();
    protected BlockPos position;

    public SimulationNode(BlockPos position){
        this.position = position;
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

    public boolean removePipeConnection(SimulationPipe toRemove){
        return connectedPipes.remove(toRemove);
    }
}
