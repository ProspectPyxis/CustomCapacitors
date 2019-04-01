package prospectpyxis.customcapacitors.registry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import prospectpyxis.customcapacitors.CustomCapacitors;
import prospectpyxis.customcapacitors.data.CapacitorData;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CapacitorRegistry {

    public static Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();

    public static List<CapacitorData> LOADED_CAPACITORS = new ArrayList<>();
    public static List<String> ID_LIST = new ArrayList<>();

    public static void loadCapacitors(File f) {
        LOADED_CAPACITORS.clear();

        File configFolder = new File(f, CustomCapacitors.modid);
        if (!configFolder.exists()) configFolder.mkdir();

        File capacitorsFolder = new File(configFolder, "capacitors");
        if (!capacitorsFolder.exists()) capacitorsFolder.mkdir();

        FileFilter jsonFilter = (pathname) -> pathname.getName().endsWith(".json");

        File[] files = capacitorsFolder.listFiles(jsonFilter);

        if (files == null) return;
        for (File ff : files) {
            try {
                CapacitorData data = gson.fromJson(new FileReader(ff), CapacitorData.class);
                if (data.id == null) {
                    CustomCapacitors.logger.warn("WARNING: A capacitor in file " + ff.getName() + " does not have an id assigned!");
                    CustomCapacitors.logger.warn("The capacitor will not be registered, please give an id value to all capacitors!");
                    continue;
                }
                else if (ID_LIST.contains(data.id)) {
                    CustomCapacitors.logger.warn("WARNING: Multiple capacitors have the id " + data.id + "!");
                    CustomCapacitors.logger.warn("Please make sure every capacitor has a unique id!");
                    continue;
                }
                if (data.storageLossData.lossDelay < 1) {
                    data.storageLossData.lossDelay = 1;
                    CustomCapacitors.logger.warn("WARNING: Capacitor " + data.id + " has less than 1 loss delay!");
                    CustomCapacitors.logger.warn("It will be set to 1, please change the loss delay to a positive number!");
                }
                LOADED_CAPACITORS.add(data);
                ID_LIST.add(data.id);
            } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
                CustomCapacitors.logger.error("Error loading capacitor data: " + e.getMessage());
            }
        }
    }

    public static CapacitorData getDataById(String id) {
        for (CapacitorData data : LOADED_CAPACITORS) {
            if (data.id.equals(id)) return data;
        }
        return null;
    }

    public static CapacitorData getDataFromNBT(NBTTagCompound tag) {
        if (tag == null || tag.hasNoTags()) return null;
        NBTTagCompound nbt = tag.getCompoundTag("BlockEntityTag");
        if (nbt.hasNoTags()) return null;
        return getDataById(nbt.getString("capid"));
    }

    public static CapacitorData getDataFromItemStack(ItemStack stack) {
        return getDataFromNBT(stack.getTagCompound());
    }
}
