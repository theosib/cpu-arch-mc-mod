package eigencraft.cpuArchMod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eigencraft.cpuArchMod.backend.DataObject;
import eigencraft.cpuArchMod.backend.DataObjectType;
import eigencraft.cpuArchMod.items.DebugDataObjectItem;
import eigencraft.cpuArchMod.util.GsonDataObjectDeserializer;
import eigencraft.cpuArchMod.util.GsonDataObjectSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CpuArchMod implements ModInitializer {

	public static final String MODID = "cpu_arch_mod";

	public static final Identifier DATAOBJECT_GUI_SAVE_C2S_PACKET = new Identifier(MODID,"dataobject.save_on_item");

	public static final ItemGroup CpuArchModItemGroup = FabricItemGroupBuilder.create(new Identifier(MODID,"mod_creative_item_group")).build();

	public static final Item debugDataObjectItem = new DebugDataObjectItem();
	

	@Override
	public void onInitialize() {
		//Todo
		Registry.register(Registry.ITEM, new Identifier(MODID, "data_object_debug_item"), debugDataObjectItem);

		ServerSidePacketRegistry.INSTANCE.register(DATAOBJECT_GUI_SAVE_C2S_PACKET, (packetContext, attachedData) -> {
			// Get the BlockPos we put earlier in the IO thread
			CompoundTag rawDataObject = attachedData.readCompoundTag();
			DataObject dataObject;
			try {
				dataObject = new DataObject(rawDataObject);
			} catch (DataObjectType.UnknownDataObjectTypeException e) {
				return;
			}
			packetContext.getTaskQueue().execute(() -> {
				// Execute on the main thread
				ItemStack mainHandStack = packetContext.getPlayer().inventory.getMainHandStack();
				//TODO validate item type
				mainHandStack.putSubTag("dataObject",dataObject.getCompoundTag());
			});
		});

		DataObjectType dataObjectType = DataObjectType.create("test");
		dataObjectType.addTag("byte",NbtType.BYTE);
		dataObjectType.addTag("int",NbtType.INT);
		dataObjectType.addTag("string",NbtType.STRING);
		dataObjectType.addTag("ia",NbtType.INT_ARRAY);
		dataObjectType.addTag("ba",NbtType.BYTE_ARRAY);
		
	}
}
