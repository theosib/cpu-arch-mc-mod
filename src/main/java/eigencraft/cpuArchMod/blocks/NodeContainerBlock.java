package eigencraft.cpuArchMod.blocks;

import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.dataObject.DataObjectType;
import eigencraft.cpuArchMod.simulation.SimulationIOManager;
import eigencraft.cpuArchMod.simulation.SimulationMasterProvider;
import eigencraft.cpuArchMod.simulation.SimulationNode;
import eigencraft.cpuArchMod.simulation.SimulationWorld;
import eigencraft.cpuArchMod.items.DebugDataObjectItem;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.function.Function;

import static eigencraft.cpuArchMod.CpuArchMod.CPU_ARCH_MOD_ITEM_GROUP;
import static eigencraft.cpuArchMod.CpuArchMod.MODID;

public class NodeContainerBlock extends Block {
    private static final Settings blockSettings = FabricBlockSettings.of(Material.METAL).breakByHand(true).hardness((float)Math.PI).build();

    //To create a new instance
    private Function<BlockPos,SimulationNode> constructor;

    /***
     * Factory function for a simulation node. Registers a block, an item and the simulationNode
     * @param type node.class
     * @param constructor node::new
     */
    public static void create(Class type,Function<BlockPos,SimulationNode> constructor){
        SimulationNode.register(type.getSimpleName(),constructor);
        NodeContainerBlock newNodeContainerBlock = new NodeContainerBlock(blockSettings,constructor);
        Registry.register(Registry.ITEM, new Identifier(MODID, type.getSimpleName().toLowerCase()), new BlockItem(newNodeContainerBlock, new Item.Settings().group(CPU_ARCH_MOD_ITEM_GROUP)));
        Registry.register(Registry.BLOCK,new Identifier(MODID,type.getSimpleName().toLowerCase()),newNodeContainerBlock);
    }

    //private constructor, used from create
    private NodeContainerBlock(Settings settings, Function<BlockPos,SimulationNode> constructor) {
        super(settings);
        this.constructor = constructor;
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        //Simulation is serverside
        if (!world.isClient()){
            //Go to simulation thread
            ((SimulationMasterProvider)world).getSimulationMaster().getIOManager().addSimulationTickRunnable(new SimulationIOManager.SimulationTickRunnable() {
                @Override
                public void run(SimulationWorld simulationWorld) {
                    //remove the node
                    simulationWorld.removeNode(pos);
                }
            });
        }
        super.onBroken(world,pos,state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        //Simulation is serverside
        if (!world.isClient){
            //Go to simulation thread
            ((SimulationMasterProvider)world).getSimulationMaster().getIOManager().addSimulationTickRunnable(new SimulationIOManager.SimulationTickRunnable() {
                @Override
                public void run(SimulationWorld simulationWorld) {
                    //Create a new node instance and place it in the simulation world
                    simulationWorld.addNode(constructor.apply(pos),pos);
                }
            });
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hand.equals(Hand.MAIN_HAND)){
            ItemStack mainHandStack = player.inventory.getMainHandStack();
            if (mainHandStack.getItem() instanceof DebugDataObjectItem){
                //Direct input
                CompoundTag dataObjectNbt = mainHandStack.getSubTag("dataObject");
                if (dataObjectNbt != null){
                    try {
                        DataObject dataObject = new DataObject(dataObjectNbt);
                        if (!world.isClient){
                            //Submit to simulation thread
                            ((SimulationMasterProvider)world).getSimulationMaster().getIOManager().addSimulationTickRunnable(new SimulationIOManager.SimulationTickRunnable() {
                                @Override
                                public void run(SimulationWorld simulationWorld) {
                                    SimulationNode node = simulationWorld.getOrLoadChunk(new ChunkPos(pos)).getNodeAt(pos);
                                    if (node==null) return;
                                    node.processDirectInput(dataObject, simulationWorld.getIoManager());
                                }
                            });
                            mainHandStack.decrement(1);
                        }
                        return ActionResult.SUCCESS;
                    } catch (DataObjectType.UnknownDataObjectTypeException e) {
                    }
                }
            } else {
                if (!world.isClient){
                    //Trigger use
                    ((SimulationMasterProvider) world).getSimulationMaster().getIOManager().addSimulationTickRunnable(new SimulationIOManager.SimulationTickRunnable() {
                        @Override
                        public void run(SimulationWorld simulationWorld) {
                            SimulationNode node = simulationWorld.getOrLoadChunk(new ChunkPos(pos)).getNodeAt(pos);
                            if (node != null){
                                node.onUse(((SimulationMasterProvider) world).getSimulationMaster().getIOManager(), player);
                            }
                        }
                    });
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.PASS;
    }
}
