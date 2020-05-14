package eigencraft.cpuArchMod.backend.dataObjects;

import eigencraft.cpuArchMod.backend.AbstractDataObject;

public class ReadResponse extends AbstractDataObject {
    byte[] data;

    public ReadResponse(byte[] data) {
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
