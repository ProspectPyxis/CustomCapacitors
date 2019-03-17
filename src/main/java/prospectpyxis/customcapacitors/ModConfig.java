package prospectpyxis.customcapacitors;

import net.minecraftforge.common.config.Config;

@Config(modid = CustomCapacitors.modid, name = "customcapacitors/config")
public class ModConfig {

    @Config.Comment({
            "Setting this to true will allow you to hold shift while hovering over a capacitor to see all information about it.",
            "[default: false]"
    })
    public static boolean enableShiftTooltip = false;
}
