package prospectpyxis.customcapacitors.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import prospectpyxis.customcapacitors.CustomCapacitors;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(CustomCapacitors.modid + ":" + id, "inventory"));
    }

    public static TextureAtlasSprite ctexBase;
    public static TextureAtlasSprite ctexTrim;
    public static TextureAtlasSprite ctexOverlay;
    public static TextureAtlasSprite ctexOverlayI;
    public static TextureAtlasSprite ctexOverlayO;

    @SubscribeEvent
    public static void registerTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) {
            ctexBase = event.getMap().registerSprite(new ResourceLocation(CustomCapacitors.modid + ":blocks/capacitor_base"));
            ctexTrim = event.getMap().registerSprite(new ResourceLocation(CustomCapacitors.modid + ":blocks/capacitor_trim"));
            ctexOverlay = event.getMap().registerSprite(new ResourceLocation(CustomCapacitors.modid + ":blocks/capacitor_overlay"));
            ctexOverlayI = event.getMap().registerSprite(new ResourceLocation(CustomCapacitors.modid + ":blocks/capacitor_overlay_input"));
            ctexOverlayO = event.getMap().registerSprite(new ResourceLocation(CustomCapacitors.modid + ":blocks/capacitor_overlay_output"));
        }
    }
}
