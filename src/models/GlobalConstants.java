package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Shane on 26/03/2017.
 */
public class GlobalConstants {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy"); // Used when converting dates to/from strings.
    public static final long SIX_MONTHS_IN_MILLISECS = 15768000000L; // thanks, wolframalpha.

    public static DateFormat getDateFormat() {
        return DATE_FORMAT;
    }
}
