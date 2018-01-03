package pm.shane.alexaclone;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Shane on 03/11/2017.
 */

public class PermissionUtils {

    public static final int REQUEST_CALL_PHONE = 39183;
    public static final int REQUEST_LOCATION = 48137;
    public static final int REQUEST_LOCATIONS = 48573;
    public static final int REQUEST_CAMERA_CODE = 29123;
    public static final int REQUEST_MIC_CODE = 1;

    public static boolean hasCallPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(MainApp.getContext(), Manifest.permission.CALL_PHONE);
        return (permissionCheck == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestCallPermission(AppCompatActivity callingActivity) {
        ActivityCompat.requestPermissions(callingActivity,
                new String[]{Manifest.permission.CALL_PHONE},
                REQUEST_CALL_PHONE);
    }

    public static void requestCameraPermission(AppCompatActivity callingActivity) {
        ActivityCompat.requestPermissions(callingActivity,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CAMERA_CODE);
    }

    public static void requestMicPermission(AppCompatActivity callingActivity){
        ActivityCompat.requestPermissions(callingActivity, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MIC_CODE);
    }

}
