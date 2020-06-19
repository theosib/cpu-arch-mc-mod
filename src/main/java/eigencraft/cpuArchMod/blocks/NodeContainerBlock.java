package eigencraft.cpuArchMod.blocks;

import eigencraft.cpuArchMod.CpuArchMod;
import eigencraft.cpuArchMod.backend.dataObject.DataObject;
import eigencraft.cpuArchMod.backend.dataObject.DataObjectType;
import eigencraft.cpuArchMod.backend.simulation.SimulationIOManager;
import eigencraft.cpuArchMod.backend.simulation.SimulationMasterProvider;
import eigencraft.cpuArchMod.backend.simulation.SimulationNode;
import eigencraft.cpuArchMod.backend.simulation.SimulationWorld;
import eigencraft.cpuArchMod.items.DebugDataObjectItem;
import eigencraft.cpuArchMod.simulationNode.IONode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.function.Function;

public class NodeContainerBlock extends Block {
    private Function<BlockPos,SimulationNode> constructor;

    public NodeContainerBlock(Settings settings, Function<BlockPos,SimulationNode> constructor) {
        super(settings);
        this.constructor = constructor;
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!world.isClient){
            ((SimulationMasterProvider)world).getSimulationMaster().getIOManager().addSimulationTickRunnable(new SimulationIOManager.SimulationTickRunnable() {
                @Override
                public void run(SimulationWorld simulationWorld) {
                    simulationWorld.removeNode(pos);
                }
            });
        }
        super.onBlockRemoved(state, world, pos, newState, moved);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient){
            ((SimulationMasterProvider)world).getSimulationMaster().getIOManager().addSimulationTickRunnable(new SimulationIOManager.SimulationTickRunnable() {
                @Override
                public void run(SimulationWorld simulationWorld) {
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
            }
        }

        return ActionResult.PASS;
    }
}
