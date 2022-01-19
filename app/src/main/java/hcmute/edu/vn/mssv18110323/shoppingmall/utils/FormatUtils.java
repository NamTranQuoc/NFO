package hcmute.edu.vn.mssv18110323.shoppingmall.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtils {
    public static String TimestampToString(Long timestamp) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = new Date(timestamp);
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "01-01-2000";
        }
    }

    public static Long StringToTimestamp(String date) {
        try {
            Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            return date1.getTime();
        } catch (Exception e) {
            return 946688461L;
        }
    }

    public static String getShowPrice(Long price) {
        String str = price.toString();
        String result = "";
        int i;
        for (i = str.length(); i > 3; ) {
            result = "." + str.substring(i - 3, i) + result;
            i -= 3;
        }
        if (i - 3 < str.length()) {
            result = str.substring(0, i) + result;
        } else {
            result = result.substring(1);
        }
        return result;
    }
}
