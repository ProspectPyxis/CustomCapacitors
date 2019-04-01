package prospectpyxis.customcapacitors.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;
import prospectpyxis.customcapacitors.CustomCapacitors;

public class ClientProxy extends CommonProxy {

    public static final KeyBinding MODE_CHANGE = new KeyBinding(
            "customcapacitors.options.mode_change_key",
            KeyConflictContext.IN_GAME,
            Keyboard.KEY_V,
            CustomCapacitors.name
    );

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(CustomCapacitors.modid + ":" + id, "inventory"));
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        ClientRegistry.registerKeyBinding(MODE_CHANGE);
    }
}
