package pm.shane.alexaclone.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.PermissionUtils;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.SpeechRecognizerManager;
import pm.shane.alexaclone.activities.Game.GameActivity;
import pm.shane.alexaclone.services.CameraService;

public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        if (savedInstanceState == null) {
            PermissionUtils.requestCameraPermission(this);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.REQUEST_CAMERA_CODE:
                if (grantResults[0] != PermissionChecker.PERMISSION_GRANTED) {
                    Toast.makeText(MainApp.getContext(), R.string.couldnt_open_camera, Toast.LENGTH_SHORT).show();
                    return;
                }
                startService(new Intent(MainApp.getContext(), CameraService.class));
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onFindCarerClicked(View view) {
        startActivity(new Intent(MainApp.getContext(), FindCarerActivity.class));
    }
}
