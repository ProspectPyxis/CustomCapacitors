package prospectpyxis.customcapacitors;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
import prospectpyxis.customcapacitors.block.tile.TileEntityCapacitor;
import prospectpyxis.customcapacitors.client.render.TESRCapacitor;
import prospectpyxis.customcapacitors.data.CapacitorData;
import prospectpyxis.customcapacitors.item.ItemBlockCapacitor;
import prospectpyxis.customcapacitors.network.MessageCapacitorColor;
import prospectpyxis.customcapacitors.network.MessageCapacitorFaces;
import prospectpyxis.customcapacitors.network.MessageReqCapacitorColor;
import prospectpyxis.customcapacitors.network.MessageReqCapacitorFaces;
import prospectpyxis.customcapacitors.proxy.CommonProxy;
import prospectpyxis.customcapacitors.registry.BlockRegisterer;
import prospectpyxis.customcapacitors.registry.CapacitorRegistry;

import java.io.File;


@Mod.EventBusSubscriber
@Mod(modid = CustomCapacitors.modid, name = CustomCapacitors.name, version = CustomCapacitors.version, dependencies = CustomCapacitors.dependencies)
public class CustomCapacitors {

    public static final String modid = "customcapacitors";
    public static final String name = "Custom Capacitors";
    public static final String version = "1.12.2-1.1.0";
    public static final String dependencies = "required-after:pyxislib@1.12.2-1.0.2;";

    public static Logger logger;
    public static File configFolder;

    public static SimpleNetworkWrapper NETWORKING;

    @SidedProxy(serverSide = "prospectpyxis.customcapacitors.proxy.ServerProxy", clientSide = "prospectpyxis.customcapacitors.proxy.ClientProxy")
    public static CommonProxy proxy;

    @GameRegistry.ObjectHolder(modid + ":custom_capacitor")
    public static ItemBlockCapacitor capItem;

    public static CreativeTabs ctab = new CreativeTabs("custom_capacitors") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(capItem);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Now loading " + name);
        configFolder = event.getModConfigurationDirectory();

        NETWORKING = NetworkRegistry.INSTANCE.newSimpleChannel(modid);
        NETWORKING.registerMessage(new MessageCapacitorColor.Handler(), MessageCapacitorColor.class, 0, Side.CLIENT);
        NETWORKING.registerMessage(new MessageReqCapacitorColor.Handler(), MessageReqCapacitorColor.class, 1, Side.SERVER);
        NETWORKING.registerMessage(new MessageCapacitorFaces.Handler(), MessageCapacitorFaces.class, 2, Side.CLIENT);
        NETWORKING.registerMessage(new MessageReqCapacitorFaces.Handler(), MessageReqCapacitorFaces.class, 3, Side.SERVER);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        CapacitorRegistry.loadCapacitors(configFolder);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCapacitor.class, new TESRCapacitor());
    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event ) {
            BlockRegisterer.registerItemBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            BlockRegisterer.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            BlockRegisterer.registerModels();
        }

        @SubscribeEvent
        public static void registerItemColorHandler(ColorHandlerEvent.Item event) {
            final ItemColors itemColors = event.getItemColors();

            final IItemColor capacitorItemColorHandler = (stack, tintIndex) -> {
                if (!stack.hasTagCompound()) {
                    if (tintIndex == 0 || tintIndex == 1) return 16777215;
                    else return 0;
                }
                CapacitorData data = CapacitorRegistry.getDataById(stack.getTagCompound().getCompoundTag("BlockEntityTag").getString("capid"));
                if (data == null) {
                    if (tintIndex == 0 || tintIndex == 1) return 16777215;
                    else return 0;
                }
                if (tintIndex == 0) return data.getColorBase();
                else if (tintIndex == 1) return data.getColorTrim();
                else return 0;
            };

            itemColors.registerItemColorHandler(capacitorItemColorHandler, BlockRegisterer.CAPACITOR);
        }

    }
}
