package eigencraft.cpuArchMod.blocks;

import eigencraft.cpuArchMod.items.BlockStateItem;
import eigencraft.cpuArchMod.simulation.*;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.function.Function;

import static eigencraft.cpuArchMod.CpuArchMod.CPU_ARCH_MOD_ITEM_GROUP;
import static eigencraft.cpuArchMod.CpuArchMod.MODID;

public class ColoredPipeContainerBlock extends PipeContainerBlock {
    public static final EnumProperty<DyeColor> COLOR_PROPERTY = EnumProperty.of("color", DyeColor.class);

    public static void create(Class type, Function<BlockPos, SimulationPipe> constructor) {
        SimulationPipe.register(type.getSimpleName(), constructor);
        //Registry is handled in the constructor
        ColoredPipeContainerBlock newNodeContainerBlock = new ColoredPipeContainerBlock(blockSettings, constructor, type);

        //TODO only on client
        ColorProviderRegistry.BLOCK.register(new BlockColorProvider() {
            @Override
            public int getColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex) {
                return state.get(COLOR_PROPERTY).getMaterialColor().color;
            }
        }, newNodeContainerBlock);
    }

    private ColoredPipeContainerBlock(Settings settings, Function<BlockPos, SimulationPipe> constructor, Class type) {
        super(settings, constructor);
        Registry.register(Registry.BLOCK, new Identifier(MODID, type.getSimpleName().toLowerCase()), this);

        for (BlockState state : this.getStateManager().getStates()) {
            String itemName = String.format("%s_%s", type.getSimpleName().toLowerCase(), state.get(COLOR_PROPERTY).asString());
            System.out.println(itemName);
            Registry.register(Registry.ITEM, new Identifier(MODID, itemName), new BlockStateItem(state, new Item.Settings().group(CPU_ARCH_MOD_ITEM_GROUP)));
        }

        setDefaultState(getStateManager().getDefaultState().with(COLOR_PROPERTY, DyeColor.WHITE));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(COLOR_PROPERTY);
    }
}