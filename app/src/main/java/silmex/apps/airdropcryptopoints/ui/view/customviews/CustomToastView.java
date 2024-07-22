package silmex.apps.airdropcryptopoints.ui.view.customviews;

import android.content.Context;
import android.view.Gravity;

import androidx.core.content.res.ResourcesCompat;

import silmex.apps.airdropcryptopoints.R;
import androidx.appcompat.widget.AppCompatTextView;

public class CustomToastView extends AppCompatTextView {

    public CustomToastView(Context context) {
        super(context);
        // Customize the appearance of the toast view
        int padding = 48;
        setPadding(padding, padding, padding, padding);
        setGravity(Gravity.CENTER);
        setTextColor(getResources().getColor(R.color.main_text_color));
        setBackgroundResource(R.drawable.toast_bg);
        setFirstBaselineToTopHeight(100);
        setTypeface(ResourcesCompat.getFont(context, R.font.itim));
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setTextSize(20);
    }

    public void setMessage(String message) {
        setText(message);
    }
}