package pm.shane.alexaclone;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
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

    private Button connectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(MainApp.getContext(), AlexaService.class));
        connectBtn = new Button(MainApp.getContext());
        getConnectBtn().setId(R.id.connectBtn);
        getConnectBtn().setText(R.string.connect_device);
        getConnectBtn().setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        getConnectBtn().setBackgroundResource(R.color.colorAccent);
        getConnectBtn().setTextColor(Color.parseColor("#ffffff"));
        getListView().addFooterView(connectBtn);
        getListView().setDivider(ContextCompat.getDrawable(this, R.drawable.preference_list_divider_material));

        if (MainApp.getConnectedDevice() != null) {
            getConnectBtn().setText(R.string.disconnect_device);
            return;
        }

        OneSheeldManager manager = OneSheeldSdk.getManager();
        manager.setConnectionRetryCount(1);
        manager.setAutomaticConnectingRetriesForClassicConnections(true);
        OneSheeldScanningCallback scanningCallback = new OneSheeldScanningCallback() {
            @Override
            public void onScanStart() {
                super.onScanStart();
                Toast.makeText(MainApp.getContext(), R.string.started_scan, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceFind(OneSheeldDevice device) {
                OneSheeldSdk.getManager().cancelScanning();
                runOnUiThread(() -> {
                    Toast.makeText(MainApp.getContext(), R.string.found_device, Toast.LENGTH_SHORT).show();
                });
                device.connect();
            }

            @Override
            public void onScanFinish(List<OneSheeldDevice> foundDevices) {
                if (!foundDevices.isEmpty()) {
                    return;
                }
                runOnUiThread(() -> {
                    Toast.makeText(MainApp.getContext(), R.string.failed_find_device, Toast.LENGTH_SHORT).show();
                });
            }
        };
        OneSheeldConnectionCallback connectionCallback = new OneSheeldConnectionCallback() {
            @Override
            public void onConnect(final OneSheeldDevice device) {
                MainApp.setConnectedDevice(device);
                device.pinMode(7, OneSheeldDevice.OUTPUT);
                runOnUiThread(() -> {
                    Toast.makeText(MainApp.getContext(), "Connected!", Toast.LENGTH_SHORT).show();
                    getConnectBtn().setText(R.string.disconnect_device);
                });
            }
        };
        manager.addConnectionCallback(connectionCallback);
        manager.addScanningCallback(scanningCallback);

        getConnectBtn().setOnClickListener((View view) -> {
            if (MainApp.getConnectedDevice() != null) {
                manager.disconnectAll();
                MainApp.setConnectedDevice(null);
                getConnectBtn().setText(R.string.connect_device);
                return;
            }
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Toast.makeText(MainApp.getContext(), R.string.bluetooth_not_supported, Toast.LENGTH_LONG).show();
                return;
            } else if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
            if (PermissionChecker.checkSelfPermission(MainApp.getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PermissionUtils.REQUEST_LOCATION);
            } else {
                Toast.makeText(MainApp.getContext(), "About to start scan.", Toast.LENGTH_SHORT).show();
                manager.scan();
            }
        });
    }

    public Button getConnectBtn() {
        return connectBtn;
    }

    @Override
    public boolean onIsMultiPane() {
        return (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers_main, target);
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
            case PermissionUtils.REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    OneSheeldSdk.getManager().scan();
                } else {
                    Toast.makeText(MainApp.getContext(), "Location permissions must be granted in order to connect to the device.", Toast.LENGTH_LONG).show();
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
