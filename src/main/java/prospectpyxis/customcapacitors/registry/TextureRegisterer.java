package prospectpyxis.customcapacitors.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import prospectpyxis.customcapacitors.CustomCapacitors;

@Mod.EventBusSubscriber
public class TextureRegisterer {

    public static TextureAtlasSprite base;
    public static TextureAtlasSprite trim;
    public static TextureAtlasSprite outline;
    public static TextureAtlasSprite overlayInput;
    public static TextureAtlasSprite overlayOutput;

    @SubscribeEvent
    public static void registerTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) {
            base = event.getMap().registerSprite(new ResourceLocation(CustomCapacitors.modid + ":blocks/capacitor_base"));
            trim = event.getMap().registerSprite(new ResourceLocation(CustomCapacitors.modid + ":blocks/capacitor_trim"));
            outline = event.getMap().registerSprite(new ResourceLocation(CustomCapacitors.modid + ":blocks/capacitor_overlay"));
            overlayInput = event.getMap().registerSprite(new ResourceLocation(CustomCapacitors.modid + ":blocks/capacitor_overlay_input"));
            overlayOutput = event.getMap().registerSprite(new ResourceLocation(CustomCapacitors.modid + ":blocks/capacitor_overlay_output"));
        }
    }
}
