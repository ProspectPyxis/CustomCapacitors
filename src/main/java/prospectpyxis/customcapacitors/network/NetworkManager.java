package prospectpyxis.customcapacitors.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import prospectpyxis.customcapacitors.CustomCapacitors;

public class NetworkManager {
    public static SimpleNetworkWrapper NET;

    public static void registerMessages() {
        NET = NetworkRegistry.INSTANCE.newSimpleChannel(CustomCapacitors.modid);
        NET.registerMessage(new MessageCapacitorColor.Handler(), MessageCapacitorColor.class, 0, Side.CLIENT);
        NET.registerMessage(new MessageReqCapacitorColor.Handler(), MessageReqCapacitorColor.class, 1, Side.SERVER);
        NET.registerMessage(new MessageCapacitorFaces.Handler(), MessageCapacitorFaces.class, 2, Side.CLIENT);
        NET.registerMessage(new MessageReqCapacitorFaces.Handler(), MessageReqCapacitorFaces.class, 3, Side.SERVER);
        NET.registerMessage(new MessageChangeChargeMode.Handler(), MessageChangeChargeMode.class, 4, Side.SERVER);
    }
}
