package eigencraft.cpuArchMod.simulation.pipes;

import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.simulation.PipeMessage;
import eigencraft.cpuArchMod.simulation.SimulationMessageProvidingPipe;
import eigencraft.cpuArchMod.simulation.SimulationPipe;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.HashSet;

public class EndPipe implements SimulationMessageProvidingPipe {

    protected HashSet<PipeMessage> messages = new HashSet<>();
    private HashSet<PipeMessage> distributionMessages = new HashSet<>();
    BlockPos position;

    public EndPipe(BlockPos position) {
        this.position = position;
    }

    @Override
    public void interPipePublish(PipeMessage dataObject) {
        //Only store it, but don't redistribute it.
        messages.add(dataObject);
    }

    @Override
    public void connect(SimulationPipe pipe) {
        //pipe ends have no outgoing stuff
    }

    @Override
    public void removeConnection(SimulationPipe pipe) {
        //pipe ends have no outgoing stuff
    }

    @Override
    public void publish(PipeMessage dataObject) {
        //Only store it, but don't redistribute it.
        //messages.add(dataObject);
    }

    @Override
    public void tick(){
        distributionMessages = messages;
        messages = new HashSet<>();
    }

    @Override
    public BlockPos getPos() {
        return position;
    }

    @Override
    public String getTypeName() {
        return "end_pipe";
    }

    public Collection<PipeMessage> getNewMessages() {
        return distributionMessages;
    }
}
