package prospectpyxis.customcapacitors.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import prospectpyxis.customcapacitors.CustomCapacitors;

public class NetworkHandler {

    public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(CustomCapacitors.modid);

    public static int id;
}
