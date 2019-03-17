package prospectpyxis.customcapacitors.proxy;


import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import prospectpyxis.customcapacitors.CustomCapacitors;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(CustomCapacitors.modid + ":" + id, "inventory"));
    }
}
