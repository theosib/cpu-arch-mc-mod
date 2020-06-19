package eigencraft.cpuArchMod.backend.simulation;

import eigencraft.cpuArchMod.backend.dataObject.DataObject;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SimulationWorld {
    HashMap<ChunkPos,SimulationChunk> loadedChunks = new HashMap<>();
    SimulationIOManager ioManager;
    Path simulationSaveDirectory;

    public SimulationWorld(Path simulationSaveDirectory) {
        this.simulationSaveDirectory = simulationSaveDirectory;
        this.ioManager = new SimulationIOManager();
    }

    public SimulationIOManager getIoManager() {
        return ioManager;
    }

    public SimulationChunk getOrLoadChunk(ChunkPos chunkPos){
        //System.out.println(String.format("Requested chunk %d, %d",chunkPos.x,chunkPos.z));
        //If already loaded, return it
        if (loadedChunks.containsKey(chunkPos)) return loadedChunks.get(chunkPos);

        //If saved
        if (this.isChunkOnDisk(chunkPos)){
            SimulationChunk newChunk = this.loadChunkFromDisk(chunkPos);
            loadedChunks.put(chunkPos,newChunk);
            return newChunk;
        }
        // Create new, empty Chunk
        else {
            SimulationChunk newChunk = new SimulationChunk(chunkPos,this);
            loadedChunks.put(chunkPos,newChunk);
            return newChunk;
        }
    }

    public void addNode(SimulationNode node, BlockPos pos){
        this.getOrLoadChunk(new ChunkPos(pos)).addNode(node,pos);
    }

    private SimulationChunk loadChunkFromDisk(ChunkPos chunkPos){
        //TODO implement disk saving
        return null;
    }

    private void saveChunkToDisk(ChunkPos pos,SimulationChunk chunk){
        //TODO saving
    }

    private boolean isChunkOnDisk(ChunkPos pos){
        //TODO implement disk saving
        return false;
    }

    public void tick(){
        for (SimulationChunk chunk:loadedChunks.values()){
            chunk.tickPipes();
        }
        for (SimulationChunk chunk:loadedChunks.values()){
            chunk.tickNodes(ioManager);
        }
        LinkedList<SimulationIOManager.SimulationTickRunnable> tasks = ioManager.getSimulationThreadQueue();
        while (!tasks.isEmpty()){
            tasks.remove().run(this);
        }
    }

    public void saveAll() {
        for (Map.Entry<ChunkPos, SimulationChunk> toSave:loadedChunks.entrySet()){
            this.saveChunkToDisk(toSave.getKey(),toSave.getValue());
        }
    }

    public void addPipe(BlockPos blockPos) {
        this.getOrLoadChunk(new ChunkPos(blockPos)).addPipe(blockPos);
    }

    public void removePipe(BlockPos blockPos){
        getOrLoadChunk(new ChunkPos(blockPos)).removePipe(blockPos);
    }

    public void removeNode(BlockPos pos) {
        getOrLoadChunk(new ChunkPos(pos)).removeNode(pos);
    }
}
