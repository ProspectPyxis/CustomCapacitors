package prospectpyxis.customcapacitors.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        // BlockColors colors = Minecraft.getMinecraft().getBlockColors();
        // colors.registerBlockColorHandler(new ColorCapacitor(), new BlockCapacitor());
    }
}
