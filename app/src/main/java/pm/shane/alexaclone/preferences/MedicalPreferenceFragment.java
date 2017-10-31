package pm.shane.alexaclone.preferences;

import android.os.Bundle;

import pm.shane.alexaclone.R;

/**
 * Created by Shane on 28/10/2017.
 */

public class MedicalPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_medical, rootKey);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
