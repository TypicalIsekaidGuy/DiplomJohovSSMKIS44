package silmex.apps.airdropcryptopoints.utils;

import android.net.ParseException;

import java.util.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

public class ConvertUtils {
    public static Date stringToDate(String aDate) {
        if (aDate == null) {
            return null; // Return null if the input string is null
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(aDate);
        } catch (ParseException | java.text.ParseException e) {
            // Return null if parsing fails
            return null;
        }
    }

}
