package eigencraft.cpuArchMod.items;


import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;


public class BlockStateItem extends Item {
    BlockState blockState;
    public BlockStateItem(BlockState block, Settings settings) {
        super( settings);
        blockState = block;
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        return this.place(new ItemPlacementContext(context));
    }

    private ActionResult place(ItemPlacementContext itemPlacementContext) {
        if (!itemPlacementContext.canPlace()) return ActionResult.FAIL;
        itemPlacementContext.getWorld().setBlockState(itemPlacementContext.getBlockPos(), blockState, 11);
        PlayerEntity player = itemPlacementContext.getPlayer();
        ItemStack stack = itemPlacementContext.getStack();
        assert player != null;

        blockState.getBlock().onPlaced(itemPlacementContext.getWorld(), itemPlacementContext.getBlockPos(), blockState, player, stack);

        if (!player.isCreative()){
            stack.decrement(1);
        }
        return ActionResult.SUCCESS;
    }

}
