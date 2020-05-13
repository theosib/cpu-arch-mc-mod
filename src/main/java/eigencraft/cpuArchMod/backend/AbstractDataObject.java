package eigencraft.cpuArchMod.backend;

import com.google.gson.Gson;

public abstract class AbstractDataObject {
    private transient Gson gson = new Gson();
    public static enum DataObjectTypes{
        READ,
        WRITE,
        READ_RESPONSE,
        WRITE_ACK
    }

    /***
     * if you get a dataObject as an input, check with something like a switch statement for the type to process reads and writes different.
     * @return The type of data object.
     * ***/
    public abstract DataObjectTypes getType();

    public String toJson(){
        return gson.toJson(this);
    }
}
