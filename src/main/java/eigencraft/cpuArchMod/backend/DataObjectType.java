package eigencraft.cpuArchMod.backend;

import net.fabricmc.fabric.api.util.NbtType;

import java.util.HashMap;

public class DataObjectType {
    private static HashMap<String,DataObjectType> types = new HashMap<>();
    
    private HashMap<String, Integer> requiredTags = new HashMap<>();
    private String name;

    /***
     * @param typeName name of the type that should be created
     * @return a new or the already existing DataObjectType
     * ***/
    public static DataObjectType create(String typeName){
        if (types.containsKey(typeName)) return types.get(typeName);
        return new DataObjectType(typeName);
    }


    private DataObjectType(String typeName){
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

    public static DataObjectType getDataObjectTypeFromName(String name) throws UnknownDataObjectTypeException {
        if (!types.containsKey(name)) throw new UnknownDataObjectTypeException();
        return types.get(name);
    }

    public static class UnknownDataObjectTypeException extends Exception{}
}
