package eigencraft.cpuArchMod.backend.simulation;

import net.minecraft.util.math.ChunkPos;
import org.xml.sax.helpers.DefaultHandler;

public class XMLChunkBuilder extends DefaultHandler {
    ChunkPos pos;
    SimulationWorld world;
    SimulationChunk base;

    public XMLChunkBuilder(ChunkPos pos, SimulationWorld world) {
        this.pos = pos;
        this.world = world;
    }
}
