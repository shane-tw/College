package pm.shane.alexaclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;

import pm.shane.alexaclone.LoginActivity;
import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.PermissionUtils;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.SessionManager;

/**
 * Created by Shane.
 */

public class ModeActivity extends AppCompatActivity {
    private RadioGroup selectedMode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(pm.shane.alexaclone.R.layout.activity_mode);
        selectedMode = findViewById(R.id.selected_mode);
        PermissionUtils.requestMicPermission(this);
        PermissionUtils.requestCalPermission(this);
    }

    public void onModeSubmit(View view) {
        switch (selectedMode.getCheckedRadioButtonId()) {
            case R.id.standalone_radio:
                SessionManager.setLoggedInUser(null);
                startActivity(new Intent(MainApp.getContext(), DashboardActivity.class));
                break;
            case R.id.supervisory_radio:
                startActivity(new Intent(MainApp.getContext(), LoginActivity.class));
                break;
        }
    }
}
