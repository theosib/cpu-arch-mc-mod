package eigencraft.cpuArchMod.mixin;

import net.minecraft.world.WorldSaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldSaveHandler.class)
public class WorldSaveHandlerMixin {
    @Inject(method = "checkSessionLock()V",at = @At("HEAD"), cancellable = true)
    private void blockLockCheck(CallbackInfo ci){
        ci.cancel();
    }
}
