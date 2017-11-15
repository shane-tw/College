package pm.shane.alexaclone.preferences;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.preference.Preference;
import android.util.AttributeSet;
import android.widget.Toast;

import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.PermissionUtils;
import pm.shane.alexaclone.R;

/**
 * Created by Shane on 14/11/2017.
 */

public class CallPreference extends Preference {

    private String phoneNumber;

    public CallPreference(Context context) {
        super(context);
    }

    public CallPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPhoneNumber(attrs.getAttributeValue(null, "phoneNumber"));
    }

    public CallPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPhoneNumber(attrs.getAttributeValue(null, "phoneNumber"));
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        setOnPreferenceClickListener((Preference preference) -> {
            String intentAction = Intent.ACTION_CALL;
            if (!PermissionUtils.hasCallPermission()) {
                intentAction = Intent.ACTION_DIAL;
                Toast.makeText(MainApp.getContext(), R.string.no_call_permission, Toast.LENGTH_SHORT).show();
            }
            Intent callIntent = new Intent(intentAction, Uri.parse("tel:" + phoneNumber));
            MainApp.get().startActivity(callIntent);
            return true;
        });
    }
}
