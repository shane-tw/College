package pm.shane.alexaclone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shane.
 */
public class Address {

    @SerializedName("eircode")
    @Expose
    public String eircode;
    @SerializedName("loc")
    @Expose
    public double[] loc = null;
    @SerializedName("address")
    @Expose
    public String address;

}
