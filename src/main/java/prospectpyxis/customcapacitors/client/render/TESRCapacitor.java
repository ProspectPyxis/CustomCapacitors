package prospectpyxis.customcapacitors.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.model.animation.FastTESR;
import prospectpyxis.customcapacitors.block.BlockCapacitor;
import prospectpyxis.customcapacitors.block.tile.TileEntityCapacitor;
import prospectpyxis.customcapacitors.proxy.ClientProxy;

import java.awt.*;

public class TESRCapacitor extends FastTESR<TileEntityCapacitor> {

    @Override
    public void renderTileEntityFast(TileEntityCapacitor tec, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        buffer.setTranslation(x, y, z);

        IBlockState state = tec.getWorld().getBlockState(tec.getPos());

        Color cb = new Color(tec.data.getColorBase(), false);
        Color ct = new Color(tec.data.getColorTrim(), false);

        createNewLayer(tec, ClientProxy.ctexBase, cb.getRed(), cb.getBlue(), cb.getGreen(), buffer);
        createNewLayer(tec, ClientProxy.ctexTrim, ct.getRed(), ct.getBlue(), ct.getGreen(), buffer);

        switch (state.getValue(BlockCapacitor.FACING)) {
            case DOWN:
                buffer.pos(1, 0, 1).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMinU(), ClientProxy.ctexOverlayO.getMaxV()).endVertex();
                buffer.pos(0, 0, 1).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMaxU(), ClientProxy.ctexOverlayO.getMaxV()).endVertex();
                buffer.pos(0, 0, 0).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMaxU(), ClientProxy.ctexOverlayO.getMinV()).endVertex();
                buffer.pos(1, 0, 0).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMinU(), ClientProxy.ctexOverlayO.getMinV()).endVertex();
                break;
            case UP:
                buffer.pos(1, 0, 1).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMinU(), ClientProxy.ctexOverlayO.getMaxV()).endVertex();
                buffer.pos(0, 0, 1).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMaxU(), ClientProxy.ctexOverlayO.getMaxV()).endVertex();
                buffer.pos(0, 0, 0).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMaxU(), ClientProxy.ctexOverlayO.getMinV()).endVertex();
                buffer.pos(1, 0, 0).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMinU(), ClientProxy.ctexOverlayO.getMinV()).endVertex();
                break;
            case NORTH:
                buffer.pos(0, 1, 0).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMinU(), ClientProxy.ctexOverlayO.getMaxV()).endVertex();
                buffer.pos(1, 1, 0).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMaxU(), ClientProxy.ctexOverlayO.getMaxV()).endVertex();
                buffer.pos(1, 0, 0).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMaxU(), ClientProxy.ctexOverlayO.getMinV()).endVertex();
                buffer.pos(0, 0, 0).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMinU(), ClientProxy.ctexOverlayO.getMinV()).endVertex();
                break;
            case SOUTH:
                buffer.pos(1, 1, 1).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMinU(), ClientProxy.ctexOverlayO.getMaxV()).endVertex();
                buffer.pos(0, 1, 1).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMaxU(), ClientProxy.ctexOverlayO.getMaxV()).endVertex();
                buffer.pos(0, 0, 1).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMaxU(), ClientProxy.ctexOverlayO.getMinV()).endVertex();
                buffer.pos(1, 0, 1).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMinU(), ClientProxy.ctexOverlayO.getMinV()).endVertex();
                break;
            case WEST:
                buffer.pos(0, 1, 1).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMinU(), ClientProxy.ctexOverlayO.getMaxV()).endVertex();
                buffer.pos(0, 1, 0).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMaxU(), ClientProxy.ctexOverlayO.getMaxV()).endVertex();
                buffer.pos(0, 0, 0).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMaxU(), ClientProxy.ctexOverlayO.getMinV()).endVertex();
                buffer.pos(0, 0, 1).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMinU(), ClientProxy.ctexOverlayO.getMinV()).endVertex();
                break;
            case EAST:
                buffer.pos(1, 1, 0).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMinU(), ClientProxy.ctexOverlayO.getMaxV()).endVertex();
                buffer.pos(1, 1, 1).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMaxU(), ClientProxy.ctexOverlayO.getMaxV()).endVertex();
                buffer.pos(1, 0, 1).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMaxU(), ClientProxy.ctexOverlayO.getMinV()).endVertex();
                buffer.pos(1, 0, 0).color(255, 255, 255, 255)
                        .tex(ClientProxy.ctexOverlayO.getMinU(), ClientProxy.ctexOverlayO.getMinV()).endVertex();
                break;
            default:
        }

    }

    public void createNewLayer(TileEntityCapacitor tec, TextureAtlasSprite tas, int r, int g, int b, BufferBuilder buffer) {
        // DOWN
        buffer.pos(1, 0, 1).color(r, g, b, 255).tex(tas.getMinU(), tas.getMaxV()).endVertex();
        buffer.pos(0, 0, 1).color(r, g, b, 255).tex(tas.getMaxU(), tas.getMaxV()).endVertex();
        buffer.pos(0, 0, 0).color(r, g, b, 255).tex(tas.getMaxU(), tas.getMinV()).endVertex();
        buffer.pos(1, 0, 0).color(r, g, b, 255).tex(tas.getMinU(), tas.getMinV()).endVertex();

        // UP
        buffer.pos(1, 1, 0).color(r, g, b, 255).tex(tas.getMinU(), tas.getMaxV()).endVertex();
        buffer.pos(0, 1, 0).color(r, g, b, 255).tex(tas.getMaxU(), tas.getMaxV()).endVertex();
        buffer.pos(0, 1, 1).color(r, g, b, 255).tex(tas.getMaxU(), tas.getMinV()).endVertex();
        buffer.pos(1, 1, 1).color(r, g, b, 255).tex(tas.getMinU(), tas.getMinV()).endVertex();

        // NORTH
        buffer.pos(0, 1, 0).color(r, g, b, 255).tex(tas.getMinU(), tas.getMaxV()).endVertex();
        buffer.pos(1, 1, 0).color(r, g, b, 255).tex(tas.getMaxU(), tas.getMaxV()).endVertex();
        buffer.pos(1, 0, 0).color(r, g, b, 255).tex(tas.getMaxU(), tas.getMinV()).endVertex();
        buffer.pos(0, 0, 0).color(r, g, b, 255).tex(tas.getMinU(), tas.getMinV()).endVertex();

        // SOUTH
        buffer.pos(1, 1, 1).color(r, g, b, 255).tex(tas.getMinU(), tas.getMaxV()).endVertex();
        buffer.pos(0, 1, 1).color(r, g, b, 255).tex(tas.getMaxU(), tas.getMaxV()).endVertex();
        buffer.pos(0, 0, 1).color(r, g, b, 255).tex(tas.getMaxU(), tas.getMinV()).endVertex();
        buffer.pos(1, 0, 1).color(r, g, b, 255).tex(tas.getMinU(), tas.getMinV()).endVertex();

        // WEST
        buffer.pos(0, 1, 1).color(r, g, b, 255).tex(tas.getMinU(), tas.getMaxV()).endVertex();
        buffer.pos(0, 1, 0).color(r, g, b, 255).tex(tas.getMaxU(), tas.getMaxV()).endVertex();
        buffer.pos(0, 0, 0).color(r, g, b, 255).tex(tas.getMaxU(), tas.getMinV()).endVertex();
        buffer.pos(0, 0, 1).color(r, g, b, 255).tex(tas.getMinU(), tas.getMinV()).endVertex();

        // EAST
        buffer.pos(1, 1, 0).color(r, g, b, 255).tex(tas.getMinU(), tas.getMaxV()).endVertex();
        buffer.pos(1, 1, 1).color(r, g, b, 255).tex(tas.getMaxU(), tas.getMaxV()).endVertex();
        buffer.pos(1, 0, 1).color(r, g, b, 255).tex(tas.getMaxU(), tas.getMinV()).endVertex();
        buffer.pos(1, 0, 0).color(r, g, b, 255).tex(tas.getMinU(), tas.getMinV()).endVertex();
    }
}
