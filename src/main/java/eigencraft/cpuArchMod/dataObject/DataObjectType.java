package eigencraft.cpuArchMod.dataObject;

import net.fabricmc.fabric.api.util.NbtType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataObjectType {
    private static HashMap<String,DataObjectType> types = new HashMap<>();
    private static final int[] allowedTags = {NbtType.BYTE,NbtType.INT,NbtType.STRING,NbtType.BYTE_ARRAY,NbtType.INT_ARRAY};
    
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
        for (int i = 0; i < allowedTags.length; i++) {
            if (allowedTags[i]==type){
                requiredTags.put(name,type);
                return;
            }
        }
        throw new UnsupportedTagTypeProgrammerMistake();
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

    public boolean matchesType(DataObject in){
        return (in.getType().equals(name));
    }

    public int getFieldTypeFromName(String name){
        if (!requiredTags.containsKey(name))return 0;
        return requiredTags.get(name);
    }

    public static DataObjectType getDataObjectTypeFromName(String name) throws UnknownDataObjectTypeException {
        if (!types.containsKey(name)) throw new UnknownDataObjectTypeException();
        return types.get(name);
    }

    public static DataObjectType getDataObjectTypeFromNameOrNull(String name){
        if (!types.containsKey(name)) return null;
        return types.get(name);
    }

    public static List<String> getDataObjectTypeNames(){
        return new ArrayList<String>(types.keySet());
    }

    public static class UnknownDataObjectTypeException extends Exception{}
    public static class UnsupportedTagTypeProgrammerMistake extends Error{}
}
