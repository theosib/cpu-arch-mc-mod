package eigencraft.cpuArchMod.simulation;

import eigencraft.cpuArchMod.dataObject.DataObject;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PipeMessage that = (PipeMessage) o;
        return dataObject.equals(that.dataObject) &&
                lane == that.lane;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataObject, lane);
    }
}
