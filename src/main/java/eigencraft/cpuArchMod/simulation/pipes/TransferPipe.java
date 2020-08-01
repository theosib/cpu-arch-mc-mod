package eigencraft.cpuArchMod.simulation.pipes;

import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.simulation.PipeMessage;
import eigencraft.cpuArchMod.simulation.SimulationPipe;
import eigencraft.cpuArchMod.simulation.SimulationPipeContext;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;

import java.util.*;

public class TransferPipe implements SimulationPipe {

    SimulationPipeContext context;
    private final List<SimulationPipe> connectedPipes = new ArrayList<>();
    private boolean loopBlocker = false;

    /***
     * Creates a new pipe.
     * @param context informations about the pipe
     */
    public TransferPipe(SimulationPipeContext context){
        this.context = context;
    }

    /***
     * Connects a pipe to a pipe. This does only enable communication in the direction from this to the other pipe. For communications in both directions, also call otherPipe.connect(this);
     * @param pipe the pipe which will be connected
     */
    public void connect(SimulationPipe pipe){
        connectedPipes.add(pipe);
    }

    /***
     * Removes a pipe connection.This does only remove communication in the direction from this to the other pipe. For communications in both directions, also call otherPipe.remove(this);
     * @param pipe the pipe that will be removed
     */
    public void removeConnection(SimulationPipe pipe){
        connectedPipes.remove(pipe);
    }

    /***
     * Sends a dataObject from this pipe to all connected pipes
     * @param pipeMessage the dataObject which is sent
     */
    public void publish(PipeMessage pipeMessage){
        loopBlocker = true;
        for(SimulationPipe pipe:connectedPipes){
            pipe.interPipePublish(pipeMessage);
        }
        loopBlocker = false;
    }

    /***
     * Used to transfer dataObjects between pipes, as an normal user, you never will use this.
     * @param message the message to transfer
     */
    public void interPipePublish(PipeMessage message) {
        if (!loopBlocker){
            loopBlocker = true;
            for (SimulationPipe pipe : connectedPipes) {
                try {
                    pipe.interPipePublish(message);
                } catch (StackOverflowError stackOverflow) {
                    //TODO what if a pipe causes a stackOverflow
                    LogManager.getLogger().info("Too long pipe!");
                }
            }
            loopBlocker = false;
        }
    }

    public void tick(){
    }

    @Override
    public SimulationPipeContext getContext() {
        return context;
    }

    @Override
    public String getTypeName() {
        return "transfer_pipe";
    }

    public Collection getNewMessages() {
        return Collections.EMPTY_LIST;
    }
}
