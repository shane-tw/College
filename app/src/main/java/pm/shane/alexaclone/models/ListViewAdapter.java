package pm.shane.alexaclone.models;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.R;

/**
 * Created by Dave on 08/12/2017.
 */

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Song> arrayListSongs;
    private MediaPlayer mediaPlayer;

    private boolean b = true;

    public ListViewAdapter(Context context, int layout, ArrayList<Song> arrayListSongs) {
        this.context = context;
        this.layout = layout;
        this.arrayListSongs = arrayListSongs;
    }

    @Override
    public int getCount() {
        return arrayListSongs.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        TextView songName;
        TextView artistName;
        Button stopButton;
        Button playButton;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if(view==null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(layout, null);
            viewHolder.songName = (TextView) view.findViewById(R.id.songName);
            viewHolder.artistName = (TextView) view.findViewById(R.id.artistName);
            viewHolder.playButton = (Button) view.findViewById(R.id.playButton);
            viewHolder.stopButton = (Button) view.findViewById(R.id.stopButton);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        final Song song = arrayListSongs.get(i);
        viewHolder.songName.setText(song.getName());
        viewHolder.artistName.setText(song.getArtist());

        viewHolder.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!b){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    b=true;
                }
                viewHolder.playButton.setText("Play");
            }
        });

        viewHolder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(b){
                    mediaPlayer= MediaPlayer.create(context, song.getSongId());
                    b=false;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    viewHolder.playButton.setText("Play");
                }else {
                    mediaPlayer.start();
                    viewHolder.playButton.setText("Pause");
                }
            }
        });

        return view;
    }
}
