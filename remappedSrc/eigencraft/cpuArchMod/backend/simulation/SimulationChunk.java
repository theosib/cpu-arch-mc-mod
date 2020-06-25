package eigencraft.cpuArchMod.backend.simulation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.*;

public class SimulationChunk {
    ChunkPos chunkPos;

    public SimulationChunk(ChunkPos chunkPos, SimulationWorld world) {
        this.chunkPos = chunkPos;
        this.world = world;
    }

    SimulationWorld world;
    private HashMap<Integer,SimulationNode> nodes = new HashMap<>();
    private HashMap<Integer, SimulationPipe> pipes = new HashMap<>();

    public String save(){
        StringBuilder xmlString = new StringBuilder();
        xmlString.append("<chunk>");
        for (SimulationNode node:nodes.values()) {
            xmlString.append(String.format("<node x='%d' y='%d' z='%d' type='%s' />",node.position.getX(),node.position.getY(),node.position.getZ(),node.getClass().getSimpleName()));
        }
        for (SimulationPipe pipe:pipes.values()) {
            xmlString.append(String.format("<pipe x='%d' y='%d' z='%d' />",pipe.position.getX(),pipe.position.getY(),pipe.position.getZ()));
        }

        xmlString.append("</chunk>");
        return xmlString.toString();
    }

    public boolean shouldSave(){
        return (!nodes.isEmpty())||(!pipes.isEmpty());
    }

    public void addNode(SimulationNode node, BlockPos pos){
        nodes.put(pos.hashCode(),node);

        //Load needed neighbors
        checkLoadNeighbors(pos);

        world.getOrLoadChunk(new ChunkPos(pos.north())).connectPipeFromPosToNode(node,pos.north());
        world.getOrLoadChunk(new ChunkPos(pos.south())).connectPipeFromPosToNode(node,pos.south());
        world.getOrLoadChunk(new ChunkPos(pos.east())).connectPipeFromPosToNode(node,pos.east());
        world.getOrLoadChunk(new ChunkPos(pos.west())).connectPipeFromPosToNode(node,pos.west());
        world.getOrLoadChunk(new ChunkPos(pos.up())).connectPipeFromPosToNode(node,pos.up());
        world.getOrLoadChunk(new ChunkPos(pos.down())).connectPipeFromPosToNode(node,pos.down());
    }

    public void removeNode(BlockPos pos) {
        nodes.remove(pos.hashCode());
        if (!chunkPos.equals(new ChunkPos(pos.north()))){
        }
        if (!chunkPos.equals(new ChunkPos(pos.south()))){
        }
        if (!chunkPos.equals(new ChunkPos(pos.east()))){
        }
        if (!chunkPos.equals(new ChunkPos(pos.west()))){
        }
    }

    public void addPipe(BlockPos blockPos){
        SimulationPipe pipe = new SimulationPipe(blockPos);
        pipes.put(blockPos.hashCode(),pipe);

        //Load needed neighbors
        checkLoadNeighbors(blockPos);

        //Add pipe-pipe connections
        world.getOrLoadChunk(new ChunkPos(blockPos.north())).connectPipePipe(pipe,blockPos.north());
        world.getOrLoadChunk(new ChunkPos(blockPos.south())).connectPipePipe(pipe,blockPos.south());
        world.getOrLoadChunk(new ChunkPos(blockPos.east())).connectPipePipe(pipe,blockPos.east());
        world.getOrLoadChunk(new ChunkPos(blockPos.west())).connectPipePipe(pipe,blockPos.west());
        world.getOrLoadChunk(new ChunkPos(blockPos.up())).connectPipePipe(pipe,blockPos.up());
        world.getOrLoadChunk(new ChunkPos(blockPos.down())).connectPipePipe(pipe,blockPos.down());

        //Add pipe-node connections
        world.getOrLoadChunk(new ChunkPos(blockPos.north())).connectPipeToNodeFromPos(pipe,blockPos.north());
        world.getOrLoadChunk(new ChunkPos(blockPos.south())).connectPipeToNodeFromPos(pipe,blockPos.south());
        world.getOrLoadChunk(new ChunkPos(blockPos.east())).connectPipeToNodeFromPos(pipe,blockPos.east());
        world.getOrLoadChunk(new ChunkPos(blockPos.west())).connectPipeToNodeFromPos(pipe,blockPos.west());
        world.getOrLoadChunk(new ChunkPos(blockPos.up())).connectPipeToNodeFromPos(pipe,blockPos.up());
        world.getOrLoadChunk(new ChunkPos(blockPos.down())).connectPipeToNodeFromPos(pipe,blockPos.down());
    }

    public void removePipe(BlockPos pos){
        if (pipes.containsKey(pos.hashCode())){

            SimulationPipe toRemove = pipes.get(pos.hashCode());
            pipes.remove(pos.hashCode());

            this.removePipeConnections(toRemove);

            //Remove in neighbor chunks
            if (!chunkPos.equals(new ChunkPos(pos.north()))){
                ChunkPos toLoad = new ChunkPos(pos.north());
                world.getOrLoadChunk(toLoad).removePipeConnections(toRemove);
            }
            if (!chunkPos.equals(new ChunkPos(pos.south()))){
                ChunkPos toLoad = new ChunkPos(pos.south());
                world.getOrLoadChunk(toLoad).removePipeConnections(toRemove);
            }
            if (!chunkPos.equals(new ChunkPos(pos.east()))){
                ChunkPos toLoad = new ChunkPos(pos.east());
                world.getOrLoadChunk(toLoad).removePipeConnections(toRemove);
            }
            if (!chunkPos.equals(new ChunkPos(pos.west()))){
                ChunkPos toLoad = new ChunkPos(pos.west());
                world.getOrLoadChunk(toLoad).removePipeConnections(toRemove);
            }
        }
    }

    private void removePipeConnections(SimulationPipe toRemove) {
        for (SimulationPipe pipe:pipes.values()){
            pipe.removeConnection(toRemove);
        }
        for (SimulationNode node:nodes.values()){
            node.removePipe(toRemove);
        }
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

    private void checkLoadNeighbors(BlockPos pos){
        //Need to load neighbours?
        if (!chunkPos.equals(new ChunkPos(pos.north()))){
            ChunkPos toLoad = new ChunkPos(pos.north());
            world.getOrLoadChunk(toLoad);
        }
        if (!chunkPos.equals(new ChunkPos(pos.south()))){
            ChunkPos toLoad = new ChunkPos(pos.south());
            world.getOrLoadChunk(toLoad);
        }
        if (!chunkPos.equals(new ChunkPos(pos.east()))){
            ChunkPos toLoad = new ChunkPos(pos.east());
            world.getOrLoadChunk(toLoad);
        }
        if (!chunkPos.equals(new ChunkPos(pos.west()))){
            ChunkPos toLoad = new ChunkPos(pos.west());
            world.getOrLoadChunk(toLoad);
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


}
