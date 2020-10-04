package pm.shane.alexaclone.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ListAdapter;

import java.util.ArrayList;

import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.models.ListViewAdapter;
import pm.shane.alexaclone.models.Song;

public class MusicActivity extends AppCompatActivity {

    private ArrayList<Song> arrayList;
    private ListViewAdapter adapter;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        arrayList = new ArrayList<>();
        arrayList.add(new Song("Insomnia","Faithless", R.raw.faithless_insomnia));
        arrayList.add(new Song("On My Side","Adryiano", R.raw.on_my_side));
        arrayList.add(new Song("New Slang","The Shins", R.raw.the_shins_new_slang));
        arrayList.add(new Song("Your Sunshine","Weiss", R.raw.your_sunshine));

        listView = (ListView) findViewById(R.id.songList);
        adapter = new ListViewAdapter(this, R.layout.music_row, arrayList);
        listView.setAdapter(adapter);

    }
}
