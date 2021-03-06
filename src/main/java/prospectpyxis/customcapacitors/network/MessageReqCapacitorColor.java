package prospectpyxis.customcapacitors.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import prospectpyxis.customcapacitors.block.tile.TileEntityCapacitor;

public class MessageReqCapacitorColor implements IMessage {

    private BlockPos pos;
    private int dimension;

    public MessageReqCapacitorColor(BlockPos pos, int dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }

    public MessageReqCapacitorColor(TileEntityCapacitor te) {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }

    public MessageReqCapacitorColor() {}

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

    public static class Handler implements IMessageHandler<MessageReqCapacitorColor, MessageCapacitorColor> {

        @Override
        public MessageCapacitorColor onMessage(MessageReqCapacitorColor message, MessageContext ctx) {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityCapacitor te = (TileEntityCapacitor)world.getTileEntity(message.pos);
            if (te != null) {
                return new MessageCapacitorColor(te);
            } else {
                return null;
            }
        }
    }
}
