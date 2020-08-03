package eigencraft.cpuArchMod.util;

import com.google.gson.*;
import eigencraft.cpuArchMod.simulation.MultiInputNodeHelper;
import eigencraft.cpuArchMod.simulation.PipeLane;

import java.lang.reflect.Type;
import java.util.Map;

public class NodeInputHelperDeserializer implements JsonDeserializer<MultiInputNodeHelper> {

    @Override
    public MultiInputNodeHelper deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject root = jsonElement.getAsJsonObject();
        MultiInputNodeHelper multiInputHelper = new MultiInputNodeHelper();
        for (Map.Entry<String, JsonElement> entry :root.entrySet()){
            multiInputHelper.configure(PipeLane.valueOf(entry.getKey()),entry.getValue().getAsInt());
        }
        return multiInputHelper;
    }
}
