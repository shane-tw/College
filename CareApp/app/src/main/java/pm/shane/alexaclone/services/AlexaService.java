package pm.shane.alexaclone.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.integreight.onesheeld.sdk.OneSheeldSdk;

import pm.shane.alexaclone.NotificationUtils;

/**
 * Created by Shane on 17/09/2017.
 */

public class AlexaService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationUtils.showPermanentNotification(this);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        OneSheeldSdk.getManager().disconnectAll();
        NotificationUtils.hideNotification(this);
        super.onDestroy();
    }

}
