package eigencraft.cpuArchMod.simulation.storage;

import eigencraft.cpuArchMod.simulation.SimulationChunk;
import eigencraft.cpuArchMod.simulation.SimulationNode;
import eigencraft.cpuArchMod.simulation.SimulationPipe;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class XMLChunkSaver {
    public static void save(SimulationChunk chunk, File worldSavePath){
        StringBuilder xmlString = new StringBuilder();
        xmlString.append(String.format("<chunk version='%d'>", SaveFormatUtils.getVersion()));
        for (SimulationNode node:chunk.getNodes()) {
            xmlString.append(String.format("<node x='%d' y='%d' z='%d' type='%s' />",node.position.getX(),node.position.getY(),node.position.getZ(),node.getClass().getSimpleName()));
        }
        for (SimulationPipe pipe:chunk.getPipes()) {
            xmlString.append(String.format("<pipe x='%d' y='%d' z='%d' type='%s'/>",pipe.getPos().getX(),pipe.getPos().getY(),pipe.getPos().getZ(),pipe.getClass().getSimpleName()));
        }

        xmlString.append("</chunk>");

        File chunkFile = SaveFormatUtils.getSavePathForChunk(chunk.getChunkPos(),worldSavePath);

        try {
            FileWriter writer = new FileWriter(chunkFile);
            writer.write(xmlString.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
