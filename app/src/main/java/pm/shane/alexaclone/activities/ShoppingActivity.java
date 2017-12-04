package pm.shane.alexaclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.activities.shopping.ElectronicsActivity;
import pm.shane.alexaclone.activities.shopping.FashionActivity;
import pm.shane.alexaclone.activities.shopping.GroceriesActivity;
import pm.shane.alexaclone.activities.shopping.HomewareActivity;

/**
 * Created by Shane on 28/10/2017.
 */

public class ShoppingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        showBackButton();
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

    private void showBackButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onGroceriesClicked(View view) {
        startActivity(new Intent(MainApp.getContext(), GroceriesActivity.class));
    }

    public void onElectronicsClicked(View view) {
        startActivity(new Intent(MainApp.getContext(), ElectronicsActivity.class));
    }

    public void onFashionClicked(View view) {
        startActivity(new Intent(MainApp.getContext(), FashionActivity.class));
    }

    public void onHomewareClicked(View view) {
        startActivity(new Intent(MainApp.getContext(), HomewareActivity.class));
    }

}
