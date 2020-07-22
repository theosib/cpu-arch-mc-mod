package eigencraft.cpuArchMod.simulation.storage;

import net.minecraft.util.math.ChunkPos;

import java.io.File;

public class SaveFormatUtils {
    private static final int version = 1;

    public static int getVersion() {
        return version;
    }

    public static File getSavePathForChunk(ChunkPos pos, File worldSavePath){
        return new File(worldSavePath,String.format("x%dz%d.xml",pos.x,pos.z));
    }

    public static boolean isChunkOnDisk(ChunkPos pos,File simulationSaveDirectory){
        File chunkFile =  getSavePathForChunk(pos,simulationSaveDirectory);
        return chunkFile.isFile();
    }
}
