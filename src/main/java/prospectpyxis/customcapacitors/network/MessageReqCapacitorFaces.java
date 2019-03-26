package prospectpyxis.customcapacitors.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import prospectpyxis.customcapacitors.block.tile.TileEntityCapacitor;

public class MessageReqCapacitorFaces implements IMessage {

    private BlockPos pos;
    private int dimension;

    public MessageReqCapacitorFaces(BlockPos pos, int dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }

    public MessageReqCapacitorFaces(TileEntityCapacitor te) {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }

    public MessageReqCapacitorFaces() {}

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(dimension);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        dimension = buf.readInt();
    }

    public static class Handler implements IMessageHandler<MessageReqCapacitorFaces, MessageCapacitorFaces> {

        @Override
        public MessageCapacitorFaces onMessage(MessageReqCapacitorFaces message, MessageContext ctx) {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityCapacitor te = (TileEntityCapacitor)world.getTileEntity(message.pos);
            if (te != null) {
                return new MessageCapacitorFaces(te);
            } else {
                return null;
            }
        }
    }
}
