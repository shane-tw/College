package pm.shane.alexaclone.api.response.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import pm.shane.alexaclone.models.RemoteCamera;

/**
 * Created by Shane on 20/10/2017.
 */

public class Patient extends User {
    @SerializedName("carers")
    @Expose
    private List<Object> carers;
    @SerializedName("geofence_points")
    @Expose
    private List<Object> geofencePoints;
    @SerializedName("enable_geofence")
    @Expose
    private Boolean enableGeofence;
    @SerializedName("calendar_events")
    @Expose
    private List<Object> calendarEvents;
    @SerializedName("remote_camera")
    @Expose
    private RemoteCamera remoteCamera = new RemoteCamera();
    @SerializedName("allow_location_tracking")
    @Expose
    private Boolean allowLocationTracking;

    public Patient() {
        accountType = "Patient";
    }

    public RemoteCamera getRemoteCamera() {
        return remoteCamera;
    }
}
