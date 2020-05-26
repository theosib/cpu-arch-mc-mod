package eigencraft.cpuArchMod.backend;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

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

    public String getType(){
        return dataStorage.getString("type");
    }

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
            case NbtType.DOUBLE:{
                tag.putDouble(name, 0);
                break;
            }
            case NbtType.FLOAT:{
                tag.putFloat(name, 0);
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
            case NbtType.LIST:{
                tag.put(name,new ListTag());
                break;
            }
            case NbtType.LONG:{
                tag.putLong(name, 0);
                break;
            }
            case NbtType.LONG_ARRAY:{
                tag.putLongArray(name, new long[]{});
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
    public void putTag(String key, Tag tag) {
        dataStorage.put(key, tag);
    }

    public void setByte(String key, byte value) {
        dataStorage.putByte(key, value);
    }

    public void setShort(String key, short value) {
        dataStorage.putShort(key, value);
    }

    public void setInt(String key, int value) {
        dataStorage.putInt(key, value);
    }

    public void setLong(String key, long value) {
        dataStorage.putLong(key, value);
    }

    public void setFloat(String key, float value) {
        dataStorage.putFloat(key, value);
    }

    public void setDouble(String key, double value) {
        dataStorage.putDouble(key, value);
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

    public void setLongArray(String key, long[] value) {
        dataStorage.putLongArray(key, value);
    }

    public void setLongArray(String key, List<Long> value) {
        dataStorage.putLongArray(key, value);
    }

    public Tag getTag(String key) {
        return dataStorage.get(key);
    }

    public byte getByte(String key) {
        return dataStorage.getByte(key);
    }

    public short getShort(String key) {
        return dataStorage.getShort(key);
    }

    public int getInt(String key) {
        return dataStorage.getInt(key);
    }

    public long getLong(String key) {
        return dataStorage.getLong(key);
    }

    public float getFloat(String key) {
        return dataStorage.getFloat(key);
    }

    public double getDouble(String key) {
        return dataStorage.getDouble(key);
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

    public long[] getLongArray(String key) {
        return dataStorage.getLongArray(key);
    }

    public CompoundTag getCompound(String key) {
        return dataStorage.getCompound(key);
    }

    public ListTag getList(String key, int type) {
        return dataStorage.getList(key, type);
    }
}
