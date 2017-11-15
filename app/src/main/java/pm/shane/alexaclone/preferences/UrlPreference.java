package pm.shane.alexaclone.preferences;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.preference.Preference;
import android.util.AttributeSet;

import pm.shane.alexaclone.MainApp;

/**
 * Created by Shane on 14/11/2017.
 */

public class UrlPreference extends Preference {

    private String url;

    public UrlPreference(Context context) {
        super(context);
    }

    public UrlPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUrl(attrs.getAttributeValue(null, "url"));
    }

    public UrlPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUrl(attrs.getAttributeValue(null, "url"));
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        setOnPreferenceClickListener((Preference preference) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
            MainApp.get().startActivity(intent);
            return true;
        });
    }
}
