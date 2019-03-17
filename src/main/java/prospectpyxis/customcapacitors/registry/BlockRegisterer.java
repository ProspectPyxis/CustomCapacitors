package prospectpyxis.customcapacitors.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import prospectpyxis.customcapacitors.CustomCapacitors;
import prospectpyxis.customcapacitors.block.BlockCapacitor;
import prospectpyxis.customcapacitors.item.ItemBlockCapacitor;

public class BlockRegisterer {

    public static BlockCapacitor CAPACITOR = new BlockCapacitor();
    public static Item CAPACITOR_ITEM = new ItemBlockCapacitor(CAPACITOR);

    public static void register(IForgeRegistry<Block> registry) {
        registry.register(CAPACITOR);
        GameRegistry.registerTileEntity(CAPACITOR.getTileEntityClass(), CAPACITOR.getRegistryName());
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.register(CAPACITOR_ITEM);
    }

    public static void registerModels() {
        CustomCapacitors.proxy.registerItemRenderer(CAPACITOR_ITEM, 0, "custom_capacitor");
    }
}
