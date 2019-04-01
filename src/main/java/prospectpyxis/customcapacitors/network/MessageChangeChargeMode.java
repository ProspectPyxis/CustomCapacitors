package prospectpyxis.customcapacitors.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import prospectpyxis.customcapacitors.CustomCapacitors;
import prospectpyxis.customcapacitors.data.CapacitorData;
import prospectpyxis.customcapacitors.item.ItemBlockCapacitor;
import prospectpyxis.customcapacitors.registry.CapacitorRegistry;

public class MessageChangeChargeMode implements IMessage {

    public MessageChangeChargeMode() {}

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<MessageChangeChargeMode, IMessage> {
        @Override
        public IMessage onMessage(MessageChangeChargeMode message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            InventoryPlayer inv = player.inventory;
            ItemStack stack = inv.getCurrentItem().copy();
            if (stack.getItem() instanceof ItemBlockCapacitor) {
                CapacitorData data = CapacitorRegistry.getDataFromItemStack(stack);
                if (!data.retainEnergy || !data.chargingData.enabled) return null;
                NBTTagCompound nbt = stack.getTagCompound();
                if (nbt == null) return null;
                boolean isCharging = nbt.hasKey("isCharging") && nbt.getBoolean("isCharging");

                if (!isCharging) {
                    nbt.setBoolean("isCharging", true);
                } else {
                    nbt.setBoolean("isCharging", false);
                }

                stack.setTagCompound(nbt);
                inv.setInventorySlotContents(inv.currentItem, stack);
                inv.markDirty();
            }
            return null;
        }
    }
}
