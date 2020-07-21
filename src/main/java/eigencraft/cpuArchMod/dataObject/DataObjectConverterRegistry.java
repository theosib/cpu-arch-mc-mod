package eigencraft.cpuArchMod.dataObject;

import java.util.HashMap;

public class DataObjectConverterRegistry {
    private static final HashMap<DataObjectType,HashMap<DataObjectType, DataObjectConverter>> converters = new HashMap<>();

    public static void addConverter(DataObjectType in, DataObjectType out, DataObjectConverter converter){
        if (!converters.containsKey(out)) converters.put(out,new HashMap<>());
        converters.get(out).put(in,converter);
    }

    public static DataObject convert(DataObject in,DataObjectType target){
        if (in.matchType(target)){
            return in;
        } else if (converters.containsKey(target)){
            DataObjectType inType = DataObjectType.getDataObjectTypeFromNameOrNull(in.getType());
            if (converters.get(target).containsKey(inType)){
                return converters.get(target).get(inType).convert(in);
            }
        }
        return null;
    }
}
