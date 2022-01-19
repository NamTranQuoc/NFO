package hcmute.edu.vn.mssv18110323.shoppingmall.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;

public class Base64Utils {
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeBase64String(imageBytes);
    }

    public static Bitmap stringToBitmap(String base64Text) {
        try {
            byte[] decodedString = Base64.decodeBase64(base64Text);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
            byte[] decodedString = Base64.decodeBase64(Const.no_image);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
    }

    public static String stringToBase64(String str) {
        byte[] bytesEncoded = Base64.encodeBase64(str.getBytes());
        return new String(bytesEncoded);
    }

    public static String base64ToString(String base64) {
        byte[] valueDecoded = Base64.decodeBase64(base64);
        return new String(valueDecoded);
    }
}
