package prospectpyxis.customcapacitors.data;

import com.google.gson.annotations.SerializedName;

public class CapacitorData {

    public String id = "test_capacitor";

    @SerializedName("display_name")
    public String displayName = "Test";
    public String description;
    public int capacity = 10000;
    @SerializedName("max_input_rate")
    public int maxInputRate = 100;
    @SerializedName("max_output_rate")
    public int maxOutputRate = 100;
    @SerializedName("base_color")
    public String colorBase = "FFFFFF";
    @SerializedName("trim_color")
    public String colorTrim = "FFFFFF";
    @SerializedName("storage_loss_type")
    public EnumLossType storageLossType = EnumLossType.NONE;
    @SerializedName("storage_loss_data")
    public StorageLossData storageLossData = new StorageLossData();
    @SerializedName("input_loss_type")
    public EnumLossType inputLossType = EnumLossType.NONE;
    @SerializedName("input_loss_value")
    public float inputLossValue = 0;
    @SerializedName("retain_energy")
    public boolean retainEnergy = false;
    @SerializedName("charging_data")
    public ChargingData chargingData = new ChargingData();
    @SerializedName("comparator_output")
    public boolean comparatorOutput = false;

    public enum EnumLossType {
        @SerializedName("none")
        NONE,
        @SerializedName("constant")
        CONSTANT,
        @SerializedName("percentage")
        PERCENTAGE,
        @SerializedName("percentage_inverted")
        PERCENTAGE_INVERTED
    }

    public int getColorBase() {
        return Integer.parseInt(colorBase, 16);
    }

    public int getColorTrim() {
        return Integer.parseInt(colorTrim, 16);
    }
}
