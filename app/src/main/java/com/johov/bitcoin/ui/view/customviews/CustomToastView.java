package com.johov.bitcoin.ui.view.customviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.core.content.res.ResourcesCompat;

import com.johov.bitcoin.MainActivity;
import com.johov.bitcoin.R;
import androidx.appcompat.widget.AppCompatTextView;

public class CustomToastView extends AppCompatTextView {

    public CustomToastView(Context context) {
        super(context);
        int padding = 100;
        if(MainActivity.Companion.isBiggerThen700dp()==false){
            padding = 32;
        }
        setPadding(padding, padding, padding, padding/3);
        // Customize the appearance of the toast view
/*        int padding = 12;
        setPadding(padding, padding, padding, padding);*/
        setGravity(Gravity.CENTER);
        setTextColor(getResources().getColor(R.color.main_text_color));
        // Optional: You can set specific height and width here
        // Optional: You can set specific height and width here (in dp)
        int width = dpToPx(context, 250);  // Equivalent to 250dp width
        int s = 150;
        if(MainActivity.Companion.isBiggerThen700dp()==false){
            s = 50;
        }
        int height = dpToPx(context,s); // Equivalent to 100dp height
        setLayoutParams(new LinearLayout.LayoutParams(width, height)); // Set the custom width and height
        // Set background resource and scale it appropriately
        Drawable backgroundDrawable = getResources().getDrawable(R.drawable.toast_bg, null);

        if(MainActivity.Companion.isBiggerThen700dp()==false){
            backgroundDrawable = getResources().getDrawable(R.drawable.small_toast_bg, null);
        }
        setBackground(backgroundDrawable); // Apply background
         setTypeface(ResourcesCompat.getFont(context, R.font.krub));
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
    }

    private int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
    public void setMessage(String message) {
        setText(message);
    }
}