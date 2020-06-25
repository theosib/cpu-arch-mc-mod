package eigencraft.cpuArchMod.items;

import eigencraft.cpuArchMod.CpuArchMod;
import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.dataObject.DataObjectType;
import eigencraft.cpuArchMod.gui.DataObjectEditGUI;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DebugDataObjectItem extends Item {
    public DebugDataObjectItem() {
        super(new Settings().group(CpuArchMod.CPU_ARCH_MOD_ITEM_GROUP));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()){
            CompoundTag rawDataObject = user.inventory.getMainHandStack().getSubTag("dataObject");
            try {
                DataObject dataObject = new DataObject(rawDataObject);
                MinecraftClient.getInstance().openScreen(new CottonClientScreen(DataObjectEditGUI.fromExisting(dataObject)));
            } catch (DataObjectType.UnknownDataObjectTypeException e) {
                MinecraftClient.getInstance().openScreen(new CottonClientScreen(DataObjectEditGUI.fromNew()));
            }
        }
        return super.use(world, user, hand);
    }
}
