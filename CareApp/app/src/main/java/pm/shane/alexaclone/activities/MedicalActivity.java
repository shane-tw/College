package pm.shane.alexaclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.activities.Game.GameActivity;

/**
 * Created by Shane on 28/10/2017.
 */

public class MedicalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);
        showBackButton();
    }

    private void showBackButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onPlayMemoryGameClicked(View view) {
        startActivity(new Intent(this, GameActivity.class));
    }

    public void onViewCalendarClicked(View view) {
        startActivity(new Intent(this, CalenderActivity.class));
    }

}
