package eigencraft.cpuArchMod.util;

import com.google.common.collect.Lists;
import com.google.gson.*;
import eigencraft.cpuArchMod.backend.DataObject;
import eigencraft.cpuArchMod.backend.DataObjectType;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.*;

import java.lang.reflect.Type;
import java.util.*;

public class GsonDataObjectSerializer implements JsonSerializer<DataObject> {
    @Override
    public JsonElement serialize(DataObject dataObject, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        CompoundTag data = dataObject.getCompoundTag();
        for (String key:data.getKeys()) {
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
                    JsonArray jsonArray = new JsonArray();
                    byte[] dataToTransfer = data.getByteArray(key);
                    for (int i = 0; i < dataToTransfer.length; i++) {
                        jsonArray.add(new JsonPrimitive(dataToTransfer[i]));
                    }
                    jsonObject.add(key,jsonArray);
                    break;
                }
                case NbtType.INT_ARRAY:{
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
        return jsonObject;
    }


}
