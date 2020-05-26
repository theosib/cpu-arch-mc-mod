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

		DataObjectType dataObjectType = DataObjectType.create("test");
		dataObjectType.addTag("byte",NbtType.BYTE);
		dataObjectType.addTag("int",NbtType.INT);
		dataObjectType.addTag("string",NbtType.STRING);
		dataObjectType.addTag("ia",NbtType.INT_ARRAY);
		dataObjectType.addTag("ba",NbtType.BYTE_ARRAY);

		DataObject d = new DataObject(dataObjectType);
		d.setByte("byte",(byte)9);
		d.setInt("int",99);
		d.setString("string","999");
		d.setByteArray("ba", new byte[]{(byte)0, (byte)1, (byte)2});

		GsonBuilder gsonb = new GsonBuilder();
		gsonb.registerTypeAdapter(DataObject.class,new GsonDataObjectSerializer());
		gsonb.registerTypeAdapter(DataObject.class,new GsonDataObjectDeserializer());

		Gson gson = gsonb.create();
		String text = gson.toJson(d);
		System.out.println(text);

		DataObject deserialised = gson.fromJson(text,DataObject.class);
		System.out.println(deserialised.getInt("int"));
		System.out.println(deserialised.getByte("byte"));
		System.out.println(deserialised.getString("string"));
		System.out.println(deserialised.getByteArray("ba"));
		System.out.println(deserialised.getIntArray("ia"));
	}
}
