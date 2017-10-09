package pm.shane.alexaclone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.integreight.onesheeld.sdk.OneSheeldConnectionCallback;
import com.integreight.onesheeld.sdk.OneSheeldDevice;
import com.integreight.onesheeld.sdk.OneSheeldManager;
import com.integreight.onesheeld.sdk.OneSheeldScanningCallback;
import com.integreight.onesheeld.sdk.OneSheeldSdk;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION = 101; // Code for coarse pref_location permission

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (MainApp.getConnectedDevice() != null) {
            //((ToggleButton)findViewById(R.id.toggleButton)).setChecked(checked);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainApp.getContext(), "Found OneSheeld.", Toast.LENGTH_SHORT).show();
                    }
                });
                device.connect();
            }

            @Override
            public void onScanFinish(List<OneSheeldDevice> foundDevices) {
                if (!foundDevices.isEmpty()) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainApp.getContext(), "Failed to find OneSheeld.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        OneSheeldConnectionCallback connectionCallback = new OneSheeldConnectionCallback() {
            @Override
            public void onConnect(final OneSheeldDevice device) {
                MainApp.setConnectedDevice(device);
                startService(new Intent(MainApp.getContext(), AlexaService.class));
                device.pinMode(7, OneSheeldDevice.OUTPUT);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainApp.getContext(), "Connected!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        manager.addConnectionCallback(connectionCallback);
        manager.addScanningCallback(scanningCallback);

        if (ContextCompat.checkSelfPermission(MainApp.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
        } else {
            manager.scan();
        }
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

    public void onLightsChanged(View actionView) {
        if (!(actionView instanceof ToggleButton)) {
            return;
        }
        MainApp.getConnectedDevice().digitalWrite(7, ((ToggleButton) actionView).isChecked());
        boolean checked = MainApp.getConnectedDevice().digitalRead(7);
        Toast.makeText(MainApp.getContext(), Boolean.toString(checked), Toast.LENGTH_SHORT).show();
    }
}
