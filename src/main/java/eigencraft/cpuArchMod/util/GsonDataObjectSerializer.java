package eigencraft.cpuArchMod.util;

import com.google.gson.*;
import eigencraft.cpuArchMod.dataObject.DataObject;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.*;

import java.lang.reflect.Type;

public class GsonDataObjectSerializer implements JsonSerializer<DataObject> {
    @Override
    public JsonElement serialize(DataObject dataObject, Type typeOfSrc, JsonSerializationContext context) {
        //Create root object
        JsonObject jsonObject = new JsonObject();
        //Get data from dataObject
        CompoundTag data = dataObject.getCompoundTag();
        //Iterate over tags of dataObject
        for (String key:data.getKeys()) {
            //Switch for datatypes
            switch (data.getType(key)){
                case NbtType.BYTE:{
                    jsonObject.addProperty(key,data.getByte(key));
                    break;
                }
                case NbtType.INT:{
                    jsonObject.addProperty(key,data.getInt(key));
                    break;
                }
                case NbtType.STRING:{
                    jsonObject.addProperty(key,data.getString(key));
                    break;
                }
                case NbtType.BYTE_ARRAY:{
                    //Convert bytes[] to JsonArray
                    JsonArray jsonArray = new JsonArray();
                    byte[] dataToTransfer = data.getByteArray(key);
                    for (int i = 0; i < dataToTransfer.length; i++) {
                        jsonArray.add(new JsonPrimitive(dataToTransfer[i]));
                    }
                    jsonObject.add(key,jsonArray);
                    break;
                }
                case NbtType.INT_ARRAY:{
                    //Convert int[] to JsonArray
                    JsonArray jsonArray = new JsonArray();
                    int[] dataToTransfer = data.getIntArray(key);
                    for (int i = 0; i < dataToTransfer.length; i++) {
                        jsonArray.add(new JsonPrimitive(dataToTransfer[i]));
                    }
                    jsonObject.add(key,jsonArray);
                    break;
                }
            }
        }
        //Finally return it
        return jsonObject;
    }


}
