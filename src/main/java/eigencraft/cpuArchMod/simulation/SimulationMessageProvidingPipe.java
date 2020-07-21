package eigencraft.cpuArchMod.simulation;

import eigencraft.cpuArchMod.dataObject.DataObject;

import java.util.Collection;

public interface SimulationMessageProvidingPipe extends SimulationPipe{
    public Collection<DataObject> getNewMessages();
}
