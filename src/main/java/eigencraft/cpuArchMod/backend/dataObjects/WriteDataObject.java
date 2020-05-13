package eigencraft.cpuArchMod.backend.dataObjects;

import eigencraft.cpuArchMod.backend.AbstractDataObject;

public class WriteDataObject extends AbstractDataObject {
    public WriteDataObject(int address, byte[] data) {
        this.address = address;
        this.data = data;
    }

    private int address;
    private byte[] data;

    public int getAddress() {
        return address;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public DataObjectTypes getType() {
        return DataObjectTypes.WRITE;
    }
}
