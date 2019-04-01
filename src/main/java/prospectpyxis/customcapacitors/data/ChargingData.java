package prospectpyxis.customcapacitors.data;

import com.google.gson.annotations.SerializedName;

public class ChargingData {

    public boolean enabled = false;
    @SerializedName("inventory_input_speed")
    public int inputSpeed = -1;
    @SerializedName("inventory_output_speed")
    public int outputSpeed = -1;
}
