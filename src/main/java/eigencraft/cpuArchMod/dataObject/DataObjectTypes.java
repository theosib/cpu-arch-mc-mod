package eigencraft.cpuArchMod.dataObject;

import net.fabricmc.fabric.api.util.NbtType;

public class DataObjectTypes {
    public static DataObjectType NULL_TYPE;
    public static DataObjectType INT_TYPE;
    public static DataObjectType BYTE_TYPE;
    public static DataObjectType INT_ARRAY_TYPE;
    public static DataObjectType BYTE_ARRAY_TYPE;
    public static DataObjectType BOOLEAN_TYPE;

    public static DataObject TRUE_BOOLEAN;
    public static DataObject FALSE_BOOLEAN;

    public static void construct(){
        NULL_TYPE = DataObjectType.create("null");
        INT_TYPE = DataObjectType.create("integer");
        BYTE_TYPE = DataObjectType.create("byte");
        INT_ARRAY_TYPE = DataObjectType.create("integer_array");
        BYTE_ARRAY_TYPE = DataObjectType.create("byte_array");
        BOOLEAN_TYPE = DataObjectType.create("boolean");

        INT_TYPE.addTag("data", NbtType.INT);

        BYTE_TYPE.addTag("data",NbtType.BYTE);

        INT_ARRAY_TYPE.addTag("data",NbtType.INT_ARRAY);

        BYTE_ARRAY_TYPE.addTag("data",NbtType.BYTE_ARRAY);

        BOOLEAN_TYPE.addTag("bool",NbtType.BYTE);

        TRUE_BOOLEAN = new DataObject(BOOLEAN_TYPE);
        TRUE_BOOLEAN.setByte("bool",(byte)1);

        FALSE_BOOLEAN = new DataObject(BOOLEAN_TYPE);
        FALSE_BOOLEAN.setByte("bool",(byte)0);
    }

    public static boolean readBoolean(DataObject booleanDataObject){
        if (booleanDataObject.matchType(BOOLEAN_TYPE)){
            return booleanDataObject.getByte("bool")==1;
        }
        return false;
    }
}
