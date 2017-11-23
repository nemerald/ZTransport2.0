package com.a0.ztransport2.robinwilde.ztransport2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.a0.ztransport2.robinwilde.ztransport2.Adapters.PalletReportsListAdapter;
import com.a0.ztransport2.robinwilde.ztransport2.Adapters.TimeReportsListAdapter;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.PalletReport;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.TimeReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ReportsFragment extends Fragment {

    RadioGroup rgReportPickerGroup, rgIntervalPickerGroup;
    RadioButton rbPickPalletReport, rbPickTimeReport, rbPickToday, rbPickThisWeek, rbPickThisMonth;
    ListView lvReportList;
    private ProgressDialog mProgressDialog;
    private PalletReportsListAdapter mPalletReportListAdapter;
    private TimeReportsListAdapter mTimeReportListAdapter;


    private ArrayList<TimeReport> allTimeReports;
    private ArrayList<TimeReport> todaysTimeReports;
    private ArrayList<TimeReport> thisWeeksTimeReports;
    private ArrayList<TimeReport> thisMonthsTimeReports;

    private ArrayList<PalletReport> allPalletReports;
    private ArrayList<PalletReport> todaysPalletReports;
    private ArrayList<PalletReport> thisWeeksPalletReports;
    private ArrayList<PalletReport> thisMonthsPalletReports;

    String getReportsUrl = "https://prod-29.northeurope.logic.azure.com:443/workflows/ea5bb264a52844a4850a8e29a05ce591/triggers/request/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Frequest%2Frun&sv=1.0&sig=CVSEQrIkyjKeoG2weFdzLYG6oCnqEQTTgKHjGpLZH7g";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_reports, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        rgReportPickerGroup = (RadioGroup) view.findViewById(R.id.rgReportPickerGroup);
        rgIntervalPickerGroup = (RadioGroup) view.findViewById(R.id.rgIntervalPickerGroup);
        rbPickPalletReport = (RadioButton) view.findViewById(R.id.rbPickPalletReport);
        rbPickTimeReport = (RadioButton) view.findViewById(R.id.rbPickTimeReport);
        rbPickToday = (RadioButton) view.findViewById(R.id.rbPickToday);
        rbPickThisWeek = (RadioButton) view.findViewById(R.id.rbPickThisWeek);
        rbPickThisMonth = (RadioButton) view.findViewById(R.id.rbPickThisMonth);
        lvReportList = (ListView) view.findViewById(R.id.lvReportList);

        rgIntervalPickerGroup.setEnabled(false);
        rgReportPickerGroup.setEnabled(false);

        getReportsFromDb();
    }

    private void getReportsFromDb() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.wait));
        mProgressDialog.setMessage(getString(R.string.getting_reports_from_server));
        mProgressDialog.show();

        DbHelperMethods.getRequester(getActivity(), getReportsUrl, null, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                mProgressDialog.dismiss();
                TimeReport timeReportHolder = new TimeReport();
                PalletReport palletReportHolder = new PalletReport();
                try {
                    JSONArray jsonTimeReportsArray = response.getJSONArray("timeReportArray");
                    JSONArray jsonPalletReportsArray = response.getJSONArray("palletReportArray");
                    allTimeReports = parseJSONArray(jsonTimeReportsArray, timeReportHolder);
                    allPalletReports = parseJSONArray(jsonPalletReportsArray, palletReportHolder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rgIntervalPickerGroup.setEnabled(true);
                rgReportPickerGroup.setEnabled(true);
            }

            @Override
            public void onError(String message) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.error_network_error)+" "+message, Toast.LENGTH_SHORT).show();
            }
        });
        rgReportPickerGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rbPickTimeReport:
                        checkPickedReportTypeAndInterval();
                        break;
                    case R.id.rbPickPalletReport:
                        checkPickedReportTypeAndInterval();
                        break;
                }
            }
        });
        rgIntervalPickerGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rbPickToday:
                        checkPickedReportTypeAndInterval();
                        break;
                    case R.id.rbPickThisWeek:
                        checkPickedReportTypeAndInterval();
                        break;
                    case R.id.rbPickThisMonth:
                        checkPickedReportTypeAndInterval();
                        break;
                }
            }
        });
    }

    private void checkPickedReportTypeAndInterval() {
        if(rgReportPickerGroup.getCheckedRadioButtonId()==-1 || rgIntervalPickerGroup.getCheckedRadioButtonId()==-1){
            lvReportList.setAdapter(null);
        }
        else if(rgReportPickerGroup.getCheckedRadioButtonId()==rbPickTimeReport.getId()){
            setReportDataToList(parseIntervalFromReportList(allTimeReports));
        }
        else if(rgReportPickerGroup.getCheckedRadioButtonId()==rbPickPalletReport.getId()){
            setReportDataToList(parseIntervalFromReportList(allPalletReports));
        }
    }

    private ArrayList parseIntervalFromReportList(ArrayList reportList){
        ArrayList parsedArrayList = new ArrayList();
        if(reportList.size()!=0){
            if(rgIntervalPickerGroup.getCheckedRadioButtonId()==rbPickToday.getId()){
                for (Object obj: reportList) {
                    if(obj.getClass().isAssignableFrom(TimeReport.class)){
                        StringBuilder todaysDateBuilder = new StringBuilder();
                        todaysDateBuilder.append(((TimeReport) obj).getYear());
                        todaysDateBuilder.append("-");
                        todaysDateBuilder.append(((TimeReport) obj).getMonth());
                        todaysDateBuilder.append("-");
                        todaysDateBuilder.append(((TimeReport) obj).getDay());
                        String todaysDateFromObj = todaysDateBuilder.toString();
                        String todaysDate = HelpMethods.getTodaysDate();
                        if(todaysDateFromObj.equals(todaysDate)){
                            parsedArrayList.add(obj);
                        }
                    }
                    if(obj.getClass().isAssignableFrom(PalletReport.class)){

                    }
                }
            }
            if(rgIntervalPickerGroup.getCheckedRadioButtonId()==rbPickThisWeek.getId()){
                for (Object obj: reportList) {
                    if(obj.getClass().isAssignableFrom(TimeReport.class)){

                    }
                    if(obj.getClass().isAssignableFrom(PalletReport.class)){

                    }
                }
            }
            if(rgIntervalPickerGroup.getCheckedRadioButtonId()==rbPickThisMonth.getId()){
                for (Object obj: reportList) {
                    if(obj.getClass().isAssignableFrom(TimeReport.class)){

                    }
                    if(obj.getClass().isAssignableFrom(PalletReport.class)){

                    }
                }
            }

        }
        return parsedArrayList;
    }

    private void setReportDataToList(ArrayList listOfReports) {
        if(listOfReports.size()!=0){
            if(listOfReports.get(0).getClass().isAssignableFrom(TimeReport.class)){
                this.mTimeReportListAdapter = new TimeReportsListAdapter(getActivity(), listOfReports);
                lvReportList.setAdapter(mTimeReportListAdapter);
            }
            if(listOfReports.get(0).getClass().isAssignableFrom(PalletReport.class)){
                this.mPalletReportListAdapter = new PalletReportsListAdapter(getActivity(), listOfReports);
                lvReportList.setAdapter(mPalletReportListAdapter);
            }
        }
        else{
            Toast.makeText(getActivity(), getString(R.string.no_data_in_list), Toast.LENGTH_SHORT).show();
            lvReportList.setAdapter(null);
        }
    }

    private ArrayList parseJSONArray(JSONArray jsonReportsArray, Object object) {
        ArrayList tempArrayList = new ArrayList();

        if (object.getClass().isAssignableFrom(TimeReport.class)){
            for(int i=0;i<jsonReportsArray.length();i++){
                try {
                    JSONObject tempObj = jsonReportsArray.getJSONObject(i);
                    object = new TimeReport();
                    ((TimeReport) object).settRId(tempObj.getString("tRId"));
                    ((TimeReport) object).setYear(tempObj.getString("year"));
                    ((TimeReport) object).setMonth(tempObj.getString("month"));
                    ((TimeReport) object).setDay(tempObj.getString("day"));
                    ((TimeReport) object).setWeek(tempObj.getString("week"));
                    ((TimeReport) object).setDriver(tempObj.getString("driver"));
                    ((TimeReport) object).setDriverId(tempObj.getString("driverId"));
                    ((TimeReport) object).setCostumer(tempObj.getString("costumer"));
                    ((TimeReport) object).setArea(tempObj.getString("area"));
                    ((TimeReport) object).setHours(tempObj.getString("hours"));
                    ((TimeReport) object).setIsRoute(tempObj.getBoolean("isRoute"));
                    ((TimeReport) object).setWorkDescription(tempObj.getString("workDescription"));
                    ((TimeReport) object).setChangedByAdmin(tempObj.getBoolean("changedByAdmin"));
                    ((TimeReport) object).setReportedBy(tempObj.getString("reportedBy"));
                    ((TimeReport) object).setInputTimeStamp(tempObj.getString("inputTimeStamp"));

                    tempArrayList.add(object);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (object.getClass().isAssignableFrom(PalletReport.class)){
            for(int i=0;i<jsonReportsArray.length();i++){
                try {
                    JSONObject tempObj = jsonReportsArray.getJSONObject(i);
                    object = new PalletReport();
                    ((PalletReport) object).setpRId(tempObj.getString("pRId"));
                    ((PalletReport) object).setInputTimeStamp(tempObj.getString("inputTimeStamp"));
                    ((PalletReport) object).setDriver(tempObj.getString("driver"));
                    ((PalletReport) object).setDriverId(tempObj.getString("driverId"));
                    ((PalletReport) object).setFromPlace(tempObj.getString("fromPlace"));
                    ((PalletReport) object).setToPlace(tempObj.getString("toPlace"));
                    ((PalletReport) object).setNoOfpallets(tempObj.getString("noOfpallets"));
                    ((PalletReport) object).setReportedBy(tempObj.getString("reportedBy"));

                    tempArrayList.add(object);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return tempArrayList;
    }

    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment Reports: Refresh called.",
                Toast.LENGTH_SHORT).show();
    }
}