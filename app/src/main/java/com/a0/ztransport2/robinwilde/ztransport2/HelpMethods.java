package com.a0.ztransport2.robinwilde.ztransport2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.PalletReport;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.TimeReport;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.User;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.VIBRATOR_SERVICE;
import static android.os.VibrationEffect.DEFAULT_AMPLITUDE;

public class HelpMethods {

    public static Boolean ifSharedPrefsHoldsData(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_preference_name), Activity.MODE_PRIVATE);

        String userName = sharedPref.getString(context.getString(R.string.shared_prefs_user_name), null);
        String userPhoneNumber = sharedPref.getString(context.getString(R.string.shared_prefs_user_phone_number), null);
        String userEmail = sharedPref.getString(context.getString(R.string.shared_prefs_user_email), null);

        if (userName != null || userPhoneNumber != null || userEmail != null) {
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList splitYearMonthDay(String date) {
        String[] splittedDate = date.split("-");
        String year = splittedDate[0];
        String month = splittedDate[1];
        String day = splittedDate[2];
        String week = getWeek(year, month, day);

        ArrayList<String> yearMonthDayWeek = new ArrayList<>();
        yearMonthDayWeek.add(year);
        yearMonthDayWeek.add(month);
        yearMonthDayWeek.add(day);
        yearMonthDayWeek.add(week);

        return yearMonthDayWeek;
    }

    public static String getTimeStamp(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String inputTimeStamp = dateFormat.format(date);

        return inputTimeStamp;
    }
    public static String getWeek(String year, String month, String day){
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(day+"/"+month+"/"+year);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Calendar cal = Calendar.getInstance(Locale.GERMANY);
        cal.setTime(date);
        String week = String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));

        return week;
    }

    public static JSONObject prepareReportDataObject(Object reportObject){
        JSONObject dataJsonObject = new JSONObject();

        if(reportObject.getClass().isAssignableFrom(TimeReport.class)){
            try{
                dataJsonObject.put("tRId",((TimeReport) reportObject).gettRId());
                dataJsonObject.put("year",((TimeReport) reportObject).getYear());
                dataJsonObject.put("month",((TimeReport) reportObject).getMonth());
                dataJsonObject.put("day",((TimeReport) reportObject).getDay());
                dataJsonObject.put("week",((TimeReport) reportObject).getWeek());
                dataJsonObject.put("driver",((TimeReport) reportObject).getDriver());
                dataJsonObject.put("driverId", ((TimeReport) reportObject).getDriverId());
                dataJsonObject.put("area",((TimeReport) reportObject).getArea());
                dataJsonObject.put("costumer",((TimeReport) reportObject).getCostumer());
                dataJsonObject.put("hours",((TimeReport) reportObject).getHours());
                dataJsonObject.put("isRoute",((TimeReport) reportObject).getIsRoute());
                dataJsonObject.put("workDescription",((TimeReport) reportObject).getWorkDescription());
                dataJsonObject.put("changedByAdmin",((TimeReport) reportObject).getIsChangedByAdmin());
                dataJsonObject.put("reportedBy",((TimeReport) reportObject).getReportedBy());
                dataJsonObject.put("inputTimeStamp",((TimeReport) reportObject).getInputTimeStamp());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(reportObject.getClass().isAssignableFrom(PalletReport.class)) {
            try {
                dataJsonObject.put("pRId", ((PalletReport) reportObject).getpRId());
                dataJsonObject.put("inputTimeStamp", ((PalletReport) reportObject).getInputTimeStamp());
                dataJsonObject.put("driver", ((PalletReport) reportObject).getDriver());
                dataJsonObject.put("driverId", ((PalletReport) reportObject).getDriverId());
                dataJsonObject.put("fromPlace", ((PalletReport) reportObject).getFromPlace());
                dataJsonObject.put("toPlace", ((PalletReport) reportObject).getToPlace());
                dataJsonObject.put("noOfpallets", ((PalletReport) reportObject).getNoOfpallets());
                dataJsonObject.put("reportedBy", ((PalletReport) reportObject).getReportedBy());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(reportObject.getClass().isAssignableFrom(User.class)){
            try{
                dataJsonObject.put("uId",((User) reportObject).getuId());
                dataJsonObject.put("name",((User) reportObject).getName());
                dataJsonObject.put("phoneNumber",((User) reportObject).getPhoneNumber());
                dataJsonObject.put("eMail",((User) reportObject).geteMail());
                dataJsonObject.put("isAdmin",((User) reportObject).getIsAdmin());
                dataJsonObject.put("isSuperAdmin",((User) reportObject).getIsSuperAdmin());
                dataJsonObject.put("hasPermissionToReport",((User) reportObject).getHasPermissionToReport());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return dataJsonObject;
    }
    public static String setFirstCharacterToUpperCase(String string){
        String stringWithUpperCase = "";

        String lowCaseString = string.toLowerCase();
        String firstChar = lowCaseString.substring(0,1).toUpperCase();
        stringWithUpperCase = firstChar+lowCaseString.substring(1);

        return stringWithUpperCase;
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
    public static String trimDayStringIfStartWithZero(String day) {
        if (day.substring(0,1).equals("0")){
            day = day.substring(1);

            return day;
        }else{
            return day;
        }
    }
}
