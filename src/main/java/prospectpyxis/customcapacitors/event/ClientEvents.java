package prospectpyxis.customcapacitors.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import prospectpyxis.customcapacitors.CustomCapacitors;
import prospectpyxis.customcapacitors.item.ItemBlockCapacitor;
import prospectpyxis.customcapacitors.network.MessageChangeChargeMode;
import prospectpyxis.customcapacitors.network.NetworkManager;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = CustomCapacitors.modid)
public class ClientEvents {

    @SubscribeEvent
    public static void onLeftClickInAir(PlayerInteractEvent.LeftClickEmpty event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.inventory.getCurrentItem().copy();
        if (stack.getItem() instanceof ItemBlockCapacitor) {
            NetworkManager.NET.sendToServer(new MessageChangeChargeMode());
        }
    }
}
