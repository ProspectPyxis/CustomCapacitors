package prospectpyxis.customcapacitors.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import prospectpyxis.customcapacitors.block.tile.TileEntityCapacitor;

public class MessageCapacitorColor implements IMessage {

    public long blockPos;

    public int baseColor;
    public int trimColor;

    public MessageCapacitorColor(long blockPos, int baseColor, int trimColor) {
        this.blockPos = blockPos;
        this.baseColor = baseColor;
        this.trimColor = trimColor;
    }

    public MessageCapacitorColor() {}

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = buf.readLong();
        baseColor = buf.readInt();
        trimColor = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(blockPos);
        buf.writeInt(baseColor);
        buf.writeInt(trimColor);
    }

    public static class MessageHandlerCapacitorColor implements IMessageHandler<MessageCapacitorColor, IMessage> {
        @Override
        public IMessage onMessage(MessageCapacitorColor message, MessageContext ctx) {
            BlockPos pos = BlockPos.fromLong(message.blockPos);
            TileEntity te = Minecraft.getMinecraft().world.getTileEntity(pos);
            if (te instanceof TileEntityCapacitor) {
                TileEntityCapacitor tec = (TileEntityCapacitor) te;
                tec.setColors(message.baseColor, message.trimColor);
            }
            return null;
        }
    }
}
