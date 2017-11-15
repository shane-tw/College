package pm.shane.alexaclone.preferences;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.preference.Preference;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.ShoppingActivity;

/**
 * Created by Shane on 28/10/2017.
 */

public class HomePreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_home, rootKey);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findPreference("current_time_btn").setOnPreferenceClickListener((android.support.v7.preference.Preference onPreferenceClickListener) -> {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
            if (MainApp.canSpeak()) {
                MainApp.speak(getString(R.string.the_time_is, sdf.format(cal.getTime())));
            }
            Toast.makeText(MainApp.getContext(), getString(R.string.the_time_is, sdf.format(cal.getTime())), Toast.LENGTH_LONG).show();
            return true;
        });
        findPreference("lights_switch").setOnPreferenceChangeListener((Preference preference, Object newValue) -> {
            if (!(newValue instanceof Boolean)) {
                return false;
            }
            if (MainApp.getConnectedDevice() == null) {
                Toast.makeText(MainApp.getContext(), R.string.connect_device_first, Toast.LENGTH_SHORT).show();
                return false;
            }
            boolean booleanValue = (Boolean)newValue;
            MainApp.getConnectedDevice().digitalWrite(6, booleanValue);
            return true;
        });
        findPreference("heating_switch").setOnPreferenceChangeListener((Preference preference, Object newValue) -> {
            if (!(newValue instanceof Boolean)) {
                return false;
            }
            if (MainApp.getConnectedDevice() == null) {
                Toast.makeText(MainApp.getContext(), R.string.connect_device_first, Toast.LENGTH_SHORT).show();
                return false;
            }
            boolean booleanValue = (Boolean)newValue;
            MainApp.getConnectedDevice().digitalWrite(7, booleanValue);
            return true;
        });
        findPreference("order_shopping_btn").setOnPreferenceClickListener((android.support.v7.preference.Preference onPreferenceClickListener) -> {
            Intent myIntent = new Intent(getActivity(), ShoppingActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(myIntent);
            return true;
        });
        findPreference("order_takeaway_btn").setOnPreferenceClickListener((android.support.v7.preference.Preference onPreferenceClickListener) -> {
            Bundle args = new Bundle();
            args.putString("type", "take-away");
            Fragment takeawayFragment = new PlacePreferenceFragment();
            takeawayFragment.setArguments(args);
            ((PreferenceActivity) getActivity()).startPreferenceFragment(takeawayFragment, true);
            return true;
        });
        findPreference("order_taxi_btn").setOnPreferenceClickListener((android.support.v7.preference.Preference onPreferenceClickListener) -> {
            Bundle args = new Bundle();
            args.putString("type", "taxi");
            Fragment takeawayFragment = new PlacePreferenceFragment();
            takeawayFragment.setArguments(args);
            ((PreferenceActivity) getActivity()).startPreferenceFragment(takeawayFragment, true);
            return true;
        });
    }
}