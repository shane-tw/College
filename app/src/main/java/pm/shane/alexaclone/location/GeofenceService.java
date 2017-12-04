package pm.shane.alexaclone.location;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;


import java.util.ArrayList;

import pm.shane.alexaclone.DBHandler;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.activities.LocationActivity;

/**
 * Created by underscorexxxjesus on 09/11/17.
 */

public class GeofenceService extends IntentService{

    private static final String TAG = GeofenceService.class.getSimpleName();

    public static final int GEOFENCE_NOTIFICATION_ID = 0;
    private DBHandler db = null;
    private ArrayList<String> numbers;

    // needed to stop service from closing
    public static final int BREACH_NOTIFICATION = 22;
    @Override
    public void onCreate(){
        super.onCreate();

        startForeground(NotificationCreator.getNotificationId(),
                NotificationCreator.getNotification(this));
                Log.d(TAG,"Geofence service added to notification");
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public GeofenceService() {
        super(TAG);

    }

    private String smsMessage = "user has just escaped the perimeter";


    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        // Handling errors
        if ( geofencingEvent.hasError() ) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode() );
            Log.e( TAG, errorMsg );
            return;
        }

        int geoFenceTransition = geofencingEvent.getGeofenceTransition();
        // Check if the transition type is of interest
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ) {

            Log.d(TAG,"geofence breached, sending sms ");

            /*
           // createOnGoingNotification();

            //sms people
            SmsManager smsManager = SmsManager.getDefault();

            for(int i = 0; i < phones.length ; i++){
                smsManager.sendTextMessage(phones[i],null, smsMessage, null, null);
            }

            */

            SharedPreferences sharedPref = getApplication().getSharedPreferences(LocationActivity.PREF_NAME, Context.MODE_PRIVATE );
            boolean notify = false;
            if(sharedPref.contains(LocationActivity.ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH)){
                notify = sharedPref.getBoolean(LocationActivity.ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH, false);
            }

            if (notify){
                createNotification();
            }

            if(db == null){
                db = new DBHandler(getApplicationContext());

            }

            numbers = db.getContactsNumbers();

            if(numbers.size()!=0){
                SmsManager smsManager = SmsManager.getDefault();

                for(int i = 0; i < numbers.size() ; i++){
                    smsManager.sendTextMessage(numbers.get(i),null, smsMessage, null, null);
                }

            }




        }
    }



    private void createNotification(){
        Intent notificationIntent = GeofenceMap.makeNotificationIntent(getApplicationContext(), "");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(GeofenceMap.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
        notificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Geofence breached")
                .setContentText("You have breached the geofence")
                .setContentIntent(notificationPendingIntent);

        // Creating and sending Notification
        NotificationManager notificatioMng = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificatioMng.notify(BREACH_NOTIFICATION,notificationBuilder.build());




    }


    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }



}
