package pm.shane.alexaclone.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shane on 20/10/2017.
 */

public class Credentials {

    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("account_model_name")
    private String accountType;

    public Credentials(String email, String password, String accountType) {
        this.email = email;
        this.password = password;
        this.accountType = accountType;
    }

}
