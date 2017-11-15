package pm.shane.alexaclone;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import java.util.List;

import pm.shane.alexaclone.preferences.shopping.ElectronicsPreferenceFragment;
import pm.shane.alexaclone.preferences.shopping.FashionPreferenceFragment;
import pm.shane.alexaclone.preferences.shopping.GroceriesPreferenceFragment;
import pm.shane.alexaclone.preferences.shopping.HomewarePreferenceFragment;

public class ShoppingActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getListView().setDivider(ContextCompat.getDrawable(this, R.drawable.preference_list_divider_material));
        showBackButton();
    }

    @Override
    public boolean onIsMultiPane() {
        return (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers_shopping, target);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return GroceriesPreferenceFragment.class.getName().equals(fragmentName)
                || ElectronicsPreferenceFragment.class.getName().equals(fragmentName)
                || FashionPreferenceFragment.class.getName().equals(fragmentName)
                || HomewarePreferenceFragment.class.getName().equals(fragmentName);
    }

    private void showBackButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(!onIsMultiPane());
        }
    }
}
