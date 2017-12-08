package pm.shane.alexaclone.api.response.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import pm.shane.alexaclone.models.Address;

/**
 * Created by Shane on 20/10/2017.
 */

public class User {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("account_model_name")
    @Expose
    String accountType;

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}
