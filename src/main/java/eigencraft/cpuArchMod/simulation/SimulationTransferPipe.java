package eigencraft.cpuArchMod.simulation;

import eigencraft.cpuArchMod.dataObject.DataObject;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;

import java.util.*;

public class SimulationTransferPipe implements SimulationPipe{

    BlockPos position;
    private final List<SimulationPipe> connectedPipes = new ArrayList<>();
    private boolean loopBlocker = false;

    /***
     * Creates a new pipe.
     * @param position the position the pipe is located at
     */
    public SimulationTransferPipe(BlockPos position){
        this.position = position;
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
     * @param dataObject the dataObject which is sent
     */
    public void publish(DataObject dataObject){
        loopBlocker = true;
        for(SimulationPipe pipe:connectedPipes){
            pipe.interPipePublish(dataObject,this);
        }
        loopBlocker = false;
    }

    /***
     * Used to transfer dataObjects between pipes, as an normal user, you never will use this.
     * @param dataObject
     * @param src stop backTracing
     */
    public void interPipePublish(DataObject dataObject, SimulationTransferPipe src) {
        if (!loopBlocker){
            loopBlocker = true;
            for (SimulationPipe pipe : connectedPipes) {
                if (pipe == src) continue;
                try {
                    pipe.interPipePublish(dataObject, this);
                } catch (StackOverflowError stackOverflow) {
                    //TODO what if a pipe causes a stackOverflow
                    LogManager.getLogger().info("Too long pipe!");
                }
            }
        }
        loopBlocker = false;
    }

    public void tick(){
    }

    @Override
    public BlockPos getPos() {
        return position;
    }

    @Override
    public String getTypeName() {
        return "transfer_pipe";
    }

    public Collection getNewMessages() {
        return Collections.EMPTY_LIST;
    }
}
