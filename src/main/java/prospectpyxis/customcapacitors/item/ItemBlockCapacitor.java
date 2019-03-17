package prospectpyxis.customcapacitors.item;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import prospectpyxis.customcapacitors.CustomCapacitors;
import prospectpyxis.customcapacitors.ModConfig;
import prospectpyxis.customcapacitors.data.CapacitorData;
import prospectpyxis.customcapacitors.registry.CapacitorRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockCapacitor extends ItemBlock {

    public ItemBlockCapacitor(Block b) {
        super(b);

        setHasSubtypes(true);
        setUnlocalizedName(b.getUnlocalizedName());
        setRegistryName(b.getRegistryName());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (!stack.hasTagCompound()) {
            tooltip.add(I18n.format("tooltip.custom_capacitor.fallback.1"));
            tooltip.add(I18n.format("tooltip.custom_capacitor.fallback.2"));
            return;
        }
        CapacitorData data = CapacitorRegistry.getDataById(stack.getTagCompound().getCompoundTag("BlockEntityTag").getString("id"));
        if (data == null) data = new CapacitorData();

        if (!ModConfig.enableShiftTooltip) {
            if (data.description != null) {
                tooltip.add(data.description);
            }
        } else {
            if (!GuiScreen.isShiftKeyDown()) {
                if (data.description != null) {
                    tooltip.add(data.description);
                }
                tooltip.add(I18n.format("tooltip.custom_capacitor.shift", TextFormatting.ITALIC, TextFormatting.YELLOW, TextFormatting.RESET));
            } else {
                tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.capacity", TextFormatting.BOLD, TextFormatting.RESET, Integer.toString(data.capacity)));

            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (!stack.hasTagCompound()) return super.getUnlocalizedName(stack);
        CapacitorData data = CapacitorRegistry.getDataById(stack.getTagCompound().getCompoundTag("BlockEntityTag").getString("id"));
        if (data == null) return I18n.format("tile.customcapacitors.custom_capacitor.name");
        return data.displayName + " " + I18n.format("tile.customcapacitors.custom_capacitor.name");
    }
}
