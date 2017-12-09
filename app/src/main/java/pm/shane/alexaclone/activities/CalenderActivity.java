package pm.shane.alexaclone.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import pm.shane.alexaclone.R;
import pm.shane.alexaclone.activities.fragments.Add_CalenderEvent_frag;
import pm.shane.alexaclone.activities.fragments.SectionsPageAdapter;
import pm.shane.alexaclone.activities.fragments.Show_CalenderEvents_Frag;


public class CalenderActivity extends AppCompatActivity {

        private static final String TAG = "Calender Activity";

        private SectionsPageAdapter mSectionsPageAdapter;

        private ViewPager mViewPager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Add_CalenderEvent_frag(), "Add Event");
        adapter.addFragment(new Show_CalenderEvents_Frag(), "Event");
        viewPager.setAdapter(adapter);
    }
}
