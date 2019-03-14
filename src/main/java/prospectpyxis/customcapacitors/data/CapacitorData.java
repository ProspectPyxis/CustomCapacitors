package prospectpyxis.customcapacitors.data;

import com.google.gson.annotations.SerializedName;

public class CapacitorData {

    public String id;

    @SerializedName("display_name")
    public String displayName;
    public int capacity;
    @SerializedName("max_transfer_rate")
    public int maxInputRate = -1;
    public int maxOutputRate = -1;
    public String color = "FFFFFF";
    public String description;
    @SerializedName("storage_loss_type")
    public EnumLossType storageLossType = EnumLossType.NONE;
    @SerializedName("storage_loss_data")
    public StorageLossData storageLossData = new StorageLossData();
    @SerializedName("input_loss_type")
    public EnumLossType inputLossType = EnumLossType.NONE;
    @SerializedName("input_loss_value")
    public float inputLossValue = 0;
    @SerializedName("comparator_output")
    public boolean comparatorOutput = false;

    public enum EnumLossType {
        NONE, CONSTANT, PERCENTAGE, PERCENTAGE_INVERTED
    }

    public int getColor() {
        return Integer.parseInt(color, 16);
    }
}
