package eigencraft.cpuArchMod.mixin;

import eigencraft.cpuArchMod.simulation.SimulationMaster;
import eigencraft.cpuArchMod.simulation.SimulationMasterProvider;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Spawner;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;


@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends Object implements SimulationMasterProvider {

    private SimulationMaster simulationMaster;

    @Override
    public SimulationMaster getSimulationMaster() {
        return this.simulationMaster;
    }

    @Inject(method = "<init>(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/world/level/ServerWorldProperties;Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/world/dimension/DimensionType;Lnet/minecraft/server/WorldGenerationProgressListener;Lnet/minecraft/world/gen/chunk/ChunkGenerator;ZJLjava/util/List;Z)V",at = @At("RETURN"))
    public void constructor(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> registryKey, RegistryKey<DimensionType> registryKey2, DimensionType dimensionType, WorldGenerationProgressListener generationProgressListener, ChunkGenerator chunkGenerator, boolean bl, long l, List<Spawner> list, boolean bl2, CallbackInfo ci){
        System.out.println(session.getDirectoryName());
        simulationMaster = new SimulationMaster(new File(session.method_27424(registryKey),"cpu_sim"));
        simulationMaster.launchSimulationWorld((World)(Object)this);
    }
}