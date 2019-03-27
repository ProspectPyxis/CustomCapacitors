package prospectpyxis.customcapacitors.data;

import com.google.gson.annotations.SerializedName;

public class ChargingData {

    public boolean enabled = false;
    @SerializedName("is_bauble")
    public boolean isBauble = false;
    @SerializedName("charging_speed")
    public int chargingSpeed = -1;
}
