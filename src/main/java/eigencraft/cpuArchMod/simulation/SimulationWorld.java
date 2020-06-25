package eigencraft.cpuArchMod.simulation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        if (this.isChunkOnDisk(chunkPos)){
            SimulationChunk newChunk = new SimulationChunk(chunkPos,this);
            loadedChunks.put(chunkPos,newChunk);
            new XMLChunkBuilder(newChunk,this,new File(simulationSaveDirectory,getSavePathForChunk(chunkPos)));
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

    public void addPipe(BlockPos blockPos) {
        this.getOrLoadChunk(new ChunkPos(blockPos)).addPipe(blockPos);
    }

    public void removePipe(BlockPos blockPos){
        getOrLoadChunk(new ChunkPos(blockPos)).removePipe(blockPos);
    }

    public void removeNode(BlockPos pos) {
        getOrLoadChunk(new ChunkPos(pos)).removeNode(pos);
    }

    private void saveChunkToDisk(ChunkPos pos,SimulationChunk chunk){
        File chunkFile = new File(simulationSaveDirectory, getSavePathForChunk(pos));
        if (chunk.shouldSave()){
            String data = chunk.save();
            try {
                FileWriter writer = new FileWriter(chunkFile);
                writer.write(data);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //Empty chunk
            if (chunkFile.exists()){
                chunkFile.delete();
            }
        }
    }

    private boolean isChunkOnDisk(ChunkPos pos){
        File chunkFile =  new File(simulationSaveDirectory,getSavePathForChunk(pos));
        return chunkFile.isFile();
    }

    private String getSavePathForChunk(ChunkPos pos){
        return String.format("x%dz%d.xml",pos.x,pos.z);
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
}
