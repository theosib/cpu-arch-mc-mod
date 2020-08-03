package eigencraft.cpuArchMod.simulation.pipes;

import eigencraft.cpuArchMod.simulation.*;
import net.minecraft.util.DyeColor;

import java.util.Collection;
import java.util.HashSet;

public class EndPipe implements SimulationMessageProvidingPipe{

    protected HashSet<PipeMessage> messages = new HashSet<>();
    private HashSet<PipeMessage> distributionMessages = new HashSet<>();
    SimulationPipeContext context;

    public EndPipe(SimulationPipeContext context) {
       this.context = context;
    }

    @Override
    public void interPipePublish(PipeMessage message) {
        //Only store it, but don't redistribute it.
        DyeColor color = context.getColor();
        messages.add(new PipeMessage(message.getDataObject(), (color==null)?message.getLane():PipeLane.convert(color)));
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
    public SimulationPipeContext getContext() {
        return context;
    }

    @Override
    public String getTypeName() {
        return "end_pipe";
    }

    public Collection<PipeMessage> getNewMessages() {
        return distributionMessages;
    }
}
