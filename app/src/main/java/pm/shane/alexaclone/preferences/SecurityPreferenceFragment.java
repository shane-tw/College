package pm.shane.alexaclone.preferences;

import android.content.Intent;
import android.os.Bundle;

import pm.shane.alexaclone.MainActivity;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.preferences.locationclasses.LocationUpdatesListner;

import static com.google.android.gms.cast.CastRemoteDisplayLocalService.startService;

/**
 * Created by Shane on 28/10/2017.
 */

public class SecurityPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_location, rootKey);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findPreference("geotag_switch").setOnPreferenceClickListener(this::handleGeoTagSwitch);
        findPreference("link_facebook_btn").setOnPreferenceClickListener(this::handleLinkFacebook);
        findPreference("link_twitter_btn").setOnPreferenceClickListener(this::handleLinkTwitter);
        findPreference("show_map").setOnPreferenceClickListener(this::handleShowMap);
        findPreference("notifications_new_message").setOnPreferenceClickListener(this::handleNotification);
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

    boolean handleNotification(android.support.v7.preference.Preference onPreferenceClickListener) {


        return true;
    }

    boolean handleLinkPhone(android.support.v7.preference.Preference onPreferenceClickListener) {


        return true;
    }




}