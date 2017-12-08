package pm.shane.alexaclone.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.hintdesk.core.activities.AlertMessageBox;
import com.hintdesk.core.util.OSUtil;
import com.hintdesk.core.util.StringUtil;

import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.location.ContactsActivity;
import pm.shane.alexaclone.location.FacebookLogin;
import pm.shane.alexaclone.location.GeofenceMap;
import pm.shane.alexaclone.location.LocationUpdatesListner;
import pm.shane.alexaclone.location.twitterStuff.ConstantValues;
import pm.shane.alexaclone.location.twitterStuff.OAuthActivity;
import pm.shane.alexaclone.location.twitterStuff.TwitterActivity;
import pm.shane.alexaclone.location.twitterStuff.TwitterService;
import pm.shane.alexaclone.location.twitterStuff.TwitterUtil;
import twitter4j.auth.RequestToken;

/**
 * Created by Shane on 28/10/2017.
 */

public class LocationActivity extends AppCompatActivity {

    public final String consumer_key = " iGutaCN8CCRUvrbQHx3gQjrAV";
    public final String secret_key = " s1VaIqE6l1IhlTFToAJXzU6NEAGOruRz5fusk7qpN4PZpoVRWe";
    public static final int RQS_PICK_CONTACT = 9;
    private String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};
    private final int REQ_PERMISSIONS = 104;
    private boolean isUseStoredTokenKey = false;
    private static final String TAG = LocationActivity.class.getSimpleName();
    public static final String PREF_NAME = "locationsharedpreferences";
    public static final String ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH = "ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH";
    private SharedPreferences locationSharedPreferences;
    private Switch geotagSwitch;
    private Switch geofenceNotificationSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        showBackButton();
        locationSharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE );
        geotagSwitch = findViewById(R.id.geotag_switch);
        geofenceNotificationSwitch = findViewById(R.id.geofence_notification_switch);
        boolean notificationEnabled = locationSharedPreferences.getBoolean(ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH,false);
        if (notificationEnabled) {
            onToggleGeotagging(null);
        }
        startService(new Intent(this, LocationUpdatesListner.class));
        startService(new Intent(this, TwitterService.class));
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQ_PERMISSIONS);
        }
        if (!OSUtil.IsNetworkAvailable(this.getApplicationContext())) {
            AlertMessageBox.Show(this, "Internet connection", "A valid internet connection can't be established", AlertMessageBox.AlertMessageBoxIcon.Info);
            return;
        }
        if (StringUtil.isNullOrWhitespace(ConstantValues.TWITTER_CONSUMER_KEY) || StringUtil.isNullOrWhitespace(ConstantValues.TWITTER_CONSUMER_SECRET)) {
            AlertMessageBox.Show(this, "Twitter oAuth infos", "Please set your twitter consumer key and consumer secret", AlertMessageBox.AlertMessageBoxIcon.Info);
            return;
        }
    }

    private void logInTwitter() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        if (!sharedPreferences.getBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN,false)) {
            new TwitterAuthenticateTask().execute();
        }
        else {
            Intent intent = new Intent(this, TwitterActivity.class);
            startActivity(intent);
        }
    }

    static class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {
        @Override
        protected void onPostExecute(RequestToken requestToken) {
            if (requestToken == null) {
                return;
            }
            Intent intent = new Intent(MainApp.getContext(), OAuthActivity.class);
            intent.putExtra(ConstantValues.STRING_EXTRA_AUTHENCATION_URL,requestToken.getAuthenticationURL());
            MainApp.get().startActivity(intent);
        }

        @Override
        protected RequestToken doInBackground(String... params) {
            return TwitterUtil.getInstance().getRequestToken();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void onToggleGeotagging(View view) {

    }

    public void onLinkFacebookClicked(View view) {

        Intent face = new Intent(this, FacebookLogin.class);
        startActivity(face);


    }

    public void onLinkTwitterClicked(View view) {
        logInTwitter();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[ ] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void onSetupGeofenceClicked(View view) {
        Intent intent = new Intent(this, GeofenceMap.class);
        startActivity(intent);
    }

    public void onToggleGeofenceNotifications(View view) {
        boolean notificationState = !geofenceNotificationSwitch.isChecked();
        geofenceNotificationSwitch.setChecked(notificationState);
        if(notificationState) {
            Log.i(TAG, "enable notification");
        } else {
            Log.i(TAG, "disable notification");
        }
        SharedPreferences.Editor editor = locationSharedPreferences.edit();
        editor.putBoolean( ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH, notificationState);
        editor.apply();
    }

    public void onLinkPhoneNumberClicked(View view) {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }

    private void showBackButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}