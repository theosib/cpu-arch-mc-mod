package eigencraft.cpuArchMod.backend.simulation;

import eigencraft.cpuArchMod.backend.dataObject.DataObject;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class SimulationPipe{

    BlockPos position;
    private List<SimulationPipe> connectedPipes = new ArrayList<>();
    private HashSet<DataObject> messages = new HashSet<>();
    private HashSet<DataObject> distributionMessages = new HashSet<>();
    private boolean loopBlocker = false;

    /***
     * Creates a new pipe.
     * @param position the position the pipe is located at
     */
    public SimulationPipe(BlockPos position){
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
    public boolean removeConnection(SimulationPipe pipe){
        return connectedPipes.remove(pipe);
    }

    /***
     * Sends a dataObject from this pipe to all connected pipes
     * @param dataObject the dataObject which is sent
     */
    public void publish(DataObject dataObject){
        loopBlocker = true;
        messages.add(dataObject);
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
    private void interPipePublish(DataObject dataObject, SimulationPipe src) {
        if (!loopBlocker){
            loopBlocker = true;
            messages.add(dataObject);
            for (SimulationPipe pipe : connectedPipes) {
                if (pipe == src) continue;
                try {
                    pipe.interPipePublish(dataObject, this);
                } catch (StackOverflowError stackOverflow) {
                    //TODO what if a pipe causes a stackOverflow
                }
            }
        }
        loopBlocker = false;
    }

    /***
     * Swaps the internal buffer
     */
    public void tick(){
        distributionMessages = messages;
        messages = new HashSet<>();
    }

    /***
     * Returns all collected dataObjects from last tick
     * @return the dataObject from last tick
     */
    public Collection<DataObject> getNewMessages() {
        return distributionMessages;
    }
}
