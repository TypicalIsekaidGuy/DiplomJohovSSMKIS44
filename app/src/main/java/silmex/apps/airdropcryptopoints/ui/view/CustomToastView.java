package silmex.apps.airdropcryptopoints.ui.view;

import android.content.Context;
import android.view.Gravity;

import androidx.core.content.res.ResourcesCompat;

import silmex.apps.airdropcryptopoints.R;
import androidx.appcompat.widget.AppCompatTextView;

public class CustomToastView extends AppCompatTextView {

    public CustomToastView(Context context, Boolean hasSucceded) {
        super(context);
        // Customize the appearance of the toast view
        int padding = 48;
        setPadding(padding, padding, padding, padding);
        setGravity(Gravity.CENTER);
        if(hasSucceded==null){
            setTextColor(getResources().getColor(R.color.toast_succeded_text));
            setBackgroundResource(R.drawable.toast_succeded_bg);
        } else if (!hasSucceded) {
            setTextColor(getResources().getColor(R.color.toast_not_succeded_text));
            setBackgroundResource(R.drawable.toast_not_succeded_bg);
        }
        else if (hasSucceded){
            setTextColor(getResources().getColor(R.color.toast_succeded_text));
            setBackgroundResource(R.drawable.toast_succeded_bg);
        }
        setTextColor(getResources().getColor(R.color.main_text_color));
        setTypeface(ResourcesCompat.getFont(context, R.font.itim));
        setTextSize(20);
    }

    public void setMessage(String message) {
        setText(message);
    }
}