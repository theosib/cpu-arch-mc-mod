package eigencraft.cpuArchMod.simulation.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import eigencraft.cpuArchMod.CpuArchMod;
import eigencraft.cpuArchMod.simulation.SimulationChunk;
import eigencraft.cpuArchMod.simulation.SimulationNode;
import eigencraft.cpuArchMod.simulation.SimulationPipe;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JSONChunkSaver {
    public static void save(SimulationChunk chunk, File worldSavePath){
        JsonObject root = new JsonObject();

        root.add("version",new JsonPrimitive(SaveFormat.getVersion()));

        JsonArray nodes = new JsonArray();
        root.add("nodes",nodes);

        for (SimulationNode node:chunk.getNodes()){
            JsonObject jsonNode = new JsonObject();

            jsonNode.add("type",new JsonPrimitive(node.getClass().getSimpleName()));
            jsonNode.add("x",new JsonPrimitive(node.position.getX()));
            jsonNode.add("y",new JsonPrimitive(node.position.getY()));
            jsonNode.add("z",new JsonPrimitive(node.position.getZ()));

            nodes.add(jsonNode);
        }

        JsonArray pipes = new JsonArray();
        root.add("pipes",pipes);

        for (SimulationPipe pipe:chunk.getPipes()) {
            JsonObject jsonPipe = (JsonObject) CpuArchMod.GSON.toJsonTree(pipe.getContext());
            jsonPipe.add("type",new JsonPrimitive(pipe.getClass().getSimpleName()));
            pipes.add(jsonPipe);
        }


        File chunkFile = SaveFormat.getSavePathForChunk(chunk.getChunkPos(),worldSavePath);

        try {
            FileWriter writer = new FileWriter(chunkFile);
            writer.write(root.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
