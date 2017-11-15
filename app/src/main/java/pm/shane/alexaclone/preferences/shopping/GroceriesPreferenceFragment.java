package pm.shane.alexaclone.preferences.shopping;

import android.os.Bundle;

import pm.shane.alexaclone.R;
import pm.shane.alexaclone.preferences.PreferenceFragment;

/**
 * Created by Shane on 14/11/2017.
 */

public class GroceriesPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_shopping_groceries, rootKey);
    }
}
