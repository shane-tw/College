package pm.shane.alexaclone;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Shane on 03/11/2017.
 */

public class PermissionUtils {

    public static final int REQUEST_CALL_PHONE = 123871298;
    public static final int REQUEST_LOCATION = 981729817;
    public static final int REQUEST_LOCATIONS = 783912373;

    public static boolean hasCallPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(MainApp.getContext(), Manifest.permission.CALL_PHONE);
        return (permissionCheck == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestCallPermission(Activity callingActivity) {
        ActivityCompat.requestPermissions(callingActivity,
                new String[]{Manifest.permission.CALL_PHONE},
                REQUEST_CALL_PHONE);
    }

}
