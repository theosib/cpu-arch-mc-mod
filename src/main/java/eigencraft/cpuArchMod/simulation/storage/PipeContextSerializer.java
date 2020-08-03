package eigencraft.cpuArchMod.simulation.storage;

import com.google.gson.*;
import eigencraft.cpuArchMod.simulation.SimulationPipeContext;

import java.lang.reflect.Type;

public class PipeContextSerializer implements JsonSerializer<SimulationPipeContext> {
    @Override
    public JsonElement serialize(SimulationPipeContext src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject root = new JsonObject();
        root.add("color",new JsonPrimitive((src.getColor()==null)?"null":src.getColor().asString()));
        root.add("x",new JsonPrimitive(src.getPosition().getX()));
        root.add("y",new JsonPrimitive(src.getPosition().getY()));
        root.add("z",new JsonPrimitive(src.getPosition().getZ()));
        return root;
    }
}
