package eigencraft.cpuArchMod.backend.dataObjects;

import eigencraft.cpuArchMod.backend.AbstractDataObject;

public class ReadResponseDataObject extends AbstractDataObject {
    byte[] data;

    public ReadResponseDataObject(byte[] data) {
        this.data = data;
    }

    @Override
    public DataObjectTypes getType() {
        return DataObjectTypes.READ_RESPONSE;
    }

    public byte[] getData() {
        return data;
    }
}
