package eigencraft.cpuArchMod.simulation;

import eigencraft.cpuArchMod.simulation.storage.SaveFormat;
import eigencraft.cpuArchMod.simulation.storage.JSONChunkLoader;
import eigencraft.cpuArchMod.simulation.storage.JSONChunkSaver;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SimulationWorld {
    HashMap<ChunkPos,SimulationChunk> loadedChunks = new HashMap<>();
    SimulationIOManager ioManager;
    File simulationSaveDirectory;

    public SimulationWorld(File simulationSaveDirectory, World world) {
        this.simulationSaveDirectory = simulationSaveDirectory;
        this.ioManager = new SimulationIOManager(world);
    }

    public SimulationIOManager getIoManager() {
        return ioManager;
    }

    public SimulationChunk getOrLoadChunk(ChunkPos chunkPos){
        //If already loaded, return it
        if (loadedChunks.containsKey(chunkPos)) return loadedChunks.get(chunkPos);
        System.out.println(String.format("Requested chunk %d, %d",chunkPos.x,chunkPos.z));
        //If saved
        if (SaveFormat.isChunkOnDisk(chunkPos,simulationSaveDirectory)){
            //Create empty chunk
            SimulationChunk newChunk = new SimulationChunk(chunkPos,this);
            //Mark chunk as loaded
            loadedChunks.put(chunkPos,newChunk);
            //Load the data
            JSONChunkLoader.load(newChunk,this, SaveFormat.getSavePathForChunk(chunkPos,simulationSaveDirectory));
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

    public void addPipe(SimulationPipe pipe, BlockPos pos){
        this.getOrLoadChunk(new ChunkPos(pos)).addPipe(pipe,pos);
    }

    public void removePipe(BlockPos blockPos){
        getOrLoadChunk(new ChunkPos(blockPos)).removePipe(blockPos);
    }

    public void removeNode(BlockPos pos) {
        getOrLoadChunk(new ChunkPos(pos)).removeNode(pos);
    }

    private void saveChunkToDisk(ChunkPos pos,SimulationChunk chunk){
        if (chunk.shouldSave()){
            JSONChunkSaver.save(chunk,simulationSaveDirectory);
        } else {
            //Empty chunk
            File chunkFile = SaveFormat.getSavePathForChunk(chunk.chunkPos,simulationSaveDirectory);
            if (chunkFile.exists()){
                chunkFile.delete();
            }
        }
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
        //TODO unload unused chunks
        LogManager.getLogger().info("Saved simulation world");
    }
}
