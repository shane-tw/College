package pm.shane.alexaclone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Shane on 03/11/2017.
 */

public class NotificationUtils {

    public static final String DEFAULT_CHANNEL_ID = "pm.shane.alexaclone.default";
    public static final String DEFAULT_CHANNEL_NAME = "Default Channel";
    public static final int PERM_NOTIFICATON_ID = 19283927;
    private static NotificationManager manager;

    private NotificationUtils() {}

    public static void createChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { // Notification channels don't exist prior to API v26
            return;
        }
        NotificationChannel defaultChannel = new NotificationChannel(DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        getManager().createNotificationChannel(defaultChannel);
    }

    public static void showPermanentNotification(Service service) {
        createChannels();
        Notification permanentNotifcation = new NotificationCompat.Builder(MainApp.getContext(), DEFAULT_CHANNEL_ID)
                .setContentTitle(MainApp.getContext().getString(R.string.running_title))
                .setContentText(MainApp.getContext().getString(R.string.running_in_background))
                .setSmallIcon(android.R.color.transparent)
                .setOngoing(true)
                .build();
        service.startForeground(PERM_NOTIFICATON_ID, permanentNotifcation);
    }

    public static void hideNotification(Service service) {
        service.stopForeground(true);
    }

    public static synchronized NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) MainApp.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

}
