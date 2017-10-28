package com.a0.ztransport2.robinwilde.ztransport2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.VIBRATOR_SERVICE;
import static android.os.VibrationEffect.DEFAULT_AMPLITUDE;

/**
 * Created by robin.wilde on 2017-10-28.
 */

public class HelpMethods {

    public static Boolean ifSharedPrefsHoldsData(SharedPreferences sharedPreferences, Context context){

        String userName = sharedPreferences.getString(context.getString(R.string.shared_prefs_user_name), null);
        String userPhoneNumber = sharedPreferences.getString(context.getString(R.string.shared_prefs_user_phone_number), null);
        String userEmail = sharedPreferences.getString(context.getString(R.string.shared_prefs_user_email), null);

        if(userName == null || userPhoneNumber == null || userEmail == null){
            return false;
        }
        else{
            return true;
        }
    }
    public static boolean checkIfStringIsEmptyOrBlankOrNull(String input){
        boolean emptyInput;

        if(isNullOrEmpty(input) || isNullOrWhitespace(input)){
            emptyInput = true;
        }
        else{
            emptyInput=false;
        }
        return emptyInput;
    }
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNullOrWhitespace(String s) {
        return s == null || isWhitespace(s);

    }
    private static boolean isWhitespace(String s) {
        int length = s.length();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    public static void vibrate(Context context, String vibrateType){
        long millis = 100;
        if(vibrateType.equals(context.getString(R.string.error_vibrate))){
            millis = 200;
        }
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(millis,DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(millis);
        }
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
