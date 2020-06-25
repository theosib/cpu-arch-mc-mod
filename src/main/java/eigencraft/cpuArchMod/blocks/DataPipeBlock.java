package eigencraft.cpuArchMod.blocks;

import eigencraft.cpuArchMod.simulation.SimulationIOManager;
import eigencraft.cpuArchMod.simulation.SimulationMasterProvider;
import eigencraft.cpuArchMod.simulation.SimulationWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class DataPipeBlock extends Block {
    public DataPipeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (!world.isClient()){
            ((SimulationMasterProvider)world).getSimulationMaster().getIOManager().addSimulationTickRunnable(new SimulationIOManager.SimulationTickRunnable() {
                @Override
                public void run(SimulationWorld simulationWorld) {
                    simulationWorld.removePipe(pos);
                }
            });
        }
        super.onBroken(world,pos,state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {

        if (!world.isClient){
            ((SimulationMasterProvider)world).getSimulationMaster().getIOManager().addSimulationTickRunnable(new SimulationIOManager.SimulationTickRunnable() {
                @Override
                public void run(SimulationWorld simulationWorld) {
                    simulationWorld.addPipe(pos);
                }
            });
        }
    }
}
