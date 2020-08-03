package eigencraft.cpuArchMod.simulation;

import java.util.Collection;

public interface SimulationMessageProvidingPipe extends SimulationPipe{
    public Collection<PipeMessage> getNewMessages();
}
