package eigencraft.cpuArchMod.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import eigencraft.cpuArchMod.dataObject.DataObjectType;

import java.lang.reflect.Type;

public class GsonDataObjectTypeSerializer implements JsonSerializer<DataObjectType> {
    @Override
    public JsonElement serialize(DataObjectType src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getName());
    }
}
