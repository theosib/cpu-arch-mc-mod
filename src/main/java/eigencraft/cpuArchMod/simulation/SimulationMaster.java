package eigencraft.cpuArchMod.simulation;

import net.minecraft.world.World;

import java.io.File;

public class SimulationMaster implements Runnable{
    private SimulationWorld world;
    private Thread simulationExecutor;
    private boolean simulationRunning = true;
    private int tickCounter = 0;
    File savePath;

    public SimulationMaster(File savePath){
        this.savePath = savePath;
    }

    public void launchSimulationWorld(World gameWorld){
        savePath.mkdirs();
        world = new SimulationWorld(savePath, gameWorld);
        simulationExecutor = new Thread(this,String.format("cpu_arch_mod %s",gameWorld.getDimensionRegistryKey().getValue().getPath()));

        //Always last
        simulationExecutor.start();
    }

    public void requestShutdown(){
        simulationRunning = false;
    }

    public SimulationIOManager getIOManager(){
        return world.getIoManager();
    }

    @Override
    public void run() {
        while(simulationRunning){
            world.tick();

            //TODO proper tps stabiliser
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            tickCounter++;
            if (tickCounter%1200==0){
                world.saveAll();
            }
        }
        world.saveAll();
    }
}
