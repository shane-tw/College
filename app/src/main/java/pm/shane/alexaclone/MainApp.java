package pm.shane.alexaclone;

import android.*;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.integreight.onesheeld.sdk.OneSheeldDevice;
import com.integreight.onesheeld.sdk.OneSheeldSdk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pm.shane.alexaclone.activities.CalenderActivity;
import pm.shane.alexaclone.activities.Game.GameActivity;
import pm.shane.alexaclone.activities.HomeActivity;
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
    private static SpeechRecognizerManager mSpeechRecognizerManager;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        OneSheeldSdk.init(getContext());
        //setmSpeechRecognizerManager();
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

    public static boolean canSpeak() {
        return canSpeak;
    }

    public static void speak(String text) {
        //mSpeechRecognizerManager.stop();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        //mSpeechRecognizerManager.start();
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

    public static SpeechRecognizerManager getmSpeechRecognizerManager() {
        return mSpeechRecognizerManager;
    }

    public static void startvoice(){
        MainApp i = new MainApp();
        setmSpeechRecognizerManager(i);
    }

    public static void setmSpeechRecognizerManager(MainApp i){
        mSpeechRecognizerManager = new SpeechRecognizerManager(getContext());
        i.setListener();
    }

    public void setListener(){
        mSpeechRecognizerManager.setOnResultListner(this);
    }

    public void destroySpeech(){
        mSpeechRecognizerManager.destroy();
    }

    private void recognition(String text){
        Log.e("Speech",""+text);
        String[] speech = text.split(" ");

        if(text.contains("what") && text.contains("time")){
            SimpleDateFormat digitalTime = new SimpleDateFormat("HH:mm", Locale.UK);
            SimpleDateFormat analogTime = new SimpleDateFormat("HH:mm a", Locale.UK);
            Date now = new Date();
            speak("The time is " + digitalTime.format(now) + " .or " + analogTime.format(now));
        }

        if(text.contains("what") && text.contains("date")){
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
            String formattedDate = df.format(c.getTime());
            speak(formattedDate);

        }

        if(text.contains("weather")){
            //speak("it is probably raining");
            // return conditions of the weather
            Intent intent = new Intent(MainApp.getContext(), WeatherActivity.class);
            MainApp.getContext().getApplicationContext().startActivity(intent);
        }

        if(text.contains("play") && text.contains("game")){
            //for the memory game
            Log.d("Testing: ", "recognition: " + getContext());
            Intent intent = new Intent(MainApp.getContext(), GameActivity.class);
            MainApp.getContext().getApplicationContext().startActivity(intent);
        }

        if(text.contains("play") && text.contains("music")){
            //for music
            Intent intent = new Intent(MainApp.getContext(), MusicActivity.class);
            MainApp.getContext().getApplicationContext().startActivity(intent);
        }

        if(text.contains("lights")) {
            //TODO change switch in settings
            if (text.contains("turn on")) {
                if(MainApp.getConnectedDevice() == null){
                    speak("No device connected");
                }else{
                    speak("lights turned on");
                    MainApp.getConnectedDevice().digitalWrite(6, true);
                    Intent intent = new Intent(MainApp.getContext(), HomeActivity.class);
                    intent.putExtra("type", "lights-on");
                    MainApp.getContext().getApplicationContext().startActivity(intent);
                }
            } else if (text.contains("turn off")) {
                if(MainApp.getConnectedDevice() == null){
                    speak("no device connected");
                }else{
                    speak("lights turned off");
                    MainApp.getConnectedDevice().digitalWrite(6, false);
                    Intent intent = new Intent(MainApp.getContext(), HomeActivity.class);
                    intent.putExtra("type", "lights-off");
                    MainApp.getContext().getApplicationContext().startActivity(intent);
                }
            }
        }

        if(text.contains("calendar")){
            Intent intent = new Intent(MainApp.getContext(), CalenderActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainApp.getContext().getApplicationContext().startActivity(intent);

        }

        if(text.contains("heating")){
            //TODO change switch in settings
            if(text.contains("turn on")){
                if(MainApp.getConnectedDevice() == null){
                    speak("No device connected");
                }else{
                    speak("heating turned on");
                    MainApp.getConnectedDevice().digitalWrite(7, true);
                    Intent intent = new Intent(MainApp.getContext(), HomeActivity.class);
                    intent.putExtra("type", "heating-on");
                    MainApp.getContext().getApplicationContext().startActivity(intent);
                }
            }
            else if(text.contains("turn off")){
                if(MainApp.getConnectedDevice() == null){
                    speak("No device connected");
                }else {
                    speak("heating turned off");
                    MainApp.getConnectedDevice().digitalWrite(7, false);
                    Intent intent = new Intent(MainApp.getContext(), HomeActivity.class);
                    intent.putExtra("type", "heating-off");
                    MainApp.getContext().getApplicationContext().startActivity(intent);
                }
            }
        }

        if(text.contains("order")){
            if(text.contains("taxi")){
                speak("Choose taxi service");
                Intent intent = new Intent(MainApp.getContext(), PlaceActivity.class);
                intent.putExtra("type", "taxi");
                MainApp.getContext().getApplicationContext().startActivity(intent);
            }else if (text.contains("take away")){
                speak("Choose Take away");
                Intent intent = new Intent(MainApp.getContext(), PlaceActivity.class);
                intent.putExtra("type", "take-away");
                MainApp.getContext().getApplicationContext().startActivity(intent);
                // ring take away
            }else if(text.contains("shopping")){
                Intent myIntent = new Intent(MainApp.getContext(), ShoppingActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                MainApp.getContext().getApplicationContext().startActivity(myIntent);
            }
        }
    }

    @Override
    public void OnResult(ArrayList<String> commands) {
        StringBuilder text = new StringBuilder("");
        for(String command:commands) {
            text.append(command).append(" ");
        }
        recognition(text.toString());
        //text = null;
    }
}

