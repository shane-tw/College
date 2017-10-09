package pm.shane.alexaclone;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.integreight.onesheeld.sdk.OneSheeldSdk;

/**
 * Created by Shane on 17/09/2017.
 */

public class AlexaService extends Service {

    private static final String CHANNEL_ID_CONNECTED = "connected_shown_constantly";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        OneSheeldSdk.getManager().disconnectAll();
        hideNotification();
        super.onDestroy();
    }

    private void showNotification() {
        NotificationCompat.Builder build = new NotificationCompat.Builder(this, CHANNEL_ID_CONNECTED);
        build.setContentText("Connected to device.");
        build.setContentTitle("Connected");
        build.setTicker("Conected");
        build.setWhen(System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        build.setContentIntent(intent);
        Notification notification = build.build();
        startForeground(1, notification);
    }

    private void hideNotification() {
        stopForeground(true);
    }
}
