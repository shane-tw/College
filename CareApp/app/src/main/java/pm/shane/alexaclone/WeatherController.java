package pm.shane.alexaclone;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dave on 08/12/2017.
 */

public class WeatherController {
    private static final String URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=%s";
    private static final String API_CODE = "254b8429b64dde6f152a98ef747f0e7b";

    public interface AsyncResponse {
        void processFinish(String output1, String output2, String output3, String output4, String output5);
    }

    public static class placeIdTask extends AsyncTask<String, Void, JSONObject> {

        public AsyncResponse delegate = null; //Call back interface

        public placeIdTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interface through constructor
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jsonWeatherObject = null;
            try {
                jsonWeatherObject = getJSON(params[0], params[1]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process results", e);
            }
            return jsonWeatherObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonWeatherObject) {
            try {
                if(jsonWeatherObject != null){
                    JSONObject details = jsonWeatherObject.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = jsonWeatherObject.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();

                    String city = jsonWeatherObject.getString("name").toUpperCase(Locale.ENGLISH) + ", " + jsonWeatherObject.getJSONObject("sys").getString("country");
                    String temperature = String.format("%.1f", main.getDouble("temp"))+ "°C";
                    String humidity = main.getString("humidity") + "%";
                    String pressure = main.getString("pressure") + " hPa";
                    String updatedOn = df.format(new Date(jsonWeatherObject.getLong("dt")*1000));

                    delegate.processFinish(city, temperature, humidity, pressure, updatedOn);

                }
            } catch (JSONException e) {
                Log.d("Error", "Cannot process results", e);
            }
        }
    }


    public static JSONObject getJSON(String lat, String lon){
        try {
            java.net.URL url = new URL(String.format(URL, lat, lon,API_CODE ));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String temp="";
            while((temp=reader.readLine())!=null)
                json.append(temp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            if(data.getInt("cod") != 200){ //error is 404
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }

}
