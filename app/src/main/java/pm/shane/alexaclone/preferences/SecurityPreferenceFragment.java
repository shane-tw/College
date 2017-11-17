package pm.shane.alexaclone.preferences;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.ActivityCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;
import android.widget.Toast;


import pm.shane.alexaclone.MainActivity;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.preferences.locationclasses.ContactsActivity;
import pm.shane.alexaclone.preferences.locationclasses.GeofenceMap;
import pm.shane.alexaclone.preferences.locationclasses.LocationUpdatesListner;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Shane on 28/10/2017.
 */

public class SecurityPreferenceFragment extends PreferenceFragment {

    public static final int RQS_PICK_CONTACT = 9;
    private String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS};
    private final int REQ_PERMISSIONS = 104;

    private static final String TAG = SecurityPreferenceFragment.class.getSimpleName();
    public static final String  PREF_NAME = "locationsharedpreferences";
    public static final String ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH = "ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH";
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


        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQ_PERMISSIONS);

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

    boolean handleLinkTwitter(android.support.v7.preference.Preference onPreferenceClickListener) {


        return true;
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




