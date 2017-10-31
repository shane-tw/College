package pm.shane.alexaclone.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shane on 20/10/2017.
 */

public class Error {

    @SerializedName("type")
    private String type;
    @SerializedName("key")
    private String key;
    @SerializedName("message")
    private String message;

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }

}
