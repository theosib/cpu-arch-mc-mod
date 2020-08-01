package eigencraft.cpuArchMod.simulation.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eigencraft.cpuArchMod.CpuArchMod;
import eigencraft.cpuArchMod.simulation.*;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class JSONChunkLoader{


    public static SimulationChunk load(SimulationChunk target,SimulationWorld world, File file){
        JsonObject root;
        try {
            root = JsonParser.parseString(new String(Files.readAllBytes(file.toPath()))).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return target;
        }

        try {
            int version = root.get("version").getAsInt();
            if (version > SaveFormat.getVersion()){
                LogManager.getLogger().warn("Found simulation chunk from newer mod version!");
            } else if (version < SaveFormat.getVersion()){
                //TODO upgrade system?
                LogManager.getLogger().warn(String.format("Found simulation chunk from older mod version! Found: %d, running version: %d",version,SaveFormat.getVersion()));
            }
            JsonArray pipes = root.get("pipes").getAsJsonArray();
            for (JsonElement pipeElement:pipes){
                SimulationPipeContext context = CpuArchMod.GSON.fromJson(pipeElement,SimulationPipeContext.class);
                SimulationPipe pipe = SimulationPipe.getFromName(pipeElement.getAsJsonObject().get("type").getAsString()).apply(context);
                target.addPipe(pipe,context.getPosition());
            }

            JsonArray nodes = root.get("nodes").getAsJsonArray();
            for (JsonElement nodeElement:nodes){
                JsonObject nodeObject = nodeElement.getAsJsonObject();
                BlockPos nodePos = new BlockPos(nodeObject.get("x").getAsInt(),nodeObject.get("y").getAsInt(),nodeObject.get("z").getAsInt());
                SimulationNode node = SimulationNode.getFromName(nodeObject.get("type").getAsString()).apply(nodePos);
                target.addNode(node,nodePos);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return target;
        }
        return target;
    }
}
