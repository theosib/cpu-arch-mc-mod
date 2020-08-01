package eigencraft.cpuArchMod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eigencraft.cpuArchMod.blocks.ColoredPipeContainerBlock;
import eigencraft.cpuArchMod.blocks.PipeContainerBlock;
import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.dataObject.DataObjectType;
import eigencraft.cpuArchMod.dataObject.DataObjectTypes;
import eigencraft.cpuArchMod.simulation.*;
import eigencraft.cpuArchMod.blocks.NodeContainerBlock;
import eigencraft.cpuArchMod.items.DebugDataObjectItem;
import eigencraft.cpuArchMod.simulation.nodes.*;
import eigencraft.cpuArchMod.simulation.pipes.EndPipe;
import eigencraft.cpuArchMod.simulation.pipes.TransferPipe;
import eigencraft.cpuArchMod.util.GsonDataObjectDeserializer;
import eigencraft.cpuArchMod.util.GsonDataObjectSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.LinkedList;

public class CpuArchMod implements ModInitializer, ClientModInitializer {

	public static final String MODID = "cpu_arch_mod";

	public static final Identifier DATAOBJECT_GUI_SAVE_C2S_PACKET = new Identifier(MODID,"dataobject.save_on_item");

	public static final ItemGroup CPU_ARCH_MOD_ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MODID,"mod_creative_item_group")).build();

	public static final Item DEBUG_DATA_OBJECT_ITEM = new DebugDataObjectItem();

	//public static final Block DATA_PIPE_BLOCK = new PipeContainerBlock(FabricBlockSettings.of(Material.METAL).breakByHand(true).hardness((float)Math.PI).build());
	//public static final Block DATA_PIPE_END_BLOCK = new DataPipeEndBlock(FabricBlockSettings.of(Material.METAL).breakByHand(true).hardness((float)Math.PI).build());

	public static Gson GSON;
	

	@Override
	public void onInitialize() {

		//Initialize important non-generic dataObjectTypes
		DataObjectTypes.construct();

		//Initialize gson
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(DataObject.class,new GsonDataObjectSerializer());
		gsonBuilder.registerTypeAdapter(DataObject.class,new GsonDataObjectDeserializer());
		GSON = gsonBuilder.create();

		//We need to start the simulation thread for every dimension
		ServerStartCallback.EVENT.register(new ServerStartCallback() {
			@Override
			public void onStartServer(MinecraftServer minecraftServer) {
				for (World world:minecraftServer.getWorlds()){
					((SimulationMasterProvider)world).getSimulationMaster().launchSimulationWorld(world);
				}
			}
		});

		//We need to shutdown all simulation thread when the server stops
		ServerStopCallback.EVENT.register(new ServerStopCallback() {
			@Override
			public void onStopServer(MinecraftServer minecraftServer) {
				for (World world:minecraftServer.getWorlds()){
					((SimulationMasterProvider)world).getSimulationMaster().requestShutdown();
				}
			}
		});

		//Interface between simulation thread and game thread, executed for every dimension
		ServerTickCallback.EVENT.register(new ServerTickCallback() {
			@Override
			public void tick(MinecraftServer minecraftServer) {
				for (World world:minecraftServer.getWorlds()){
					SimulationMaster worldSimulationMaster = ((SimulationMasterProvider)world).getSimulationMaster();
					LinkedList<SimulationIOManager.MinecraftServerRunnable> events = worldSimulationMaster.getIOManager().getMainThreadQueue();
					while (!events.isEmpty()){
						events.remove().run(minecraftServer);
					}
				}
			}
		});

		//Register items
		Registry.register(Registry.ITEM, new Identifier(MODID, "data_object_disk_item"), DEBUG_DATA_OBJECT_ITEM);

		//Register nodes
		NodeContainerBlock.create(IONode.class,IONode::new);
		NodeContainerBlock.create(IntConverterNode.class, IntConverterNode::new);
		NodeContainerBlock.create(ByteConverterNode.class, ByteConverterNode::new);
		NodeContainerBlock.create(IntArrayConverterNode.class, IntArrayConverterNode::new);
		NodeContainerBlock.create(ByteArrayConverterNode.class, ByteArrayConverterNode::new);
		NodeContainerBlock.create(BoolConverterNode.class, BoolConverterNode::new);

		//Registaer pipes
		ColoredPipeContainerBlock.create(TransferPipe.class, TransferPipe::new);
		PipeContainerBlock.create(EndPipe.class, EndPipe::new);


		//Register packages
		ServerSidePacketRegistry.INSTANCE.register(DATAOBJECT_GUI_SAVE_C2S_PACKET, (packetContext, attachedData) -> {
			// Get the BlockPos we put earlier in the IO thread
			CompoundTag rawDataObject = attachedData.readCompoundTag();
			DataObject dataObject;
			try {
				dataObject = new DataObject(rawDataObject);
			} catch (DataObjectType.UnknownDataObjectTypeException e) {
				//Invalid dataObject, do nothing
				return;
			}
			packetContext.getTaskQueue().execute(() -> {
				// Execute on the main thread
				ItemStack mainHandStack = packetContext.getPlayer().inventory.getMainHandStack();
				if (mainHandStack.getItem() instanceof DebugDataObjectItem){
					mainHandStack.putSubTag("dataObject", dataObject.getCompoundTag());
				}
			});
		});
	}

	@Override
	public void onInitializeClient() {
	}
}
