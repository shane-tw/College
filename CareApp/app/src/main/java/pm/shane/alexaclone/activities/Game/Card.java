package pm.shane.alexaclone.activities.Game;

/**
 * Created by Patrick O'Shea on 05/10/2017.
 */

import android.widget.Button;


public class Card{

    public int x;
    public int y;
    public Button button;

    public Card(Button button, int x, int y) {
        this.x = x;
        this.y=y;
        this.button=button;
    }


}
