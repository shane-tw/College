package pm.shane.alexaclone.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.models.Calender;


public class CalenderActivity extends AppCompatActivity {
    public static ArrayList<Calender> calenders = new ArrayList<>();
    public ListView simpleList;
    SwipeRefreshLayout swipeContainer;

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

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setListview();
            }
        });

    }

    public void setListview(){
        ArrayAdapter<Calender> arrayAdapter = new ArrayAdapter<Calender>(this, android.R.layout.simple_list_item_1, readCalendarEvent(this));
        simpleList.setAdapter(arrayAdapter);
        swipeContainer.setRefreshing(false);
    }



    public static ArrayList<Calender> readCalendarEvent(Context context) {//"dtstart"

        Uri content = Uri.parse("content://com.android.calendar/events");
        String[] vec = new String[] { "calendar_id", "title", "dtstart","_id" };
        String selectionClause = "dtstart >= ?";
        String[] selectionsArgs = new String[]{"" + getcurrentdate()};
        String orderby = "dtstart";
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(content, vec, selectionClause, selectionsArgs, orderby);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        // fetching calendars name
        assert cursor != null;
        String CNames[] = new String[cursor.getCount()];
        for (int i = 0; i < CNames.length; i++) {

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

    private static Long getcurrentdate(){
        Calendar date = Calendar.getInstance();
        return date.getTimeInMillis();
    }
}
