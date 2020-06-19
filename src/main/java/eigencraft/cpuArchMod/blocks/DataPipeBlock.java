package eigencraft.cpuArchMod.blocks;

import eigencraft.cpuArchMod.CpuArchMod;
import eigencraft.cpuArchMod.backend.simulation.SimulationIOManager;
import eigencraft.cpuArchMod.backend.simulation.SimulationWorld;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class DataPipeBlock extends Block {
    public DataPipeBlock(Settings settings) {
        super(settings);
    }


    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!world.isClient){
            CpuArchMod.simulationMaster.getIOManager().addSimulationTickRunnable(new SimulationIOManager.SimulationTickRunnable() {
                @Override
                public void run(SimulationWorld simulationWorld) {
                    simulationWorld.removePipe(pos);
                }
            });
        }
        super.onBlockRemoved(state, world, pos, newState, moved);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient){
            CpuArchMod.simulationMaster.getIOManager().addSimulationTickRunnable(new SimulationIOManager.SimulationTickRunnable() {
                @Override
                public void run(SimulationWorld simulationWorld) {
                    simulationWorld.addPipe(pos);
                }
            });
        }
    }
}
