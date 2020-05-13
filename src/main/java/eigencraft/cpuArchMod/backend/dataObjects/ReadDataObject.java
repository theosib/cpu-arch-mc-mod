package eigencraft.cpuArchMod.backend.dataObjects;

import eigencraft.cpuArchMod.backend.AbstractDataObject;

public class ReadDataObject extends AbstractDataObject {

    int requestedAddress;

    public ReadDataObject(int requestedAddress) {
        this.requestedAddress = requestedAddress;
    }

    public int getRequestedAddress() {
        return requestedAddress;
    }

    @Override
    public DataObjectTypes getType() {
        return DataObjectTypes.READ;
    }
}
