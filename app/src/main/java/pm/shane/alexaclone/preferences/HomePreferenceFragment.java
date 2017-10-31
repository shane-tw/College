package pm.shane.alexaclone.preferences;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.R;

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
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a", Locale.US);
            if (MainApp.canSpeak()) {
                MainApp.speak(String.format("The time is %s.", sdf.format(cal.getTime())));
            }
            Toast.makeText(MainApp.getContext(), String.format("The time is %s.", sdf.format(cal.getTime())), Toast.LENGTH_LONG).show();
            return false;
        });
        findPreference("lights_switch").setOnPreferenceChangeListener((Preference preference, Object newValue) -> {
            if (!(newValue instanceof Boolean)) {
                return false;
            }
            if (MainApp.getConnectedDevice() == null) {
                Toast.makeText(MainApp.getContext(), "You need to connect to device first.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainApp.getContext(), "You need to connect to device first.", Toast.LENGTH_SHORT).show();
                return false;
            }
            boolean booleanValue = (Boolean)newValue;
            MainApp.getConnectedDevice().digitalWrite(7, booleanValue);
            return true;
        });
    }
}