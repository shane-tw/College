package pm.shane.alexaclone;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.integreight.onesheeld.sdk.OneSheeldDevice;
import com.integreight.onesheeld.sdk.OneSheeldSdk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pm.shane.alexaclone.activities.Game.GameActivity;
import pm.shane.alexaclone.activities.MusicActivity;
import pm.shane.alexaclone.activities.PlaceActivity;
import pm.shane.alexaclone.activities.ShoppingActivity;
import pm.shane.alexaclone.activities.WeatherActivity;

/**
 * Created by Shane on 17/09/2017.
 */

public class MainApp extends Application implements SpeechRecognizerManager.OnResultListener{

    private static Application mInstance;
    private static OneSheeldDevice device;
    private static TextToSpeech tts;
    private static boolean canSpeak = false;
    private static boolean isLogedin = false;
    private static SpeechRecognizerManager mSpeechRecognizerManager;
    private int MY_DATA_CHECK_CODE = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        OneSheeldSdk.init(getContext());
        setmSpeechRecognizerManager();
        tts = new TextToSpeech(getContext(), (int status) -> {
            if (status != TextToSpeech.SUCCESS) {
                Log.e("error", "Initialisation Failed!");
                return;
            }
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
            || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("error", "This Language is not supported");
                return;
            }
            canSpeak = true;
        });
    }

    public static void setIsLogedin(boolean check){
        isLogedin = check;
       // setmSpeechRecognizerManager();
    }

    public static boolean canSpeak() {
        return canSpeak;
    }

    public static void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public static Application get() {
        return mInstance;
    }

    public static Context getContext() {
        return (Context)mInstance;
    }

    public static OneSheeldDevice getConnectedDevice() {
        return device;
    }

    public static void setConnectedDevice(OneSheeldDevice device) {
        MainApp.device = device;
    }

    public void setmSpeechRecognizerManager(){
        mSpeechRecognizerManager = new SpeechRecognizerManager(getContext());
        mSpeechRecognizerManager.setOnResultListner(this);
    }

    private void recognition(String text){
        Log.e("Speech",""+text);
        String[] speech = text.split(" ");

        if(text.contains("what") && text.contains("time")){
            SimpleDateFormat digitalTime = new SimpleDateFormat("HH:mm", Locale.US);
            SimpleDateFormat analogTime = new SimpleDateFormat("HH:mm a", Locale.US);
            Date now = new Date();
            speak("The time is " + digitalTime.format(now) + " .or " + analogTime.format(now));
        }

        if(text.contains("what") && text.contains("date")){
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
            String formattedDate = df.format(c.getTime());
            speak(formattedDate);

        }

        if(text.contains("weather")){
            //speak("it is probably raining");
            // return conditions of the weather
            startActivity(new Intent(getContext(), WeatherActivity.class));
        }

        if(text.contains("play") && text.contains("game")){
            //for the memory game
            startActivity(new Intent(getContext(), GameActivity.class));
        }

        if(text.contains("play") && text.contains("music")){
            //for music
            startActivity(new Intent(getContext(), MusicActivity.class));
        }

        if(text.contains("lights")) {
            if (text.contains("turn on")) {
                if(MainApp.getConnectedDevice() == null){
                    speak("No device connected");
                }else{
                    speak("lights turned on");
                    MainApp.getConnectedDevice().digitalWrite(6, true);
                }
            } else if (text.contains("turn off")) {
                if(MainApp.getConnectedDevice() == null){
                    speak("no device connected");
                }else{
                    speak("lights turned off");
                    MainApp.getConnectedDevice().digitalWrite(6, false);
                }
            }
        }

        if(text.contains("heating")){
            if(text.contains("turn on")){
                if(MainApp.getConnectedDevice() == null){
                    speak("No device connected");
                }else{
                    speak("heating turned on");
                    MainApp.getConnectedDevice().digitalWrite(7, true);
                }
            }
            else if(text.contains("turn off")){
                if(MainApp.getConnectedDevice() == null){
                    speak("No device connected");
                }else {
                    speak("heating turned off");
                    MainApp.getConnectedDevice().digitalWrite(7, false);
                }
            }
        }

        if(text.contains("order")){
            if(text.contains("taxi")){
                speak("Choose taxi service");
                Intent intent = new Intent(MainApp.getContext(), PlaceActivity.class);
                intent.putExtra("type", "taxi");
                startActivity(intent);
            }else if (text.contains("take away")){
                speak("Choose Take away");
                Intent intent = new Intent(MainApp.getContext(), PlaceActivity.class);
                intent.putExtra("type", "take-away");
                startActivity(intent);
                // ring take away
            }else if(text.contains("shopping")){
                Intent myIntent = new Intent(this, ShoppingActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(myIntent);
            }
        }

        // this is to be removed
        /*if(text.contains("wake me up at") || text.contains("alarm")){
            speak(speech[speech.length-1]);
            String[] time = speech[speech.length-1].split(":");
            String hour = time[0];
            String minutes = time[1];
            Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
            i.putExtra(AlarmClock.EXTRA_HOUR, Integer.valueOf(hour));
            i.putExtra(AlarmClock.EXTRA_MINUTES, Integer.valueOf(minutes));
            startActivity(i);
            speak("Setting alarm to ring at " + hour + ":" + minutes);
        }

        if(text.contains("thank you")){
            speak("Thank you too ");
        }*/
    }

    @Override
    public void OnResult(ArrayList<String> commands) {
        StringBuilder text = new StringBuilder("");
        for(String command:commands) {
            text.append(command).append(" ");
        }
        if(isLogedin){
            recognition(text.toString());
        }
        text = null;
    }
}

