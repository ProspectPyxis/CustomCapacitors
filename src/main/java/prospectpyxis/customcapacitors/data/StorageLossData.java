package prospectpyxis.customcapacitors.data;

import com.google.gson.annotations.SerializedName;

public class StorageLossData {

    @SerializedName("loss_value")
    public float lossValue = 0;
    @SerializedName("loss_delay")
    public int lossDelay = 1;
    @SerializedName("min_threshold")
    public int minThreshold = 0;
    @SerializedName("max_threshold")
    public int maxThreshold = -1;


}
