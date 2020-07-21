package eigencraft.cpuArchMod.dataObject;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;

import java.util.*;
/***
 * A DataObject
 * you need only to check if it ii of a given type, and it's ensured to have every required tag with at least a default value
 * ***/
public class DataObject {

    private CompoundTag dataStorage;

    /***
     * @param type The DataObjectType used as template. It will be initialised with defaults
     * ***/
    public DataObject(DataObjectType type) {
        dataStorage = new CompoundTag();
        //Initialise with all required tags, setting them to defaults, so we don't need to check if a type is valid
        for (Map.Entry<String, Integer> entry : type.getRequiredTags().entrySet()){
            addDefaultToCompoundTag(dataStorage,entry.getKey(),entry.getValue());
        }
        //Set type name
        dataStorage.putString("type",type.getName());
    }

    /***
     * @param src CompoundTag used as source. Will be validated and fixed, to be type-save.
     * @throws DataObjectType.UnknownDataObjectTypeException
     * ***/
    public DataObject(CompoundTag src) throws DataObjectType.UnknownDataObjectTypeException {
        dataStorage = fixNbtCompoundAsDataObject(src);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataObject that = (DataObject) o;
        return dataStorage.equals(that.dataStorage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataStorage);
    }

    public String getType(){
        return dataStorage.getString("type");
    }

    public CompoundTag getCompoundTag(){return dataStorage;}

    private static CompoundTag fixNbtCompoundAsDataObject(CompoundTag compoundTag) throws DataObjectType.UnknownDataObjectTypeException {
        if (compoundTag==null) throw new DataObjectType.UnknownDataObjectTypeException();
        if (!compoundTag.contains("type",NbtType.STRING)) throw new DataObjectType.UnknownDataObjectTypeException();

        String typeName = compoundTag.getString("type");
        DataObjectType type = DataObjectType.getDataObjectTypeFromName(typeName);

        for (Map.Entry<String, Integer> entry : type.getRequiredTags().entrySet()){
            if (!compoundTag.contains(entry.getKey(),entry.getValue())){
                addDefaultToCompoundTag(compoundTag,entry.getKey(),entry.getValue());
            }
        }
        return compoundTag;
    }

    private static void addDefaultToCompoundTag(CompoundTag tag,String name,int nbtType){
        switch (nbtType){
            case NbtType.BYTE:{
                tag.putByte(name,(byte)0);
                break;
            }
            case NbtType.BYTE_ARRAY:{
                tag.putByteArray(name, new byte[]{});
                break;
            }
            case NbtType.INT:{
                tag.putInt(name, 0);
                break;
            }
            case NbtType.INT_ARRAY:{
                tag.putIntArray(name, new int[]{});
                break;
            }
            case NbtType.STRING:{
                tag.putString(name, "");
                break;
            }
            default:{
                throw new IllegalArgumentException("Invalid datatype");
            }
        }
    }



    //Interfaces for tags

    public void setByte(String key, byte value) {
        dataStorage.putByte(key, value);
    }

    public void setInt(String key, int value) {
        dataStorage.putInt(key, value);
    }

    public void setString(String key, String value) {
        dataStorage.putString(key, value);
    }

    public void setByteArray(String key, byte[] value) {
        dataStorage.putByteArray(key, value);
    }

    public void setIntArray(String key, int[] value) {
        dataStorage.putIntArray(key, value);
    }

    public void setIntArray(String key, List<Integer> value) {
        dataStorage.putIntArray(key, value);
    }

    public byte getByte(String key) {
        return dataStorage.getByte(key);
    }

    public int getInt(String key) {
        return dataStorage.getInt(key);
    }

    public String getString(String key) {
        return dataStorage.getString(key);
    }

    public byte[] getByteArray(String key) {
        return dataStorage.getByteArray(key);
    }

    public int[] getIntArray(String key) {
        return dataStorage.getIntArray(key);
    }

    public boolean matchType(DataObjectType toCompare) {
        return getType().equals(toCompare.getName());
    }
}
