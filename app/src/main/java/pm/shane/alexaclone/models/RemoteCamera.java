package pm.shane.alexaclone.models;

import android.util.Base64;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shane on 20/10/2017.
 */

public class RemoteCamera {

    @SerializedName("last_picture")
    @Expose
    public String lastPicture;
    @SerializedName("enabled")
    @Expose
    public Boolean enabled;

    public void setLastPicture(byte[] data) {
        lastPicture = "data:image/jpeg;base64," + Base64.encodeToString(data, Base64.DEFAULT);
    }

}
