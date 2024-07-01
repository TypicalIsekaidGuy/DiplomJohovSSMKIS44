package silmex.apps.airdropcryptopoints.ui.view;

import android.content.Context;
import android.view.Gravity;

import silmex.apps.airdropcryptopoints.R;

public class CustomToastView extends androidx.appcompat.widget.AppCompatTextView {

    public CustomToastView(Context context, Boolean hasSucceded) {
        super(context);
        // Customize the appearance of the toast view
        int padding = 48;
        setPadding(padding, padding, padding, padding);
        setGravity(Gravity.CENTER);
        if(hasSucceded){
            setTextColor(getResources().getColor(R.color.toast_succeded_text));
            setBackgroundResource(R.drawable.toast_succeded_bg);
        } else if (!hasSucceded) {
            setTextColor(getResources().getColor(R.color.toast_not_succeded_text));
            setBackgroundResource(R.drawable.toast_not_succeded_bg);
        }
        else{
            setTextColor(getResources().getColor(R.color.toast_succeded_text));
            setBackgroundResource(R.drawable.toast_succeded_bg);
        }
        setTextSize(20);
    }

    public void setMessage(String message) {
        setText(message);
    }
}