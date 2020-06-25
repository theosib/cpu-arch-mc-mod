package eigencraft.cpuArchMod;

import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.dataObject.DataObjectType;
import eigencraft.cpuArchMod.simulation.SimulationIOManager;
import eigencraft.cpuArchMod.simulation.SimulationMaster;
import eigencraft.cpuArchMod.simulation.SimulationMasterProvider;
import eigencraft.cpuArchMod.blocks.DataPipeBlock;
import eigencraft.cpuArchMod.blocks.NodeContainerBlock;
import eigencraft.cpuArchMod.items.DebugDataObjectItem;
import eigencraft.cpuArchMod.simulation.nodes.IONode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.LinkedList;

public class CpuArchMod implements ModInitializer {

	public static final String MODID = "cpu_arch_mod";

	public static final Identifier DATAOBJECT_GUI_SAVE_C2S_PACKET = new Identifier(MODID,"dataobject.save_on_item");

	public static final ItemGroup CPU_ARCH_MOD_ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MODID,"mod_creative_item_group")).build();

	public static final Item DEBUG_DATA_OBJECT_ITEM = new DebugDataObjectItem();

	public static final Block DATA_PIPE_BLOCK = new DataPipeBlock(FabricBlockSettings.of(Material.METAL).breakByHand(true).hardness((float)Math.PI).build());

	@Environment(EnvType.SERVER)
	public static MinecraftServer mcServer;
	

	@Override
	public void onInitialize() {
		ServerStartCallback.EVENT.register(new ServerStartCallback() {
			@Override
			public void onStartServer(MinecraftServer minecraftServer) {
				for (World world:minecraftServer.getWorlds()){
					if (!world.isClient){
						((SimulationMasterProvider)world).getSimulationMaster().launchSimulationWorld(world);
					}
				}
			}
		});

		ServerTickCallback.EVENT.register(new ServerTickCallback() {
			@Override
			public void tick(MinecraftServer minecraftServer) {
				//minecraftServer.getWo
				//Interface between simulation thread and game thread, executed for every dimension
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
		Registry.register(Registry.ITEM, new Identifier(MODID, "data_pipe_item"), new BlockItem(DATA_PIPE_BLOCK, new Item.Settings().group(CPU_ARCH_MOD_ITEM_GROUP)));

		//Register blocks
		Registry.register(Registry.BLOCK,new Identifier(MODID,"data_pipe"),DATA_PIPE_BLOCK);

		//Register nodes
		NodeContainerBlock.create(IONode.class,IONode::new);


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

		//Test dataType
		DataObjectType dataObjectType = DataObjectType.create("test");
		dataObjectType.addTag("int",NbtType.INT);
		dataObjectType.addTag("string",NbtType.STRING);

	}
}
