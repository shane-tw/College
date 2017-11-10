package pm.shane.alexaclone.preferences;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;


import pm.shane.alexaclone.MainActivity;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.preferences.locationclasses.LocationUpdatesListner;



/**
 * Created by Shane on 28/10/2017.
 */

public class SecurityPreferenceFragment extends PreferenceFragment {

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

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPref.edit();

        findPreference("geotag_switch").setOnPreferenceClickListener(this::handleGeoTagSwitch);
        findPreference("link_facebook_btn").setOnPreferenceClickListener(this::handleLinkFacebook);
        findPreference("link_twitter_btn").setOnPreferenceClickListener(this::handleLinkTwitter);
        findPreference("show_map").setOnPreferenceClickListener(this::handleShowMap);

        SwitchPreference notification = (android.support.v14.preference.SwitchPreference) findPreference("notifications_new_message");
        notification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((Boolean) newValue) {
                    Log.i(TAG, "enable notification");
                    editor.putBoolean( ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH,  true);

                    editor.apply();

                } else {
                    Log.i(TAG, "disable notification");
                    editor.putBoolean( ENABLE_NOTIFICATION_ON_GEOFENCE_BREACH,  false);

                    editor.apply();
                }


                return true;
            }
        });


        findPreference("link_phone").setOnPreferenceClickListener(this::handleLinkPhone);

        getActivity().startService(new Intent(getActivity(), LocationUpdatesListner.class));

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



        return true;
    }



    boolean handleLinkPhone(android.support.v7.preference.Preference onPreferenceClickListener) {


        return true;
    }




}