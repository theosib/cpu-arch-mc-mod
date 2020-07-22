package eigencraft.cpuArchMod.blocks;

import eigencraft.cpuArchMod.simulation.*;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.function.Function;

import static eigencraft.cpuArchMod.CpuArchMod.CPU_ARCH_MOD_ITEM_GROUP;
import static eigencraft.cpuArchMod.CpuArchMod.MODID;

public class PipeContainerBlock extends Block {
    private static final Settings blockSettings = FabricBlockSettings.of(Material.METAL).breakByHand(true).hardness((float)Math.PI).build();

    private Function<BlockPos,SimulationPipe> constructor;

    public static void create(Class type, Function<BlockPos, SimulationPipe> constructor){
        SimulationPipe.register(type.getSimpleName(),constructor);
        PipeContainerBlock newNodeContainerBlock = new PipeContainerBlock(blockSettings,constructor);
        Registry.register(Registry.ITEM, new Identifier(MODID, type.getSimpleName().toLowerCase()), new BlockItem(newNodeContainerBlock, new Item.Settings().group(CPU_ARCH_MOD_ITEM_GROUP)));
        Registry.register(Registry.BLOCK,new Identifier(MODID,type.getSimpleName().toLowerCase()),newNodeContainerBlock);
    }

    private PipeContainerBlock(Settings settings, Function<BlockPos, SimulationPipe> constructor) {
        super(settings);
        this.constructor = constructor;
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

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        //Simulation is only server side
        if (!world.isClient){
            //Run it in the simulation thread
            ((SimulationMasterProvider)world).getSimulationMaster().getIOManager().addSimulationTickRunnable(new SimulationIOManager.SimulationTickRunnable() {
                @Override
                public void run(SimulationWorld simulationWorld) {
                    //In the simulation thread, add the pipe
                    simulationWorld.addPipe(constructor.apply(pos),pos);
                }
            });
        }
    }
}
