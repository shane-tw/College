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

import pm.shane.alexaclone.MainApp;
import pm.shane.alexaclone.R;

/**
 * Created by Shane on 14/11/2017.
 */

public class UrlListItem extends AppCompatTextView {

    private String url;

    public UrlListItem(Context context) {
        super(context);
        init();
    }

    public UrlListItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUrl(attrs.getAttributeValue(null, "url"));
        init();
    }

    public UrlListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUrl(attrs.getAttributeValue(null, "url"));
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        setOnClickListener((View v) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
            MainApp.get().startActivity(intent);
        });
    }

    public void setTitle(String title) {
        setText(title);
    }
}
