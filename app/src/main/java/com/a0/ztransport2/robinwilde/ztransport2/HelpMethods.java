package com.a0.ztransport2.robinwilde.ztransport2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.PalletBalanceUpdater;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.PalletReport;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.Report;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.TimeReport;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.User;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


import static android.content.Context.VIBRATOR_SERVICE;
import static android.os.VibrationEffect.DEFAULT_AMPLITUDE;

public class HelpMethods {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //region Date Helpers
    public static ArrayList splitYearMonthDay(String date) {
        String[] splittedDate = date.split("-");
        String year = splittedDate[0];
        String month = splittedDate[1];
        String day = splittedDate[2];
        String week = getWeekFromInputDate(year, month, day);

        ArrayList<String> yearMonthDayWeek = new ArrayList<>();
        yearMonthDayWeek.add(year);
        yearMonthDayWeek.add(month);
        yearMonthDayWeek.add(day);
        yearMonthDayWeek.add(week);

        return yearMonthDayWeek;
    }
    public static String getWeekFromInputDate(String year, String month, String day){
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(year+"-"+month+"-"+day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Calendar cal = Calendar.getInstance(Locale.GERMANY);
        cal.setTime(date);
        String week = String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));

        return week;
    }
    public static String getTimeStamp(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String inputTimeStamp = dateFormat.format(date);

        return inputTimeStamp;
    }

    public static String getTodaysDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedTodaysDate = sdf.format(c.getTime());

        return formattedTodaysDate;
    }
    public static HashMap getTodaysDateInHashMap() {
        HashMap actualDateHashMap = new HashMap();

        final Calendar c = Calendar.getInstance(Locale.GERMANY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<String> yearMonthDayWeek = splitYearMonthDay(sdf.format(c.getTime()));

        String year = yearMonthDayWeek.get(0);
        String month = yearMonthDayWeek.get(1);
        String day = yearMonthDayWeek.get(2);
        String week = yearMonthDayWeek.get(3);

        actualDateHashMap.put("year", year);
        actualDateHashMap.put("month", month);
        actualDateHashMap.put("day", day);
        actualDateHashMap.put("week", week);

        return actualDateHashMap;
    }
    public static HashMap getDateFromPalletReportTimeStamp(String inputTimeStamp) {
        HashMap dateFromPalletReportTimeStamp = new HashMap();
        String[] splitString = inputTimeStamp.split("-");
        String[] splitDateString = splitString[2].split(" ");
        String year = splitString[0];
        String month = splitString[1];
        String day = splitDateString[0];

        dateFromPalletReportTimeStamp.put("year", year);
        dateFromPalletReportTimeStamp.put("month", month);
        dateFromPalletReportTimeStamp.put("day", day);
        dateFromPalletReportTimeStamp.put("week", getWeekFromInputDate(year,month,day));

        return dateFromPalletReportTimeStamp;
    }
    //endregion Date Helpers
    //region String Helpers
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
    //endregion String Helpers
    //region App Helpers
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
    //endregion App Helpers
    public static JSONObject prepareReportDataObject(Object reportObject){
        JSONObject dataJsonObject = new JSONObject();

        if(reportObject.getClass().isAssignableFrom(TimeReport.class)){
            try{
                dataJsonObject.put("tRId",((TimeReport) reportObject).getReportId());
                dataJsonObject.put("year",((TimeReport) reportObject).getYear());
                dataJsonObject.put("month",((TimeReport) reportObject).getMonth());
                dataJsonObject.put("day",((TimeReport) reportObject).getDay());
                dataJsonObject.put("week",((TimeReport) reportObject).getWeek());
                dataJsonObject.put("driver",((TimeReport) reportObject).getReportDriverName());
                dataJsonObject.put("driverId", ((TimeReport) reportObject).getReportReporterId());
                dataJsonObject.put("area",((TimeReport) reportObject).getArea());
                dataJsonObject.put("costumer",((TimeReport) reportObject).getCostumer());
                dataJsonObject.put("hours",((TimeReport) reportObject).getHours());
                dataJsonObject.put("isRoute",((TimeReport) reportObject).isRoute());
                dataJsonObject.put("workDescription",((TimeReport) reportObject).getWorkDescription());
                dataJsonObject.put("changedByAdmin",((TimeReport) reportObject).isChangedByAdmin());
                dataJsonObject.put("reportedBy",((TimeReport) reportObject).getReportReporterName());
                dataJsonObject.put("inputTimeStamp",((TimeReport) reportObject).getReportTimeStamp());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(reportObject.getClass().isAssignableFrom(PalletReport.class)) {
            try {
                dataJsonObject.put("pRId", ((PalletReport) reportObject).getReportId());
                dataJsonObject.put("inputTimeStamp", ((PalletReport) reportObject).getReportTimeStamp());
                dataJsonObject.put("driver", ((PalletReport) reportObject).getReportDriverName());
                dataJsonObject.put("driverId", ((PalletReport) reportObject).getReportReporterId());
                dataJsonObject.put("fromPlace", ((PalletReport) reportObject).getFromPlace());
                dataJsonObject.put("toPlace", ((PalletReport) reportObject).getToPlace());
                dataJsonObject.put("noOfpallets", ((PalletReport) reportObject).getNoOfpallets());
                dataJsonObject.put("reportedBy", ((PalletReport) reportObject).getReportReporterName());
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
        if(reportObject.getClass().isAssignableFrom(PalletBalanceUpdater.class)){
            try{
                dataJsonObject.put("inputTimeStamp", ((PalletBalanceUpdater) reportObject).getInpuTimeStamp());
                dataJsonObject.put("jblbalance", ((PalletBalanceUpdater) reportObject).getJblBalance());
                dataJsonObject.put("hedeBalance", ((PalletBalanceUpdater) reportObject).getHedeBalance());
                dataJsonObject.put("fashionServiceBalance", ((PalletBalanceUpdater) reportObject).getFashionServiceBalance());
                dataJsonObject.put("adminUpdate", ((PalletBalanceUpdater) reportObject).getAdminUpdate());
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return dataJsonObject;
    }
    public static ArrayList prepareWeekReportToBeParsedToExcel(String year, String week, ArrayList<TimeReport> reportsList) {
        ArrayList parsedArrayList = new ArrayList();
        for (TimeReport report: reportsList) {
            if(report.getYear().equals(year) && report.getWeek().equals(week)){
                parsedArrayList.add(report);
            }
        }
        return parsedArrayList;
    }
    public static ArrayList prepareMonthReportToBeParsedToExcel(String year, int month, ArrayList<TimeReport> reportsList) {
        ArrayList parsedArrayList = new ArrayList();
        for (TimeReport report: reportsList) {
            if(report.getYear().equals(year) && Integer.parseInt(report.getMonth()) == month){
                parsedArrayList.add(report);
            }
        }
        return parsedArrayList;
    }
    public static String parseArrayToExcelAndGenerateReport(Activity activity, ArrayList<TimeReport> timeReports, int weekOrMonthReportId){
        String fnamexls="";
        String feedbackMessage="";

        if(weekOrMonthReportId==R.id.rbWeekReport){
            fnamexls = "ZTransport_"+timeReports.get(0).getYear()+"_v"+timeReports.get(0).getWeek()+ ".xls";
        }
        if(weekOrMonthReportId==R.id.rbMonthReport){
            fnamexls = "ZTransport_"+timeReports.get(0).getYear()+""+timeReports.get(0).getMonth()+ ".xls";
        }
        int columnCounter;
        int rowCounter = 0;

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, fnamexls);

        path.mkdirs();

        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        verifyStoragePermissions(activity);

        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            WritableSheet sheet = workbook.createSheet(fnamexls, 0);
            Label labelHeader1 = new Label(0,0, activity.getString(R.string.report_time_stamp));
            Label labelHeader2 = new Label(1,0, activity.getString(R.string.year));
            Label labelHeader3 = new Label(2,0, activity.getString(R.string.month));
            Label labelHeader4 = new Label(3,0, activity.getString(R.string.day));
            Label labelHeader5 = new Label(4,0, activity.getString(R.string.driver));
            Label labelHeader6 = new Label(5,0, activity.getString(R.string.costumer));
            Label labelHeader7 = new Label(6,0, activity.getString(R.string.area));
            Label labelHeader8 = new Label(7,0, activity.getString(R.string.hours));
            Label labelHeader9 = new Label(8,0, activity.getString(R.string.route));
            Label labelHeader10 = new Label(9,0, activity.getString(R.string.workDescription));
            Label labelHeader11 = new Label(10,0, activity.getString(R.string.week));
            Label labelHeader12 = new Label(11,0, activity.getString(R.string.changed));

            try {
                sheet.addCell(labelHeader1);
                sheet.addCell(labelHeader2);
                sheet.addCell(labelHeader3);
                sheet.addCell(labelHeader4);
                sheet.addCell(labelHeader5);
                sheet.addCell(labelHeader6);
                sheet.addCell(labelHeader7);
                sheet.addCell(labelHeader8);
                sheet.addCell(labelHeader9);
                sheet.addCell(labelHeader10);
                sheet.addCell(labelHeader11);
                sheet.addCell(labelHeader12);

            } catch (RowsExceededException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            rowCounter=1;
            for (TimeReport report: timeReports){
                columnCounter=0;
                String isRoute="";
                String isChangedByAdmin="";
                Label label1 = new Label(columnCounter++, rowCounter, String.valueOf(report.getReportTimeStamp()));
                Label label2 = new Label(columnCounter++, rowCounter, String.valueOf(report.getYear()));
                Label label3 = new Label(columnCounter++, rowCounter, String.valueOf(report.getMonth()));
                Label label4 = new Label(columnCounter++, rowCounter, String.valueOf(report.getDay()));
                Label label5 = new Label(columnCounter++, rowCounter, String.valueOf(report.getReportDriverName()));
                Label label6 = new Label(columnCounter++, rowCounter, String.valueOf(report.getCostumer()));
                Label label7 = new Label(columnCounter++, rowCounter, String.valueOf(report.getArea()));
                Label label8 = new Label(columnCounter++, rowCounter, String.valueOf(report.getHours()));
                if(report.isRoute()){
                    isRoute="Ja";
                }
                else{isRoute="Nej";}
                Label label9 = new Label(columnCounter++, rowCounter, isRoute);
                Label label10 = new Label(columnCounter++, rowCounter, String.valueOf(report.getWorkDescription()));
                Label label11 = new Label(columnCounter++, rowCounter, String.valueOf(report.getWeek()));
                if(report.isChangedByAdmin()){
                    isChangedByAdmin="Ja";
                }
                else{isChangedByAdmin="Nej";}
                Label label12 = new Label(columnCounter++, rowCounter, isChangedByAdmin);

                try {
                    sheet.addCell(label1);
                    sheet.addCell(label2);
                    sheet.addCell(label3);
                    sheet.addCell(label4);
                    sheet.addCell(label5);
                    sheet.addCell(label6);
                    sheet.addCell(label7);
                    sheet.addCell(label8);
                    sheet.addCell(label9);
                    sheet.addCell(label10);
                    sheet.addCell(label11);
                    sheet.addCell(label12);

                } catch (RowsExceededException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (WriteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                rowCounter++;
            }

            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            feedbackMessage=activity.getResources().getString(R.string.write_report_success);
            //createExcel(excelSheet);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            feedbackMessage=activity.getResources().getString(R.string.write_report_failure);
            e.printStackTrace();
        }

        return feedbackMessage;
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    public static void clearSharedPreferences(Context context, String sharedPrefsName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefsName,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
