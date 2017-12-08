package pm.shane.alexaclone.models;

/**
 * Created by Dave on 08/12/2017.
 */

public class Song {
    private String name;
    private String artist;
    private int songId;

    public Song(String name, String artist, int songId){
        this.name=name;
        this.artist=artist;
        this.songId=songId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }
}
