package silmex.apps.airdropcryptopoints.utils;

import java.util.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

public class ConvertUtils {
    public static Date stringToDate(String aDate) {
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(simpledateformat.parse(aDate, pos) == null||aDate==null){
            return new Date();
        }
        return simpledateformat.parse(aDate, pos);
    }
}
