package prospectpyxis.customcapacitors.item;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import prospectpyxis.customcapacitors.ModSettings;
import prospectpyxis.customcapacitors.data.CapacitorData;
import prospectpyxis.customcapacitors.registry.CapacitorRegistry;

import javax.annotation.Nullable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class ItemBlockCapacitor extends ItemBlock {

    public ItemBlockCapacitor(Block b) {
        super(b);

        setHasSubtypes(true);
        setUnlocalizedName(b.getUnlocalizedName());
        setRegistryName(b.getRegistryName());
    }

    /*
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!(entityIn instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entityIn;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {

        }
    }
    */

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (!stack.hasTagCompound()) {
            tooltip.add(I18n.format("tooltip.custom_capacitor.fallback.1"));
            tooltip.add(I18n.format("tooltip.custom_capacitor.fallback.2"));
            return;
        }
        CapacitorData data = CapacitorRegistry.getDataById(stack.getTagCompound().getCompoundTag("BlockEntityTag").getString("capid"));
        if (data == null) data = new CapacitorData();

        if (ModSettings.shiftTooltipDetailLevel == 0) {
            if (data.description != null) {
                tooltip.add(data.description);
            }
        } else {
            if (!GuiScreen.isShiftKeyDown()) {
                if (data.description != null) {
                    tooltip.add(data.description);
                }
                tooltip.add(I18n.format("tooltip.custom_capacitor.shift", TextFormatting.ITALIC, TextFormatting.YELLOW, TextFormatting.GRAY));
            } else {
                tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.capacity", TextFormatting.BOLD, TextFormatting.RESET, Integer.toString(data.capacity)));
                tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.inputrate", TextFormatting.BOLD, TextFormatting.RESET, Integer.toString(data.maxInputRate)));
                tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.outputrate", TextFormatting.BOLD, TextFormatting.RESET, Integer.toString(data.maxOutputRate)));

                DecimalFormat df = new DecimalFormat("#.###");
                df.setRoundingMode(RoundingMode.HALF_UP);

                if (ModSettings.shiftTooltipDetailLevel >= 2) {
                    switch (data.storageLossType) {
                        case NONE:
                            tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.storage_loss.none", TextFormatting.BOLD));
                            break;
                        case CONSTANT:
                            tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.storage_loss.constant", TextFormatting.BOLD));
                            if (ModSettings.shiftTooltipDetailLevel == 3) tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.storage_loss.constant.value",
                                    TextFormatting.BOLD, TextFormatting.RESET, Integer.toString((int) data.storageLossData.lossValue),
                                    data.storageLossData.lossDelay == 1 ? "" : Integer.toString(data.storageLossData.lossDelay)));
                            break;
                        case PERCENTAGE:
                            tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.storage_loss.percentage", TextFormatting.BOLD));
                            if (ModSettings.shiftTooltipDetailLevel == 3) tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.storage_loss.percentage.value",
                                    TextFormatting.BOLD, TextFormatting.RESET, df.format(data.storageLossData.lossValue * 100)));
                            break;
                        case PERCENTAGE_INVERTED:
                            tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.storage_loss.percentage_inverted", TextFormatting.BOLD));
                            if (ModSettings.shiftTooltipDetailLevel == 3) tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.storage_loss.percentage_inverted.value",
                                    TextFormatting.BOLD, TextFormatting.RESET, df.format(data.storageLossData.lossValue * 100)));
                            break;
                    }

                    if (ModSettings.shiftTooltipDetailLevel == 3) {
                        if (data.storageLossType != CapacitorData.EnumLossType.NONE) {
                            if (data.storageLossData.minThreshold != -1 && data.storageLossData.minThreshold != 0) {
                                tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.storage_loss.threshold.min",
                                        TextFormatting.BOLD, TextFormatting.RESET, Integer.toString(data.storageLossData.minThreshold)));
                            }
                            if (data.storageLossData.maxThreshold != -1 && data.storageLossData.maxThreshold < data.capacity) {
                                tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.storage_loss.threshold.max",
                                        TextFormatting.BOLD, TextFormatting.RESET, Integer.toString(data.storageLossData.maxThreshold)));
                            }
                        }
                    }

                    switch (data.inputLossType) {
                        case NONE:
                            tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.input_loss.none", TextFormatting.BOLD));
                            break;
                        case CONSTANT:
                            tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.input_loss.constant", TextFormatting.BOLD));
                            if (ModSettings.shiftTooltipDetailLevel == 3) tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.input_loss.constant.value",
                                    TextFormatting.BOLD, TextFormatting.RESET, Integer.toString((int)data.inputLossValue)));
                            break;
                        case PERCENTAGE:
                            tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.input_loss.percentage", TextFormatting.BOLD));
                            if (ModSettings.shiftTooltipDetailLevel == 3) tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.input_loss.percentage.value",
                                    TextFormatting.BOLD, TextFormatting.RESET, df.format(data.inputLossValue * 100)));
                            break;
                        case PERCENTAGE_INVERTED:
                            tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.input_loss.percentage_inverted", TextFormatting.BOLD));
                            if (ModSettings.shiftTooltipDetailLevel == 3) tooltip.add(I18n.format("tooltip.custom_capacitor.advanced.input_loss.percentage_inverted.value",
                                    TextFormatting.BOLD, TextFormatting.RESET, df.format(data.inputLossValue * 100)));
                            break;
                    }
                }

            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (!stack.hasTagCompound()) return super.getItemStackDisplayName(stack);
        CapacitorData data = CapacitorRegistry.getDataById(stack.getTagCompound().getCompoundTag("BlockEntityTag").getString("capid"));
        if (data == null) return I18n.format("tile.customcapacitors.custom_capacitor.name");
        return data.displayName + " " + I18n.format("tile.customcapacitors.custom_capacitor.name");
    }
}
