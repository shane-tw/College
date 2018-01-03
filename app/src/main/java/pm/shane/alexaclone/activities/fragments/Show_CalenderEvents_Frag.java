package pm.shane.alexaclone.activities.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.models.Calender;

public class Show_CalenderEvents_Frag extends Fragment {
    private static final String TAG = "Tab1Fragment";
    private Calender calender;
    private ArrayList<String> events;

    private void getStuff(){
        //events = calender.readCalendarEvent(MainApp.getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_fragment,container,false);
        //calender = new Calender();
        getStuff();
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainApp.getContext(), android.R.layout.simple_list_item_1, events);

        //ListView listView = (ListView) getActivity().findViewById(R.id.mobile_list);
        //listView.setAdapter(adapter);
        return view;
    }
}
