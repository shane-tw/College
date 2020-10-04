package pm.shane.alexaclone.models;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Created by Patrick O'Shea on 09/12/2017.
 */

public class Calender {
    private int ID;
    private String title;
    private String date;

    public Calender(int ID, String title, String date){
        setID(ID);
        setTitle(title);
        setDate(date);
    }

    @Override
    public String toString(){
        return getTitle() + " - " + getDate();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
