package pm.shane.alexaclone;

import android.content.Context;
import android.content.SharedPreferences;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import pm.shane.alexaclone.api.response.data.User;

/**
 * Created by Shane on 28/10/2017.
 */

public class SessionManager {

    private static User loggedInUser;
    private static SharedPreferences sharedPreferences;
    private static ClearableCookieJar cookieJar;

    public static synchronized void assignPreferencesIfNeeded() {
        if (sharedPreferences == null) {
            sharedPreferences = MainApp.get().getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE);
        }
    }

    public static boolean isLoggedIn() {
        assignPreferencesIfNeeded();
        return sharedPreferences.getBoolean("logged_in", false);
    }

    public static void setLoggedIn(boolean loggedIn) {
        if (!loggedIn) {
            loggedInUser = null;
            getCookieJar().clear();
        }
        assignPreferencesIfNeeded();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("logged_in", loggedIn);
        editor.apply();
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
        setLoggedIn(loggedInUser != null);
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static synchronized ClearableCookieJar getCookieJar() {
        if (cookieJar == null) {
            cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MainApp.getContext()));
        }
        return cookieJar;
    }

}
