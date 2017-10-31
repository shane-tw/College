package pm.shane.alexaclone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.integreight.onesheeld.sdk.OneSheeldConnectionCallback;
import com.integreight.onesheeld.sdk.OneSheeldDevice;
import com.integreight.onesheeld.sdk.OneSheeldManager;
import com.integreight.onesheeld.sdk.OneSheeldScanningCallback;
import com.integreight.onesheeld.sdk.OneSheeldSdk;

import java.util.List;

import pm.shane.alexaclone.preferences.HomePreferenceFragment;
import pm.shane.alexaclone.preferences.MedicalPreferenceFragment;
import pm.shane.alexaclone.preferences.SecurityPreferenceFragment;
import pm.shane.alexaclone.preferences.SettingsPreferenceFragment;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private static final int REQUEST_CODE_LOCATION = 101;
    private Button connectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setupActionBar();
        connectBtn = new Button(MainApp.getContext());
        connectBtn.setId(R.id.connectBtn);
        connectBtn.setText("Connect Device");
        connectBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        connectBtn.setBackgroundResource(R.color.colorAccent);
        connectBtn.setTextColor(Color.parseColor("#ffffff"));
        getListView().addFooterView(connectBtn);
        getListView().setDivider(ContextCompat.getDrawable(this, R.drawable.preference_list_divider_material));
        connectBtn = findViewById(R.id.connectBtn);

        if (MainApp.getConnectedDevice() != null) {
            getConnectBtn().setText("Disconnect Device");
            // TODO: Change button text and colour maybe
            return;
        }

        OneSheeldManager manager = OneSheeldSdk.getManager();
        manager.setConnectionRetryCount(1);
        manager.setAutomaticConnectingRetriesForClassicConnections(true);
        OneSheeldScanningCallback scanningCallback = new OneSheeldScanningCallback() {
            @Override
            public void onScanStart() {
                super.onScanStart();
                Toast.makeText(MainApp.getContext(), "Started scan.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceFind(OneSheeldDevice device) {
                OneSheeldSdk.getManager().cancelScanning();
                runOnUiThread(() -> {
                    Toast.makeText(MainApp.getContext(), "Found OneSheeld.", Toast.LENGTH_SHORT).show();
                });
                device.connect();
            }

            @Override
            public void onScanFinish(List<OneSheeldDevice> foundDevices) {
                if (!foundDevices.isEmpty()) {
                    return;
                }
                runOnUiThread(() -> {
                    Toast.makeText(MainApp.getContext(), "Failed to find OneSheeld.", Toast.LENGTH_SHORT).show();
                });
            }
        };
        OneSheeldConnectionCallback connectionCallback = new OneSheeldConnectionCallback() {
            @Override
            public void onConnect(final OneSheeldDevice device) {
                MainApp.setConnectedDevice(device);
                startService(new Intent(MainApp.getContext(), AlexaService.class));
                device.pinMode(7, OneSheeldDevice.OUTPUT);
                runOnUiThread(() -> {
                    Toast.makeText(MainApp.getContext(), "Connected!", Toast.LENGTH_SHORT).show();
                    getConnectBtn().setText("Disconnect Device");
                    // TODO: Change button text and colour maybe
                });
            }
        };
        manager.addConnectionCallback(connectionCallback);
        manager.addScanningCallback(scanningCallback);

        getConnectBtn().setOnClickListener((View view) -> {
            if (MainApp.getConnectedDevice() != null) {
                manager.disconnectAll();
                MainApp.setConnectedDevice(null);

                getConnectBtn().setText("Connect Device");
                return;
            }
            if (PermissionChecker.checkSelfPermission(MainApp.getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
            } else {
                Toast.makeText(MainApp.getContext(), "About to start scan.", Toast.LENGTH_SHORT).show();
                manager.scan();
            }
        });
    }

    public Button getConnectBtn() {
        return connectBtn;
    }

    public void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(!onIsMultiPane());
    }

    @Override
    public boolean onIsMultiPane() {
        return (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || HomePreferenceFragment.class.getName().equals(fragmentName)
                || MedicalPreferenceFragment.class.getName().equals(fragmentName)
                || SecurityPreferenceFragment.class.getName().equals(fragmentName)
                || SettingsPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    OneSheeldSdk.getManager().scan();
                } else {
                    Toast.makeText(MainApp.getContext(), "Permission denied.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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



}
