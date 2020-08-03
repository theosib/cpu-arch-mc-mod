package eigencraft.cpuArchMod.blocks;

import eigencraft.cpuArchMod.simulation.*;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.function.Function;

import static eigencraft.cpuArchMod.CpuArchMod.CPU_ARCH_MOD_ITEM_GROUP;
import static eigencraft.cpuArchMod.CpuArchMod.MODID;

public class PipeContainerBlock extends HorizontalConnectingBlock implements CpuArchModBlock {
    protected static final Settings blockSettings = FabricBlockSettings.of(Material.METAL).breakByHand(true).hardness((float)Math.PI).build();


    protected Function<SimulationPipeContext,SimulationPipe> constructor;

    public static void create(Class type, Function<SimulationPipeContext, SimulationPipe> constructor){
        SimulationPipe.register(type.getSimpleName(),constructor);
        PipeContainerBlock newPipeContainerBlock = new PipeContainerBlock(blockSettings,constructor);
        Registry.register(Registry.ITEM, new Identifier(MODID, type.getSimpleName().toLowerCase()), new BlockItem(newPipeContainerBlock, new Item.Settings().group(CPU_ARCH_MOD_ITEM_GROUP)));
        Registry.register(Registry.BLOCK,new Identifier(MODID,type.getSimpleName().toLowerCase()),newPipeContainerBlock);
    }

    protected PipeContainerBlock(Settings settings, Function<SimulationPipeContext, SimulationPipe> constructor) {
        super(1.0F, 1.0F, 16.0F, 16.0F, 16.0F,settings);
        this.constructor = constructor;
    }

    protected SimulationPipeContext buildContext(BlockState state,BlockPos pos){
        return new SimulationPipeContext().setPosition(pos);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        //Simulation is only server side
        if (!world.isClient()){
            //Run it in the simulation thread
            ((SimulationMasterProvider)world).getSimulationMaster().getIOManager().addSimulationTickRunnable(new SimulationIOManager.SimulationTickRunnable() {
                @Override
                public void run(SimulationWorld simulationWorld) {
                    //In the simulation thread, remove the pipe
                    simulationWorld.removePipe(pos);
                }
            });
        }
        super.onBroken(world,pos,state);
    }

    public boolean canConnect(BlockState state, boolean neighborIsFullSquare, Direction dir) {
        Block block = state.getBlock();
        boolean x = (!cannotConnect(block)) && (block instanceof CpuArchModBlock);
        System.out.println(x);
        return x;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockView blockView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        BlockPos blockPos2 = blockPos.north();
        BlockPos blockPos3 = blockPos.east();
        BlockPos blockPos4 = blockPos.south();
        BlockPos blockPos5 = blockPos.west();
        BlockState blockState = blockView.getBlockState(blockPos2);
        BlockState blockState2 = blockView.getBlockState(blockPos3);
        BlockState blockState3 = blockView.getBlockState(blockPos4);
        BlockState blockState4 = blockView.getBlockState(blockPos5);
        return ((BlockState)((BlockState)((BlockState)super.getPlacementState(ctx).with(NORTH, this.canConnect(blockState, blockState.isSideSolidFullSquare(blockView, blockPos2, Direction.SOUTH), Direction.SOUTH))).with(EAST, this.canConnect(blockState2, blockState2.isSideSolidFullSquare(blockView, blockPos3, Direction.WEST), Direction.WEST))).with(SOUTH, this.canConnect(blockState3, blockState3.isSideSolidFullSquare(blockView, blockPos4, Direction.NORTH), Direction.NORTH))).with(WEST, this.canConnect(blockState4, blockState4.isSideSolidFullSquare(blockView, blockPos5, Direction.EAST), Direction.EAST)).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        //Simulation is only server side
        if (!world.isClient){
            //Run it in the simulation thread
            ((SimulationMasterProvider)world).getSimulationMaster().getIOManager().addSimulationTickRunnable(new SimulationIOManager.SimulationTickRunnable() {
                @Override
                public void run(SimulationWorld simulationWorld) {
                    //In the simulation thread, add the pipe
                    simulationWorld.addPipe(constructor.apply(buildContext(state,pos)),pos);
                }
            });
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if ((Boolean)state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return direction.getAxis().getType() == Direction.Type.HORIZONTAL ? (BlockState)state.with((Property)FACING_PROPERTIES.get(direction), this.canConnect(newState, newState.isSideSolidFullSquare(world, posFrom, direction.getOpposite()), direction.getOpposite())) : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }
}