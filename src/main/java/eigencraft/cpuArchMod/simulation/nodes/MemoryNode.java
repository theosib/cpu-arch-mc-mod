package eigencraft.cpuArchMod.simulation.nodes;

import eigencraft.cpuArchMod.CpuArchMod;
import eigencraft.cpuArchMod.simulation.*;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class MemoryNode extends SimulationNode {
    int[] data = new int[256];
    MultiInputNodeHelper multiInputHelper = new MultiInputNodeHelper();

    public MemoryNode(BlockPos position) {
        super(position);
        multiInputHelper.configure(PipeLane.WHITE,0); //Read
        multiInputHelper.configure(PipeLane.ORANGE,1); //Write address
        multiInputHelper.configure(PipeLane.MAGENTA,2); //Write data
    }

    @Override
    public void process(PipeMessage message, SimulationIOManager ioManager) {

    }

    @Override
    public void onUse(SimulationIOManager ioManager, PlayerEntity player) {
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeBlockPos(position);
        passedData.writeString(CpuArchMod.GSON.toJson(multiInputHelper));
        ioManager.addMainTickRunnable(new SimulationIOManager.MinecraftServerRunnable() {
            @Override
            public void run(MinecraftServer server) {
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CpuArchMod.INPUT_CONFIGURATION_OPEN_GUI_S2C_PACKET,passedData);
            }
        });
    }
}
