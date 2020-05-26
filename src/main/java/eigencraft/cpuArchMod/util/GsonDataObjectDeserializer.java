package eigencraft.cpuArchMod.util;

import com.google.gson.*;
import eigencraft.cpuArchMod.backend.DataObject;
import eigencraft.cpuArchMod.backend.DataObjectType;
import net.fabricmc.fabric.api.util.NbtType;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

public class GsonDataObjectDeserializer implements JsonDeserializer<DataObject> {
    public static class JsonStructureException extends Exception{}
    @Override
    public DataObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)throws JsonParseException {
        String typeName = json.getAsJsonObject().get("type").getAsString();
        JsonObject rootObject = json.getAsJsonObject();
        DataObject dataObject;
        try {
            DataObjectType dataObjectType = DataObjectType.getDataObjectTypeFromName(typeName);
            dataObject = new DataObject(dataObjectType);
            for (Map.Entry<String, Integer> entry:dataObjectType.getRequiredTags().entrySet()) {
                JsonElement value = rootObject.get(entry.getKey());
                switch (entry.getValue()){
                    case NbtType.INT:{
                        dataObject.setInt(entry.getKey(),value.getAsInt());
                        break;
                    }
                    case NbtType.BYTE:{
                        dataObject.setByte(entry.getKey(),value.getAsByte());
                        break;
                    }
                    case NbtType.STRING:{
                        System.out.println(entry.getKey());
                        dataObject.setString(entry.getKey(),value.getAsString());
                        break;
                    }
                    case NbtType.BYTE_ARRAY:{
                        byte[] data = new byte[value.getAsJsonArray().size()];
                        int i = 0;
                        for (JsonElement element:value.getAsJsonArray()) {
                            data[i++] = element.getAsByte();
                        }
                        dataObject.setByteArray(entry.getKey(),data);
                        break;
                    }
                    case NbtType.INT_ARRAY:{
                        int[] data = new int[value.getAsJsonArray().size()];
                        int i = 0;
                        for (JsonElement element:value.getAsJsonArray()) {
                            data[i++] = element.getAsInt();
                        }
                        dataObject.setIntArray(entry.getKey(),data);
                        break;
                    }
                }
            }

        } catch (DataObjectType.UnknownDataObjectTypeException e) {
            throw new JsonParseException("Invalid datatype!");
        }
        return dataObject;
    }
}
