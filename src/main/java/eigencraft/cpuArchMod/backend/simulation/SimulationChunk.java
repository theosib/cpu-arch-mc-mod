package eigencraft.cpuArchMod.backend.simulation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.*;

public class SimulationChunk {
    ChunkPos chunkPos;

    public SimulationChunk(ChunkPos chunkPos, SimulationWorld master) {
        this.chunkPos = chunkPos;
        this.master = master;
    }

    SimulationWorld master;
    private int dependentNeighbors = 0;
    private HashMap<Integer,SimulationNode> nodes = new HashMap<>();
    private HashMap<Integer, SimulationPipe> pipes = new HashMap<>();
    private List<ChunkPos> requiredChunks = new ArrayList<>();

    public void addNode(SimulationNode node, BlockPos pos){
        nodes.put(pos.hashCode(),node);

        //Load needed neighbors
        checkLoadNeighbors(pos);

        master.getOrLoadChunk(new ChunkPos(pos.north())).connectPipeFromPosToNode(node,pos.north());
        master.getOrLoadChunk(new ChunkPos(pos.south())).connectPipeFromPosToNode(node,pos.south());
        master.getOrLoadChunk(new ChunkPos(pos.east())).connectPipeFromPosToNode(node,pos.east());
        master.getOrLoadChunk(new ChunkPos(pos.west())).connectPipeFromPosToNode(node,pos.west());
        master.getOrLoadChunk(new ChunkPos(pos.up())).connectPipeFromPosToNode(node,pos.up());
        master.getOrLoadChunk(new ChunkPos(pos.down())).connectPipeFromPosToNode(node,pos.down());
    }

    public void addPipe(BlockPos blockPos){
        SimulationPipe pipe = new SimulationPipe();
        pipes.put(blockPos.hashCode(),pipe);

        //Load needed neighbors
        checkLoadNeighbors(blockPos);

        //Add pipe-pipe connections
        master.getOrLoadChunk(new ChunkPos(blockPos.north())).connectPipePipe(pipe,blockPos.north());
        master.getOrLoadChunk(new ChunkPos(blockPos.south())).connectPipePipe(pipe,blockPos.south());
        master.getOrLoadChunk(new ChunkPos(blockPos.east())).connectPipePipe(pipe,blockPos.east());
        master.getOrLoadChunk(new ChunkPos(blockPos.west())).connectPipePipe(pipe,blockPos.west());
        master.getOrLoadChunk(new ChunkPos(blockPos.up())).connectPipePipe(pipe,blockPos.up());
        master.getOrLoadChunk(new ChunkPos(blockPos.down())).connectPipePipe(pipe,blockPos.down());

        //Add pipe-node connections
        master.getOrLoadChunk(new ChunkPos(blockPos.north())).connectPipeToNodeFromPos(pipe,blockPos.north());
        master.getOrLoadChunk(new ChunkPos(blockPos.south())).connectPipeToNodeFromPos(pipe,blockPos.south());
        master.getOrLoadChunk(new ChunkPos(blockPos.east())).connectPipeToNodeFromPos(pipe,blockPos.east());
        master.getOrLoadChunk(new ChunkPos(blockPos.west())).connectPipeToNodeFromPos(pipe,blockPos.west());
        master.getOrLoadChunk(new ChunkPos(blockPos.up())).connectPipeToNodeFromPos(pipe,blockPos.up());
        master.getOrLoadChunk(new ChunkPos(blockPos.down())).connectPipeToNodeFromPos(pipe,blockPos.down());
    }

    public void removePipe(BlockPos pos){
        if (pipes.containsKey(pos.hashCode())){
            //TODO remove chunkloading when no longer required

            SimulationPipe toRemove = pipes.get(pos.hashCode());
            pipes.remove(pos.hashCode());

            this.removePipeConnections(toRemove);

            //Remove in neighbor chunks
            master.getOrLoadChunk(new ChunkPos(pos.north(16))).removePipeConnections(toRemove);
            master.getOrLoadChunk(new ChunkPos(pos.south(16))).removePipeConnections(toRemove);
            master.getOrLoadChunk(new ChunkPos(pos.east(16))).removePipeConnections(toRemove);
            master.getOrLoadChunk(new ChunkPos(pos.west(16))).removePipeConnections(toRemove);
        }
    }

    private void removePipeConnections(SimulationPipe toRemove) {
        for (SimulationPipe pipe:pipes.values()){
            pipe.remove(toRemove);
        }
        for (SimulationNode node:nodes.values()){
            node.removePipeConnection(toRemove);
        }
    }

    private void checkLoadNeighbors(BlockPos pos){
        //Need to load neighbours?
        if (!chunkPos.equals(new ChunkPos(pos.north()))){
            ChunkPos toLoad = new ChunkPos(pos.north());
            this.addDependentChunk(toLoad);
            master.getOrLoadChunk(toLoad).addDependentChunk(this.chunkPos);
        }
        if (!chunkPos.equals(new ChunkPos(pos.south()))){
            ChunkPos toLoad = new ChunkPos(pos.south());
            this.addDependentChunk(toLoad);
            master.getOrLoadChunk(toLoad).addDependentChunk(this.chunkPos);;
        }
        if (!chunkPos.equals(new ChunkPos(pos.east()))){
            ChunkPos toLoad = new ChunkPos(pos.east());
            this.addDependentChunk(toLoad);
            master.getOrLoadChunk(toLoad).addDependentChunk(this.chunkPos);;
        }
        if (!chunkPos.equals(new ChunkPos(pos.west()))){
            ChunkPos toLoad = new ChunkPos(pos.west());
            this.addDependentChunk(toLoad);
            master.getOrLoadChunk(toLoad).addDependentChunk(this.chunkPos);
        }
    }

    public void addDependentChunk(ChunkPos pos){
        requiredChunks.add(pos);
    }

    private void connectPipeFromPosToNode(SimulationNode node, BlockPos pipePos){
        if (pipes.containsKey(pipePos.hashCode())){
            SimulationPipe pipe = pipes.get(pipePos.hashCode());
            node.addPipe(pipe);
        }
    }

    private void connectPipeToNodeFromPos(SimulationPipe pipe,BlockPos nodePos){
        if (nodes.containsKey(nodePos.hashCode())){
            nodes.get(nodePos.hashCode()).addPipe(pipe);
        }
    }

    private void connectPipePipe(SimulationPipe pipe, BlockPos pipePos) {
        if (pipes.containsKey(pipePos.hashCode())){
            SimulationPipe other = pipes.get(pipePos.hashCode());
            pipe.connect(other);
            other.connect(pipe);
        }
    }


    public void tickNodes(SimulationIOManager ioManager){
        for (SimulationNode node:nodes.values()){
            node.tick(ioManager);
        }
    }

    public void tickPipes(){
        for (SimulationPipe pipe:pipes.values()){
            pipe.tick();
        }
    }

    public SimulationNode getNodeAt(BlockPos nodePos) {
        if (nodes.containsKey(nodePos.hashCode())){
            return nodes.get(nodePos.hashCode());
        }
        return null;
    }

    public void removeNode(BlockPos pos) {
        nodes.remove(pos.hashCode());
    }
}
