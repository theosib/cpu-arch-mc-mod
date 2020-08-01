package eigencraft.cpuArchMod.simulation;

import eigencraft.cpuArchMod.dataObject.DataObject;

public class PipeMessage {
    public PipeMessage(DataObject dataObject, PipeLane lane) {
        this.dataObject = dataObject;
        this.lane = lane;
    }

    public DataObject getDataObject() {
        return dataObject;
    }

    public PipeLane getLane() {
        return lane;
    }

    private DataObject dataObject;
    private PipeLane lane;
}
