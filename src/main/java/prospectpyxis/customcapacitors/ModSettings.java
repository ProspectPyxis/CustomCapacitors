package prospectpyxis.customcapacitors;

import net.minecraftforge.common.config.Config;

@Config(modid = CustomCapacitors.modid, name = "customcapacitors/config")
public class ModSettings {

    @Config.Comment({
            "The amount of detail shown when you hold shift while hovering over a capacitor.",
            "0: No shift tooltip, 1: Basic details (capacity, max IO rate),",
            "2: Loss type details, 3: Full details",
            "[default: 0]"
    })
    @Config.RangeInt(min = 0, max = 3)
    public static int shiftTooltipDetailLevel = 0;
}
