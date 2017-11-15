package pm.shane.alexaclone.preferences.locationclasses;


import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import pm.shane.alexaclone.DBHandler;

/**
 * Created by underscorexxxjesus on 09/11/17.
 */

public class LocationUpdatesListner extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LocationUpdatesListner.class.getSimpleName();
    private GoogleApiClient googleApiClient;
    private DBHandler db = null;

    private LocationManager locationManager;
    private final int REQ_PERMISSIONS = 104;
    private String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS};

    @Override
    public void onCreate(){
        super.onCreate();

        startForeground(NotificationCreator.getNotificationId(),
                NotificationCreator.getNotification(this));

        Log.d(TAG,"Geofence service added to notification");

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        googleApiClient.connect();

        /*

        db = new DBHandler(getApplicationContext());
        ArrayList<Location> locations = new ArrayList<>();

        locations = db.getAllLocationHistory();

        for (int i = 0; i < locations.size(); i++){
            Log.e(TAG, " " + locations.get(i).getLongitude() + " " + locations.get(i).getLatitude() + " " + locations.get(i).getAltitude() + " " + locations.get(i).getTime() + " ");
        }

        */ //works grand above


    }
    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL = 3000;
    private final int FASTEST_INTERVAL = 1000;

    // Start location Updates
    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (checkPermissions()){
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            }
            catch(SecurityException e){}
        }
    }


    private boolean checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this != null && PERMISSIONS != null) {
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {



                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location listner service on location updates");

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        double altitude = location.getAltitude();
        long timestamp = location.getTime();

        //store in database

        if(db == null){
            db = new DBHandler(getApplicationContext());

        }
        else {
            db.addLocationHistory(longitude, latitude, altitude, timestamp);
        }




    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
