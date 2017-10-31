package pm.shane.alexaclone.preferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import pm.shane.alexaclone.LoginActivity;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.SessionManager;

/**
 * Created by Shane on 28/10/2017.
 */

public class SettingsPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_settings, rootKey);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.preference.Preference signOutPreference = findPreference("sign_out_btn");
        signOutPreference.setOnPreferenceClickListener(this::handleSignOut);
    }

    public boolean handleSignOut(android.support.v7.preference.Preference preference) {
        SessionManager.setLoggedIn(false);
        Activity activity = getActivity();
        Intent myIntent = new Intent(activity, LoginActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myIntent);
        return true;
    }

}