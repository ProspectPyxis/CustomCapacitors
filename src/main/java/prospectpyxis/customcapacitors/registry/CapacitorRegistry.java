package prospectpyxis.customcapacitors.registry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
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

    public static CapacitorData getDataById(String id) {
        for (CapacitorData data : LOADED_CAPACITORS) {
            if (data.id.equals(id)) return data;
        }
        return null;
    }

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
                LOADED_CAPACITORS.add(data);
            } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
                CustomCapacitors.logger.error("Error loading capacitor data: " + e.getMessage());
            }
        }
    }
}
