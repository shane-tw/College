package pm.shane.alexaclone.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.WeatherController;

public class WeatherActivity extends AppCompatActivity {

    TextView cityText, currentTemperatureText, humidityText, pressureText, updatedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_weather);

        /*LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String lat=Double.toString(location.getLatitude());
        String lon=Double.toString(location.getLongitude());
		*/

        cityText = (TextView)findViewById(R.id.cityText);
        currentTemperatureText = (TextView)findViewById(R.id.currentTemperatureText);
        humidityText = (TextView)findViewById(R.id.humidityText);
        pressureText = (TextView)findViewById(R.id.pressureText);
        updatedText = (TextView)findViewById(R.id.updatedText);

        WeatherController.placeIdTask asyncTask =new WeatherController.placeIdTask(new WeatherController.AsyncResponse() {
            public void processFinish(String city, String temperature, String humidity, String pressure, String updated) {

                cityText.setText(city);
                currentTemperatureText.setText(temperature);
                humidityText.setText("Humidity: "+humidity);
                pressureText.setText("Pressure: "+pressure);
                updatedText.setText(updated);
            }
        });
        asyncTask.execute("51.883956", "-8.533809"); //use lat and lon variables from location manager above

    }
}
