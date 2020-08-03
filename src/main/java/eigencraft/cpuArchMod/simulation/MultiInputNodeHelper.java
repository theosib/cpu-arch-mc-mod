package eigencraft.cpuArchMod.simulation;


import java.util.HashMap;

public class MultiInputNodeHelper {
    HashMap<Integer,PipeMessage> buffer = new HashMap<>();
    HashMap<PipeLane,Integer> configuration = new HashMap<>();

    public HashMap<PipeLane, Integer> getConfiguration() {
        return configuration;
    }

    public void process(PipeMessage message){
        buffer.put(configuration.get(message.getLane()),message);
    }

    public void configure(PipeLane in,int slot){
        configuration.put(in, slot);
    }

    public PipeMessage getSlotMessage(int slot){
        return buffer.getOrDefault(slot,null);
    }

}
