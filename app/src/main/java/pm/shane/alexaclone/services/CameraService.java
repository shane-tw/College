package pm.shane.alexaclone.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.SessionManager;
import pm.shane.alexaclone.api.RetrofitManager;
import pm.shane.alexaclone.api.response.Error;
import pm.shane.alexaclone.api.response.GenericResponse;
import pm.shane.alexaclone.api.response.data.Patient;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by Shane.
 */

public class CameraService extends Service {

    private Handler handler = new Handler();
    private Camera mCamera;
    private Camera.Parameters params;
    private TakeImage takeImage;

    @Override
    public void onCreate() {
        super.onCreate();
        takeImage = new TakeImage();
        takeImage.execute();
    }

    private boolean setPictureResolution() {
        android.hardware.Camera.Size pictureSize = getOptimalPictureResolution();
        if (pictureSize == null) {
            return false;
        }
        params.setPictureSize(pictureSize.width, pictureSize.height);
        return true;
    }

    private android.hardware.Camera.Size getOptimalPictureResolution() {
        List<android.hardware.Camera.Size> sizeList = params.getSupportedPictureSizes();
        if (sizeList.size() == 0) {
            return null;
        }
        android.hardware.Camera.Size largestSize = sizeList.get(0);
        for (android.hardware.Camera.Size size : sizeList) {
            if (size.height < largestSize.height && size.height >= 720) {
                largestSize = size;
            }
        }
        return largestSize;
    }

    private boolean deviceHasCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private class TakeImage extends AsyncTask<Object,Void,Void> {
        @Override
        protected Void doInBackground(Object... params) {
            while (true) {
                takeImage();
                try {
                    Thread.sleep(25 * 1000);
                } catch (InterruptedException e) {
                    handler.post(() -> {
                        Toast.makeText(getApplicationContext(), R.string.camera_sleep_interrupted, Toast.LENGTH_LONG).show();
                    });
                }
            }
        }
    }

    private synchronized void takeImage() {
        if (!deviceHasCamera(getApplicationContext())) {
            handler.post(() -> {
                Toast.makeText(getApplicationContext(), R.string.device_has_no_camera, Toast.LENGTH_LONG).show();
            });
            stopSelf();
            return;
        }
        mCamera = getCameraInstance();
        if (mCamera == null) {
            handler.post(() -> {
                Toast.makeText(getApplicationContext(), R.string.camera_unavailable, Toast.LENGTH_LONG).show();
            });
            return;
        }
        try {
            mCamera.setPreviewTexture(new SurfaceTexture(0));
        } catch (IOException e) {
            handler.post(() -> {
                Toast.makeText(getApplicationContext(), R.string.failed_set_camera_display, Toast.LENGTH_LONG).show();
            });
            return;
        }
        params = mCamera.getParameters();
        if (!setPictureResolution()) {
            handler.post(() -> {
                Toast.makeText(getApplicationContext(), R.string.camera_has_no_resolutions, Toast.LENGTH_LONG).show();
            });
            stopSelf();
            return;
        }
        mCamera.setParameters(params);
        mCamera.startPreview();
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            handler.post(() -> {
                Toast.makeText(getApplicationContext(), R.string.camera_sleep_interrupted, Toast.LENGTH_LONG).show();
            });
            mCamera.release();
            return;
        }
        mCamera.takePicture(null, null, mCall);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    android.hardware.Camera.PictureCallback mCall = (byte[] data, android.hardware.Camera camera) -> {
        Patient me = new Patient();
        me.getRemoteCamera().setLastPicture(data);
        RetrofitManager.getService().me(me)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<GenericResponse<Patient>>() {
                @Override
                public void onSubscribe(Disposable s) {}

                @Override
                public void onNext(GenericResponse<Patient> userResponse) {
                    Toast.makeText(MainApp.getContext(), "Remote camera sent successfully.", Toast.LENGTH_SHORT).show();
                    SessionManager.setLoggedInUser(userResponse.getData());
                }

                @Override
                public void onError(Throwable t) {
                    if (!(t instanceof HttpException)) {
                        Toast.makeText(MainApp.getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Response response = ((HttpException) t).response();
                    ResponseBody responseBody = response.errorBody();
                    if (responseBody == null) {
                        return;
                    }
                    Converter<ResponseBody, GenericResponse<Void>> converter = RetrofitManager.getRetrofit().responseBodyConverter(GenericResponse.class, new Annotation[0]);
                    GenericResponse<Void> genericResponse;
                    try {
                        genericResponse = converter.convert(responseBody);
                        for (Error error : genericResponse.getErrors()) {
                            Toast.makeText(MainApp.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException | JsonSyntaxException e) {
                        Toast.makeText(MainApp.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }


                @Override
                public void onComplete() {}
            });
        mCamera.release();
        mCamera = null;
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public android.hardware.Camera getCameraInstance() {
        android.hardware.Camera camera = null;
        try {
            camera = android.hardware.Camera.open();
        } catch (RuntimeException e) {
            handler.post(() -> {
                Toast.makeText(getApplicationContext(), R.string.couldnt_open_camera, Toast.LENGTH_SHORT).show();
            });
            stopSelf();
        }
        return camera;
    }

    @Override
    public void onDestroy() {
        takeImage.cancel(true);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        super.onDestroy();
    }

}