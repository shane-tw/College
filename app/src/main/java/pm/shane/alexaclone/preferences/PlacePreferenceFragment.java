package pm.shane.alexaclone.preferences;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
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
import pm.shane.alexaclone.PermissionUtils;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.api.PlaceFilter;
import pm.shane.alexaclone.api.RetrofitManager;
import pm.shane.alexaclone.api.response.Error;
import pm.shane.alexaclone.api.response.GenericResponse;
import pm.shane.alexaclone.api.response.data.Business;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by Shane on 15/11/2017.
 */

public class PlacePreferenceFragment extends PreferenceFragment {

    private String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        type = getArguments().getString("type");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_places, rootKey);
        FragmentCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PermissionUtils.REQUEST_CALL_PHONE);
    }

    public void addTakeaways() {
        LocationManager lm = (LocationManager) MainApp.get().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MainApp.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainApp.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            FragmentCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PermissionUtils.REQUEST_LOCATIONS);
            return;
        }
        if (lm == null) {
            Toast.makeText(MainApp.getContext(), R.string.cant_buildings_location, Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();
            return;
        }
        List<String> providers = lm.getProviders(true);
        Location location = null;
        for (String provider : providers) {
            lm.requestLocationUpdates(provider, 1000, 0, new LocationListener() {

                public void onLocationChanged(Location location) {
                }

                public void onProviderDisabled(String provider) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onStatusChanged(String provider, int status,
                                            Bundle extras) {
                }
            });
            location = lm.getLastKnownLocation(provider);
            if (location != null) {
                break;
            }
        }
        if (location == null) {
            Toast.makeText(MainApp.getContext(), R.string.cant_find_location, Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();
            return;
        }
        RetrofitManager.getService().getPlaces(new PlaceFilter(location.getLongitude(), location.getLatitude(), type))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GenericResponse<List<Business>>>() {
                    @Override
                    public void onSubscribe(Disposable s) {}


                    @Override
                    public void onNext(GenericResponse<List<Business>> businessResponse) {
                        for (Business business : businessResponse.getData()) {
                            CallPreference callPreference = new CallPreference(MainApp.getContext());
                            callPreference.setPhoneNumber(business.getPhoneNumber());
                            callPreference.setTitle(business.getName());
                            getPreferenceScreen().addPreference(callPreference);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t instanceof HttpException) {
                            Response response = ((HttpException)t).response();
                            ResponseBody responseBody = response.errorBody();
                            if (responseBody == null) {
                                getActivity().onBackPressed();
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
                                Toast.makeText(MainApp.getContext(), R.string.failed_understand_response, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(MainApp.getContext(), R.string.network_error_occurred, Toast.LENGTH_LONG).show();
                        }
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void onComplete() {}
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.REQUEST_LOCATIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    addTakeaways();
                } else {
                    Toast.makeText(MainApp.getContext(), R.string.cant_buildings_location, Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                }
                break;
            case PermissionUtils.REQUEST_CALL_PHONE:
                addTakeaways();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
