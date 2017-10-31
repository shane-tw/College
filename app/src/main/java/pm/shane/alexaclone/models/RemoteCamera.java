package pm.shane.alexaclone.models;

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
        public boolean enabled;

}
