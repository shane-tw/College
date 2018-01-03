package pm.shane.alexaclone.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import pm.shane.alexaclone.R;
import pm.shane.alexaclone.models.Calender;


public class CalenderActivity extends AppCompatActivity {

    public static ArrayList<String> cal_ID = new ArrayList<>();
    public static ArrayList<String> nameOfEvent = new ArrayList<String>();
    public static ArrayList<String> startDates = new ArrayList<String>();
    public static ArrayList<String> endDates = new ArrayList<String>();
    public static ArrayList<String> descriptions = new ArrayList<String>();
    public static ArrayList<Calender> calenders = new ArrayList<>();

    ListView simpleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType("vnd.android.cursor.item/event");
                startActivity(intent);
            }
        });

        simpleList = (ListView)findViewById(R.id.mobile_list);
        ArrayAdapter<Calender> arrayAdapter = new ArrayAdapter<Calender>(this, android.R.layout.simple_list_item_1, readCalendarEvent(this));
        simpleList.setAdapter(arrayAdapter);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id = calenders.get(i).getID();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri.Builder uri = CalendarContract.Events.CONTENT_URI.buildUpon();
                uri.appendPath(Long.toString(id));
                intent.setData(uri.build());
                startActivity(intent);
            }
        });
    }


    public static ArrayList<Calender> readCalendarEvent(Context context) {
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.android.calendar/events"),
                new String[] { "calendar_id", "title", "dtstart","_id" }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        // fetching calendars name
        assert cursor != null;
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        /*cal_ID.clear();
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();*/
        for (int i = 0; i < 20; i++) {

            int id = Integer.parseInt(cursor.getString(3));
            String title = cursor.getString(1);
            Calender calender = new Calender(id, title, getDate(Long.parseLong(cursor.getString(2))));
            calenders.add(calender);
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();

        }
        return calenders;
    }

    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
