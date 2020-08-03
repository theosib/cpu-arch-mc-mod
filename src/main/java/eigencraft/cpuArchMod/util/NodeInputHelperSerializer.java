package eigencraft.cpuArchMod.util;

import com.google.gson.*;
import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.simulation.MultiInputNodeHelper;
import eigencraft.cpuArchMod.simulation.PipeLane;

import java.lang.reflect.Type;
import java.util.Map;

public class NodeInputHelperSerializer implements JsonSerializer<MultiInputNodeHelper> {
    @Override
    public JsonElement serialize(MultiInputNodeHelper multiInputNodeHelper, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject root = new JsonObject();
        for (Map.Entry<PipeLane,Integer> adapterEntry:multiInputNodeHelper.getConfiguration().entrySet()){
            root.add(adapterEntry.getKey().toString(),new JsonPrimitive(adapterEntry.getValue()));
        }
        return root;
    }
}
