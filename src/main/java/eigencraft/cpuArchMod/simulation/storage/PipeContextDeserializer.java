package eigencraft.cpuArchMod.simulation.storage;

import com.google.gson.*;
import eigencraft.cpuArchMod.simulation.SimulationPipeContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Type;

public class PipeContextDeserializer implements JsonDeserializer<SimulationPipeContext> {
    @Override
    public SimulationPipeContext deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SimulationPipeContext pipeContext = new SimulationPipeContext();
        if (json.isJsonObject()){
            JsonObject root = json.getAsJsonObject();
            pipeContext.setColor(DyeColor.byName(root.get("color").getAsString(),null));
            pipeContext.setPosition(new BlockPos(root.get("x").getAsInt(),root.get("y").getAsInt(),root.get("z").getAsInt()));
        }
        return pipeContext;
    }
}
