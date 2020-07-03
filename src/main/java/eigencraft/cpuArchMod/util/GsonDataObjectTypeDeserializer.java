package eigencraft.cpuArchMod.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.dataObject.DataObjectType;

import java.lang.reflect.Type;

public class GsonDataObjectTypeDeserializer implements JsonDeserializer<DataObjectType> {
    @Override
    public DataObjectType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return DataObjectType.getDataObjectTypeFromName(json.getAsString());
        } catch (DataObjectType.UnknownDataObjectTypeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
