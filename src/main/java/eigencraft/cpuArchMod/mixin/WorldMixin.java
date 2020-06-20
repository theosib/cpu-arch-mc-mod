package eigencraft.cpuArchMod.mixin;

import eigencraft.cpuArchMod.backend.simulation.SimulationMaster;
import eigencraft.cpuArchMod.backend.simulation.SimulationMasterProvider;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(World.class)
public abstract class WorldMixin extends Object implements SimulationMasterProvider {
    @Shadow @Final public boolean isClient;

    @Shadow public abstract ChunkManager getChunkManager();

    private SimulationMaster simulationMaster;

    @Override
    public SimulationMaster getSimulationMaster() {
        return this.simulationMaster;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/LevelProperties;Lnet/minecraft/world/dimension/DimensionType;Ljava/util/function/BiFunction;Lnet/minecraft/util/profiler/Profiler;Z)V",at = @At("RETURN"))
    public void constructor(CallbackInfo ci){
        if (!isClient){
            simulationMaster = new SimulationMaster();
        }
    }

    @Inject(method = "close()V",at = @At("HEAD"))
    public void onClose(CallbackInfo ci){
        if (!isClient){
            this.simulationMaster.requestShutdown();
        }
    }
}