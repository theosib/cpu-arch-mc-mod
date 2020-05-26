package eigencraft.cpuArchMod;

import eigencraft.cpuArchMod.backend.DataObject;
import eigencraft.cpuArchMod.backend.DataObjectType;
import eigencraft.cpuArchMod.items.DebugDataObjectItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CpuArchMod implements ModInitializer {

	public static final String MODID = "cpu_arch_mod";

	public static final ItemGroup CpuArchModItemGroup = FabricItemGroupBuilder.create(new Identifier(MODID,"mod_creative_item_group")).build();

	public static final Item debugDataObjectItem = new DebugDataObjectItem();
	

	@Override
	public void onInitialize() {
		//Todo
		Registry.register(Registry.ITEM, new Identifier(MODID, "data_object_debug_item"), debugDataObjectItem);
	}
}
