package hcmute.edu.vn.mssv18110323.shoppingmall.utils;

import java.util.Random;

public class GenerateUtils {
    private static Random randomInstance = new Random();

    public static String oneTimePassword() {
        String otpCode = "";
        for (int i = 0; i < Const.length_otp; i++) {
            otpCode += randomInstance.nextInt(10);
        }
        return otpCode;
    }
}