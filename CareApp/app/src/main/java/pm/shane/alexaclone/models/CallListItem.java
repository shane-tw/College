package pm.shane.alexaclone.models;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.PermissionUtils;
import pm.shane.alexaclone.R;

/**
 * Created by Shane on 14/11/2017.
 */

public class CallListItem extends AppCompatTextView {

    private String phoneNumber;

    public CallListItem(Context context) {
        super(context);
        init();
    }

    public CallListItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CallListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        setBackgroundResource(outValue.resourceId);
        setPadding(getResources().getDimensionPixelOffset(R.dimen.list_item_horizontal), getResources().getDimensionPixelOffset(R.dimen.list_item_vertical),
                getResources().getDimensionPixelOffset(R.dimen.list_item_horizontal), getResources().getDimensionPixelOffset(R.dimen.list_item_vertical));
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.list_item_text_size));
        setTextColor(getResources().getColor(R.color.black));
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        setOnClickListener((View v) -> {
            String intentAction = Intent.ACTION_CALL;
            if (!PermissionUtils.hasCallPermission()) {
                intentAction = Intent.ACTION_DIAL;
                Toast.makeText(MainApp.getContext(), R.string.no_call_permission, Toast.LENGTH_SHORT).show();
            }
            Intent callIntent = new Intent(intentAction, Uri.parse("tel:" + phoneNumber));
            MainApp.get().startActivity(callIntent);
        });
    }

    public void setTitle(String title) {
        setText(title);
    }
}
