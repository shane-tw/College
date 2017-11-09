package pm.shane.alexaclone.preferences.locationclasses;

import android.app.Notification;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import pm.shane.alexaclone.R;

/**
 * Created by underscorexxxjesus on 09/11/17.
 */

public class NotificationCreator {

    private static final int NOTIFICATION_ID = 1094;

    private static Notification notification;

    public static Notification getNotification(Context context) {

        if(notification == null) {

            notification = new NotificationCompat.Builder(context)
                    .setContentTitle("background location Services")
                    .setContentText("location listener")
                    .setSmallIcon(android.R.color.transparent)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .build();
        }

        return notification;
    }

    public static int getNotificationId() {
        return NOTIFICATION_ID;
    }
}