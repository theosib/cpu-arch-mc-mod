package eigencraft.cpuArchMod.util;

import com.google.gson.*;
import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.dataObject.DataObjectType;
import net.fabricmc.fabric.api.util.NbtType;

import java.lang.reflect.Type;
import java.util.Map;

public class GsonDataObjectDeserializer implements JsonDeserializer<DataObject> {
    public static class InvalidDataObjectJsonStructureError extends Error{}
    @Override
    public DataObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        DataObject dataObject;
        try {
            //Check type name. if type not available, return with error
            String typeName;
            try {
                typeName = json.getAsJsonObject().get("type").getAsString();
            } catch (NullPointerException e){
                throw new InvalidDataObjectJsonStructureError();
            }
            //get json root
            JsonObject rootObject = json.getAsJsonObject();
            //get the dataObjectType
            DataObjectType dataObjectType = DataObjectType.getDataObjectTypeFromName(typeName);
            //Create Template
            dataObject = new DataObject(dataObjectType);
            //Iterate over all required tags
            for (Map.Entry<String, Integer> entry:dataObjectType.getRequiredTags().entrySet()) {
                //Get value from json, if absent ignore it.
                JsonElement value = rootObject.get(entry.getKey());
                if (value!=null){
                    //Switch for different types
                    switch (entry.getValue()) {
                        case NbtType.INT: {
                            dataObject.setInt(entry.getKey(), value.getAsInt());
                            break;
                        }
                        case NbtType.BYTE: {
                            dataObject.setByte(entry.getKey(), value.getAsByte());
                            break;
                        }
                        case NbtType.STRING: {
                            dataObject.setString(entry.getKey(), value.getAsString());
                            break;
                        }
                        case NbtType.BYTE_ARRAY: {
                            //Convert JsonArray to bytes[]
                            byte[] data = new byte[value.getAsJsonArray().size()];
                            int i = 0;
                            for (JsonElement element : value.getAsJsonArray()) {
                                data[i++] = element.getAsByte();
                            }
                            dataObject.setByteArray(entry.getKey(), data);
                            break;
                        }
                        case NbtType.INT_ARRAY: {
                            //Convert JsonArray to int[]
                            int[] data = new int[value.getAsJsonArray().size()];
                            int i = 0;
                            for (JsonElement element : value.getAsJsonArray()) {
                                data[i++] = element.getAsInt();
                            }
                            dataObject.setIntArray(entry.getKey(), data);
                            break;
                        }
                        default:{
                            //Shouldn't happen
                            throw new InvalidDataObjectJsonStructureError();
                        }
                    }
                }
            }

        } catch (DataObjectType.UnknownDataObjectTypeException e) {
            //Unknown dataObjectType, return with error
            throw new InvalidDataObjectJsonStructureError();
        }
        //Finally return the dataObject
        return dataObject;
    }
}
