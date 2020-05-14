package eigencraft.cpuArchMod;

import eigencraft.cpuArchMod.blocks.DebugBlock;
import eigencraft.cpuArchMod.blocks.DebugBlockContainer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class CpuArchMod implements ModInitializer {

	public static final Block DebugBlock = new DebugBlock(FabricBlockSettings.of(Material.METAL).build());


	@Override
	public void onInitialize() {
		//Todo
		System.out.println("Hello!");
		Registry.register(Registry.BLOCK, new Identifier("cpu_arch_mod","debug_block"), DebugBlock);
		ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier("cpu_arch_mod","debug_block"), (syncId, identifier, player, buf) -> {
			final World world = player.world;
			final BlockPos pos = buf.readBlockPos();
			return new DebugBlockContainer(syncId);
		});
	}
}
