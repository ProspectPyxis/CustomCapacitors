package prospectpyxis.customcapacitors;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import prospectpyxis.customcapacitors.proxy.CommonProxy;


@Mod.EventBusSubscriber
@Mod(modid = CustomCapacitors.modid, name = CustomCapacitors.name, version = CustomCapacitors.version, dependencies = CustomCapacitors.dependencies)
public class CustomCapacitors {

    public static final String modid = "customcapacitors";
    public static final String name = "Custom Capacitors";
    public static final String version = "1.12.2-0.1";
    public static final String dependencies = "required-after:pyxislib;";

    public static Logger logger;

    @SidedProxy(serverSide = "prospectpyxis.customcapacitors.proxy.ServerProxy", clientSide = "prospectpyxis.customcapacitors.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Now loading " + name);
    }
}
