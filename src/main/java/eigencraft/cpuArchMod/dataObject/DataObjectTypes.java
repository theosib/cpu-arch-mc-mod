package eigencraft.cpuArchMod.dataObject;

public class DataObjectTypes {
    public static DataObjectType NULL_TYPE;

    public static void construct(){
        NULL_TYPE = DataObjectType.create("null");
    }
}
