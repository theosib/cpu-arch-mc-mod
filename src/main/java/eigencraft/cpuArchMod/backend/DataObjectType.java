package eigencraft.cpuArchMod.backend;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataObjectType {
    private static HashMap<String,DataObjectType> types = new HashMap<>();
    
    private HashMap<String, Integer> requiredTags = new HashMap<>();
    private String name;

    /***
     * @param typeName name of the type that should be created
     * ***/
    public DataObjectType(String typeName){
        requiredTags.put("type", NbtType.STRING);
        types.put(typeName,this);
        this.name = typeName;
    }
    
    /***
     * @param name the name of the new required tag
     * @param type types defined in net.fabricmc.fabric.api.util.NbtType
     * ***/
    public void addTag(String name,int type){
        requiredTags.put(name,type);
    }

    /***
     * Returns the name of this dataType
     * ***/
    public String getName() {
        return name;
    }

    /***
     * @return all required tags
     * ***/
    public HashMap<String, Integer> getRequiredTags() {
        return requiredTags;
    }

    public static DataObjectType getDataObjectTypeFromName(String name){
        return types.get(name);
    }
}
