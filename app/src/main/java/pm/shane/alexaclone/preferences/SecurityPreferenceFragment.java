package pm.shane.alexaclone.preferences;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.ActivityCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;
import android.widget.Toast;


import com.hintdesk.core.activities.AlertMessageBox;
import com.hintdesk.core.util.OSUtil;
import com.hintdesk.core.util.StringUtil;

import pm.shane.alexaclone.MainActivity;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.preferences.locationclasses.ContactsActivity;
import pm.shane.alexaclone.preferences.locationclasses.GeofenceMap;
import pm.shane.alexaclone.preferences.locationclasses.LocationUpdatesListner;
import pm.shane.alexaclone.preferences.locationclasses.twitterStuff.ConstantValues;
import pm.shane.alexaclone.preferences.locationclasses.twitterStuff.OAuthActivity;
import pm.shane.alexaclone.preferences.locationclasses.twitterStuff.TwitterActivity;
import pm.shane.alexaclone.preferences.locationclasses.twitterStuff.TwitterService;
import pm.shane.alexaclone.preferences.locationclasses.twitterStuff.TwitterUtil;
import twitter4j.auth.RequestToken;


import static android.app.Activity.RESULT_OK;


/**
 * Created by Shane on 28/10/2017.
 */

public class SecurityPreferenceFragment extends PreferenceFragment {

    public static final int RQS_PICK_CONTACT = 9;
    private String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};
    private final int REQ_PERMISSIONS = 104;

    private static final String TAG = SecurityPreferenceFragment.class.getSimpleName();
    public static final String  PREF_NAME = "locationsharedpreferences";
    public static final String ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH = "ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH";
    private boolean isUseStoredTokenKey = false;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_location, rootKey);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        boolean notificationenabled = false;

        if (sharedPref.contains(ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH)) {
            notificationenabled = sharedPref.getBoolean(ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH, false);
        }


        findPreference("geotag_switch").setOnPreferenceClickListener(this::handleGeoTagSwitch);
        findPreference("link_facebook_btn").setOnPreferenceClickListener(this::handleLinkFacebook);
        findPreference("link_twitter_btn").setOnPreferenceClickListener(this::handleLinkTwitter);
        findPreference("show_map").setOnPreferenceClickListener(this::handleShowMap);

        SwitchPreference notification = (android.support.v14.preference.SwitchPreference) findPreference("notifications_new_message");
        notification.setChecked(notificationenabled);
        notification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((Boolean) newValue) {
                    Log.i(TAG, "enable notification");
                    editor.putBoolean(ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH, true);

                    editor.apply();

                } else {
                    Log.i(TAG, "disable notification");
                    editor.putBoolean(ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH, false);

                    editor.apply();
                }


                return true;
            }
        });


        findPreference("link_phone").setOnPreferenceClickListener(this::handleLinkPhone);

        getActivity().startService(new Intent(getActivity(), LocationUpdatesListner.class));
        getActivity().startService(new Intent(getActivity(), TwitterService.class));


        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQ_PERMISSIONS);

        }


        if (!OSUtil.IsNetworkAvailable(getActivity().getApplicationContext())) {
            AlertMessageBox.Show(getActivity(), "Internet connection", "A valid internet connection can't be established", AlertMessageBox.AlertMessageBoxIcon.Info);
            return;
        }

        if (StringUtil.isNullOrWhitespace(ConstantValues.TWITTER_CONSUMER_KEY) || StringUtil.isNullOrWhitespace(ConstantValues.TWITTER_CONSUMER_SECRET)) {
            AlertMessageBox.Show(getActivity(), "Twitter oAuth infos", "Please set your twitter consumer key and consumer secret", AlertMessageBox.AlertMessageBoxIcon.Info);
            return;
        }







    }

    private void logIn() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        if (!sharedPreferences.getBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN,false))
        {
            new TwitterAuthenticateTask().execute();
        }
        else
        {
            Intent intent = new Intent(getActivity(), TwitterActivity.class);
            startActivity(intent);
        }
    }

    class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {

        @Override
        protected void onPostExecute(RequestToken requestToken) {
            if (requestToken!=null)
            {

                    Intent intent = new Intent(getActivity().getApplicationContext(), OAuthActivity.class);
                    intent.putExtra(ConstantValues.STRING_EXTRA_AUTHENCATION_URL,requestToken.getAuthenticationURL());
                    startActivity(intent);

            }
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








    boolean handleGeoTagSwitch(android.support.v7.preference.Preference onPreferenceClickListener) {


        return true;
    }

    boolean handleLinkFacebook(android.support.v7.preference.Preference onPreferenceClickListener) {


        return true;
    }

    public final String consumer_key = " iGutaCN8CCRUvrbQHx3gQjrAV";
    public final String secret_key = " s1VaIqE6l1IhlTFToAJXzU6NEAGOruRz5fusk7qpN4PZpoVRWe";
    boolean handleLinkTwitter(android.support.v7.preference.Preference onPreferenceClickListener) {

        logIn();


        return true;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    boolean handleShowMap(android.support.v7.preference.Preference onPreferenceClickListener) {

        Intent intent = new Intent(getActivity(), GeofenceMap.class);
        startActivity(intent);


        return true;
    }



    boolean handleLinkPhone(android.support.v7.preference.Preference onPreferenceClickListener) {

       // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
       // intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        Intent intent = new Intent(getActivity(), ContactsActivity.class);
        startActivity(intent);

        return true;
    }



    }




