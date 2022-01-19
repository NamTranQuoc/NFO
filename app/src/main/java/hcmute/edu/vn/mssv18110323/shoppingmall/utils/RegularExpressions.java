package hcmute.edu.vn.mssv18110323.shoppingmall.utils;

public class RegularExpressions {
    public static String checkString(String str) {
        if (isEmail(str)) return Const.is_email;
        if (isPhoneNumber(str)) return Const.is_phone_number;
        return Const.unknown;
    }

    public static boolean isEmail(String str) {
        if (str.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) return true;
        return false;
    }

    public static boolean isPhoneNumber(String str) {
        if (str.matches("^\\d{10}$")) return true;
        return false;
    }

    public static boolean isDate(String str) {
        str.replace('/', '-');
        str.replace('\\', '-');
        str.replace('.', '-');
        str.replace(' ', '-');
        if (str.matches("^(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-\\d{4}$")) return true;
        return false;
    }
}
