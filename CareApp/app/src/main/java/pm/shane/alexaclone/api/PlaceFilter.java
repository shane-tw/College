package pm.shane.alexaclone.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shane on 15/11/2017.
 */

public class PlaceFilter {
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("type")
    private String type;

    public PlaceFilter(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public PlaceFilter(double longitude, double latitude, String type) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
    }
}
