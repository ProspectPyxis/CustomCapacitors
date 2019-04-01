package prospectpyxis.customcapacitors.item;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import prospectpyxis.customcapacitors.CustomCapacitors;
import prospectpyxis.customcapacitors.ModSettings;
import prospectpyxis.customcapacitors.data.CapacitorData;
import prospectpyxis.customcapacitors.registry.CapacitorRegistry;
import prospectpyxis.pyxislib.entity.player.PlayerUtils;

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
        setMaxStackSize(1);
    }


    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        NBTTagCompound nbt = stack.getTagCompound();
        CapacitorData data = CapacitorRegistry.getDataFromNBT(nbt);
        if (data == null) return;
        if (!data.retainEnergy) return;
        if (!data.chargingData.enabled) return;
        if (!(entityIn instanceof EntityPlayer)) return;
        if (!stack.hasTagCompound() || !stack.getTagCompound().getBoolean("isCharging")) return;

        EntityPlayer player = (EntityPlayer) entityIn;
        ItemStack originalStack = stack.copy();
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack slot = player.inventory.getStackInSlot(i);
            if (slot.equals(stack)) continue;
            if (slot.hasCapability(CapabilityEnergy.ENERGY, null)) {
                int energyStored = nbt.getCompoundTag("BlockEntityTag").getInteger("energy");
                int energySent = slot.getCapability(CapabilityEnergy.ENERGY, null).receiveEnergy(
                        Math.min(data.chargingData.outputSpeed == -1 ? data.maxOutputRate : data.chargingData.outputSpeed, energyStored),
                        false
                );
                if (energySent < 0) energySent = 0;
                nbt.getCompoundTag("BlockEntityTag").setInteger("energy", Math.max(energyStored - energySent, 0));
                stack.setTagCompound(nbt);
            }
        }
        if (player.inventory.getCurrentItem().equals(originalStack)) {
            player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
            player.inventory.markDirty();
        }

    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (!stack.hasTagCompound()) return;
        CapacitorData data = CapacitorRegistry.getDataFromItemStack(stack);
        if (data == null) data = new CapacitorData();

        if (ModSettings.shiftTooltipDetailLevel == 0) {
            if (data.description != null) {
                tooltip.add(data.description);
            }
            if (data.retainEnergy) {
                int egy = 0;
                int maxy = data.capacity;
                if (stack.getTagCompound().hasKey("BlockEntityTag") && stack.getTagCompound().getCompoundTag("BlockEntityTag").hasKey("energy")) {
                    egy = stack.getTagCompound().getCompoundTag("BlockEntityTag").getInteger("energy");
                }
                tooltip.add(I18n.format("tooltip.custom_capacitor.current_energy", TextFormatting.BOLD, TextFormatting.RESET, Integer.toString(egy), Integer.toString(maxy)));
                if (data.chargingData.enabled) {
                    boolean mode = stack.getTagCompound().hasKey("isCharging") && stack.getTagCompound().getBoolean("isCharging");
                    if (mode) {
                        tooltip.add(I18n.format("tooltip.custom_capacitor.is_charging.on", TextFormatting.BOLD, TextFormatting.GREEN));
                    } else {
                        tooltip.add(I18n.format("tooltip.custom_capacitor.is_charging.off", TextFormatting.BOLD, TextFormatting.DARK_RED));
                    }
                }
            }
        } else {
            if (!GuiScreen.isShiftKeyDown()) {
                if (data.description != null) {
                    tooltip.add(data.description);
                }
                if (data.retainEnergy) {
                    int egy = 0;
                    int maxy = data.capacity;
                    if (stack.getTagCompound().hasKey("BlockEntityTag") && stack.getTagCompound().getCompoundTag("BlockEntityTag").hasKey("energy")) {
                        egy = stack.getTagCompound().getCompoundTag("BlockEntityTag").getInteger("energy");
                    }
                    tooltip.add(I18n.format("tooltip.custom_capacitor.current_energy", TextFormatting.BOLD, TextFormatting.RESET, Integer.toString(egy), Integer.toString(maxy)));
                    if (data.chargingData.enabled) {
                        boolean mode = stack.getTagCompound().hasKey("isCharging") && stack.getTagCompound().getBoolean("isCharging");
                        if (mode) {
                            tooltip.add(I18n.format("tooltip.custom_capacitor.is_charging.on", TextFormatting.BOLD, TextFormatting.GREEN));
                        } else {
                            tooltip.add(I18n.format("tooltip.custom_capacitor.is_charging.off", TextFormatting.BOLD, TextFormatting.DARK_RED));
                        }
                    }
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
        if (!stack.hasTagCompound()) return I18n.format("tile.customcapacitors.custom_capacitor.name");
        CapacitorData data = CapacitorRegistry.getDataFromItemStack(stack);
        if (data == null) return I18n.format("tile.customcapacitors.custom_capacitor.name");
        return data.displayName + " " + I18n.format("tile.customcapacitors.custom_capacitor.name");
    }
}
