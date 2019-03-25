package prospectpyxis.customcapacitors.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import prospectpyxis.customcapacitors.block.tile.TileEntityCapacitor;

public class MessageCapacitorColor implements IMessage {

    private BlockPos pos;
    private int baseColor;
    private int trimColor;

    public MessageCapacitorColor(BlockPos pos, int baseColor, int trimColor) {
        this.pos = pos;
        this.baseColor = baseColor;
        this.trimColor = trimColor;
    }

    public MessageCapacitorColor(TileEntityCapacitor te) {
        this.pos = te.getPos();
        this.baseColor = te.baseColor;
        this.trimColor = te.trimColor;
    }

    public MessageCapacitorColor() {}

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(baseColor);
        buf.writeInt(trimColor);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        baseColor = buf.readInt();
        trimColor = buf.readInt();
    }

    public static class Handler implements IMessageHandler<MessageCapacitorColor, IMessage> {

        @Override
        public IMessage onMessage(MessageCapacitorColor message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntityCapacitor te = (TileEntityCapacitor)Minecraft.getMinecraft().world.getTileEntity(message.pos);
                te.baseColor = message.baseColor;
                te.trimColor = message.trimColor;
            });
            return null;
        }
    }
}
