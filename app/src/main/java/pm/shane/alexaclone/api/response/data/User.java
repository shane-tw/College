package pm.shane.alexaclone.api.response.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shane on 20/10/2017.
 */

public class User {

    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("avatar")
    @Expose
    public String avatar;
    @SerializedName("account_type")
    @Expose
    public String accountType;

}
