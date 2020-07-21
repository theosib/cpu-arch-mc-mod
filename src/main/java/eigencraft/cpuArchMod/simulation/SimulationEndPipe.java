package eigencraft.cpuArchMod.simulation;

import eigencraft.cpuArchMod.dataObject.DataObject;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class SimulationEndPipe implements SimulationMessageProvidingPipe {

    protected HashSet<DataObject> messages = new HashSet<>();
    private HashSet<DataObject> distributionMessages = new HashSet<>();
    BlockPos position;

    public SimulationEndPipe(BlockPos position) {
        this.position = position;
    }

    @Override
    public void interPipePublish(DataObject dataObject, SimulationTransferPipe src) {
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
    public void publish(DataObject dataObject) {
        //Only store it, but don't redistribute it.
        messages.add(dataObject);
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

    public Collection<DataObject> getNewMessages() {
        return distributionMessages;
    }
}
