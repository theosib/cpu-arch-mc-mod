package eigencraft.cpuArchMod.backend.simulation;

import net.minecraft.server.MinecraftServer;

import java.util.LinkedList;

public class SimulationIOManager {
    private LinkedList<MinecraftServerRunnable> scheduledMainThreadEvents = new LinkedList<>();
    private LinkedList<SimulationTickRunnable> scheduledSimulationThreadEvents = new LinkedList<>();

    public void addMainTickRunnable(MinecraftServerRunnable r){
        scheduledMainThreadEvents.add(r);
    }

    public void addSimulationTickRunnable(SimulationTickRunnable r){
        scheduledSimulationThreadEvents.add(r);
    }

    public LinkedList<MinecraftServerRunnable> getMainThreadQueue(){
        return scheduledMainThreadEvents;
    }

    public LinkedList<SimulationTickRunnable> getSimulationThreadQueue(){
        return scheduledSimulationThreadEvents;
    }

    public interface MinecraftServerRunnable{
        public void run(MinecraftServer server);
    }

    public interface SimulationTickRunnable{
        public void run(SimulationWorld simulationWorld);
    }
}
