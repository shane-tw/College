package pm.shane.alexaclone;

import android.app.Application;
import android.content.Context;

import com.integreight.onesheeld.sdk.OneSheeldDevice;
import com.integreight.onesheeld.sdk.OneSheeldSdk;

/**
 * Created by Shane on 17/09/2017.
 */

public class MainApp extends Application {

    private static Application mInstance;
    private static OneSheeldDevice device;

    @Override
    public void onCreate() {
        mInstance = this;
        OneSheeldSdk.init(getContext());
        super.onCreate();
    }

    public static Application get() {
        return mInstance;
    }

    public static Context getContext() {
        return (Context)mInstance;
    }

    public static OneSheeldDevice getConnectedDevice() {
        return device;
    }

    public static void setConnectedDevice(OneSheeldDevice device) {
        MainApp.device = device;
    }
}
