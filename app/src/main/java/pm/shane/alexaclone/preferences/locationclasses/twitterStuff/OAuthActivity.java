package pm.shane.alexaclone.preferences.locationclasses.twitterStuff;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;



public class OAuthActivity extends FragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String authenticationUrl = getIntent().getStringExtra(ConstantValues.STRING_EXTRA_AUTHENCATION_URL);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        OAuthWebViewFragment oAuthWebViewFragment = new OAuthWebViewFragment(authenticationUrl);
        fragmentTransaction.add(android.R.id.content,oAuthWebViewFragment);
        fragmentTransaction.commit();
    }
}