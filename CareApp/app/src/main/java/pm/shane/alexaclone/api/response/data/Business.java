package pm.shane.alexaclone.api.response.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shane on 15/11/2017.
 */

public class Business {

    @SerializedName("eir_id")
    @Expose
    private String eirId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("loc")
    @Expose
    private double[] loc;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
