package pm.shane.alexaclone.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import pm.shane.alexaclone.AppCompatPreferenceActivity;

/**
 * Created by Shane on 28/10/2017.
 */

public abstract class PreferenceFragment extends android.support.v14.preference.PreferenceFragment {

    private void showBackButton() {
        Activity activity = getActivity();
        if (!(activity instanceof AppCompatPreferenceActivity)) {
            return;
        }
        AppCompatPreferenceActivity preferenceActivity = (AppCompatPreferenceActivity)activity;
        ActionBar actionBar = ((AppCompatPreferenceActivity)activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(!preferenceActivity.onIsMultiPane());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButton();
    }

}
