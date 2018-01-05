package pm.shane.alexaclone;

import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.integreight.onesheeld.sdk.OneSheeldConnectionCallback;
import com.integreight.onesheeld.sdk.OneSheeldDevice;
import com.integreight.onesheeld.sdk.OneSheeldError;
import com.integreight.onesheeld.sdk.OneSheeldErrorCallback;
import com.integreight.onesheeld.sdk.OneSheeldManager;
import com.integreight.onesheeld.sdk.OneSheeldScanningCallback;
import com.integreight.onesheeld.sdk.OneSheeldSdk;

import java.util.List;

/**
 * Created by Shane.
 */

public class OneSheeldFragment extends Fragment {
    private Button connectBtn;
    private Handler handler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        OneSheeldManager manager = OneSheeldSdk.getManager();
        manager.setScanningTimeOut(8);
        OneSheeldScanningCallback scanningCallback = new OneSheeldScanningCallback() {
            @Override
            public void onScanStart() {
                super.onScanStart();
                handler.post(() -> {
                    Toast.makeText(MainApp.getContext(), R.string.started_scan, Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onDeviceFind(OneSheeldDevice device) {
                handler.post(() -> {
                    Toast.makeText(MainApp.getContext(), R.string.found_device, Toast.LENGTH_SHORT).show();
                });
                OneSheeldSdk.getManager().cancelScanning();
                device.connect();
            }

            @Override
            public void onScanFinish(List<OneSheeldDevice> foundDevices) {
                if (!foundDevices.isEmpty()) {
                    return;
                }
                handler.post(() -> {
                    Toast.makeText(MainApp.getContext(), R.string.failed_find_device, Toast.LENGTH_SHORT).show();
                    getConnectBtn().setEnabled(true);
                });
            }
        };
        OneSheeldConnectionCallback connectionCallback = new OneSheeldConnectionCallback() {
            @Override
            public void onConnect(final OneSheeldDevice device) {
                MainApp.setConnectedDevice(device);
                device.pinMode(7, OneSheeldDevice.OUTPUT);
                handler.post(() -> {
                    Toast.makeText(MainApp.getContext(), "Connected!", Toast.LENGTH_SHORT).show();
                    getConnectBtn().setText(R.string.disconnect_device);
                    getConnectBtn().setEnabled(true);
                });
            }
        };
        OneSheeldErrorCallback errorCallback = new OneSheeldErrorCallback() {
            @Override
            public void onError(OneSheeldDevice device, OneSheeldError error) { // TODO: Check the error before deciding to re-enable button?
                if (error == OneSheeldError.DEVICE_NOT_CONNECTED) { // We only want to handle issues scanning/connecting, whereas this error would be a disconnection issue when running pinMode, etc. after connected. We'd need to handle this differently.
                    return;
                }
                handler.post(() -> {
                    Toast.makeText(MainApp.getContext(), R.string.failed_connect_device, Toast.LENGTH_SHORT).show();
                    getConnectBtn().setEnabled(true);
                });
            }
        };
        manager.addScanningCallback(scanningCallback);
        manager.addConnectionCallback(connectionCallback);
        manager.addErrorCallback(errorCallback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_onesheeld, container, false);
        Button tmpConnectBtn = v.findViewById(R.id.connect_btn);
        if (connectBtn != null) {
            tmpConnectBtn.setEnabled(connectBtn.isEnabled());
            tmpConnectBtn.setText(connectBtn.getText());
        } else if (MainApp.getConnectedDevice() != null) {
            tmpConnectBtn.setText(R.string.disconnect_device);
        }
        connectBtn = tmpConnectBtn;
        getConnectBtn().setOnClickListener(this::onConnectBtnClicked);
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    OneSheeldSdk.getManager().cancelScanning();
                    OneSheeldSdk.getManager().scan();
                } else {
                    Toast.makeText(MainApp.getContext(), "Location permissions must be granted in order to connect to the device.", Toast.LENGTH_LONG).show();
                    getConnectBtn().setEnabled(true);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public Button getConnectBtn() {
        return connectBtn;
    }

    public void onConnectBtnClicked(View view) {
        getConnectBtn().setEnabled(false);
        if (MainApp.getConnectedDevice() != null) {
            OneSheeldSdk.getManager().disconnectAll();
            MainApp.setConnectedDevice(null);
            getConnectBtn().setText(R.string.connect_device);
            getConnectBtn().setEnabled(true);
            return;
        }
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(MainApp.getContext(), R.string.bluetooth_not_supported, Toast.LENGTH_LONG).show();
            getConnectBtn().setEnabled(true);
            return;
        } else if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        if (PermissionChecker.checkSelfPermission(MainApp.getContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PermissionUtils.REQUEST_LOCATION);
        } else {
            Toast.makeText(MainApp.getContext(), "About to start scan.", Toast.LENGTH_SHORT).show();
            OneSheeldSdk.getManager().cancelScanning();
            OneSheeldSdk.getManager().scan();
        }
    }
}
