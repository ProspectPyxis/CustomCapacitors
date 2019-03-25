package prospectpyxis.customcapacitors.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.animation.FastTESR;
import org.lwjgl.util.Color;
import prospectpyxis.customcapacitors.block.BlockCapacitor;
import prospectpyxis.customcapacitors.block.tile.TileEntityCapacitor;
import prospectpyxis.customcapacitors.proxy.ClientProxy;
import prospectpyxis.pyxislib.client.RendererUtils;
public class TESRCapacitor extends FastTESR<TileEntityCapacitor> {

    @Override
    public void renderTileEntityFast(TileEntityCapacitor tec, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        // buffer.setTranslation(x, y, z);

        World wld = tec.getWorld();
        IBlockState state = wld.getBlockState(tec.getPos());

        Color cb = new Color(((tec.baseColor & 0xFF0000) >> 16), ((tec.baseColor & 0xFF00) >> 8), tec.baseColor & 0xFF);
        Color ct = new Color(((tec.trimColor & 0xFF0000) >> 16), ((tec.trimColor & 0xFF00) >> 8), tec.trimColor & 0xFF);

        RendererUtils.renderCubeWithLighting(buffer, (float)x, (float)y, (float)z, cb, ClientProxy.ctexBase, tec.getWorld(), state, tec.getPos());
        RendererUtils.renderCubeWithLighting(buffer, (float)x, (float)y, (float)z, ct, ClientProxy.ctexTrim, tec.getWorld(), state, tec.getPos());
        RendererUtils.renderCubeWithLighting(buffer, (float)x, (float)y, (float)z, new Color(255, 255, 255), ClientProxy.ctexOverlay, tec.getWorld(), state, tec.getPos());

        EnumFacing isFacing = state.getValue(BlockCapacitor.FACING);
        RendererUtils.renderFace(buffer, isFacing, (float)x, (float)y, (float)z, new Color(255, 255, 255), ClientProxy.ctexOverlayO,
                RendererUtils.getLightMapSkyLight(state, tec.getWorld(), tec.getPos().offset(isFacing)),
                RendererUtils.getLightMapBlockLight(state, tec.getWorld(), tec.getPos().offset(isFacing)));
    }
}
