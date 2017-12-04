package pm.shane.alexaclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.R;

public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void onHomeClicked(View view) {
        startActivity(new Intent(MainApp.getContext(), HomeActivity.class));
    }

    public void onMedicalClicked(View view) {
        startActivity(new Intent(MainApp.getContext(), MedicalActivity.class));
    }

    public void onLocationClicked(View view) {
        startActivity(new Intent(MainApp.getContext(), LocationActivity.class));
    }

    public void onSettingsClicked(View view) {
        startActivity(new Intent(MainApp.getContext(), SettingsActivity.class));
    }
}
