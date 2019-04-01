package prospectpyxis.customcapacitors.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import prospectpyxis.customcapacitors.block.tile.TileEntityCapacitor;

public class MessageCapacitorFaces implements IMessage {

    private BlockPos pos;
    private int[] faces = new int[6];

    public MessageCapacitorFaces(BlockPos pos, int[] faces) {
        this.pos = pos;
        this.faces = faces;
    }

    public MessageCapacitorFaces(TileEntityCapacitor te) {
        this(te.getPos(), te.io_faces);
    }

    public MessageCapacitorFaces() {}

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        for (int i = 0; i < 6; i++) {
            buf.writeInt(faces[i]);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        for (int i = 0; i < 6; i++) {
            faces[i] = buf.readInt();
        }
    }

    public static class Handler implements IMessageHandler<MessageCapacitorFaces, IMessage> {

        @Override
        public IMessage onMessage(MessageCapacitorFaces message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
               TileEntityCapacitor te = (TileEntityCapacitor)Minecraft.getMinecraft().world.getTileEntity(message.pos);
               te.io_faces = message.faces;
            });
            return null;
        }
    }
}
