package com.a0.ztransport2.robinwilde.ztransport2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.a0.ztransport2.robinwilde.ztransport2.Adapters.PalletReportsListAdapter;
import com.a0.ztransport2.robinwilde.ztransport2.Adapters.TimeReportsListAdapter;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.PalletBalanceUpdater;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.PalletReport;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.TimeReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

public class ReportsFragment extends Fragment {
    AdminActivityFragmentCommunicator mCallback;

    RadioGroup rgReportPickerGroup, rgIntervalPickerGroup;
    RadioButton rbPickPalletReport, rbPickTimeReport, rbPickToday, rbPickThisWeek, rbPickThisMonth;
    ListView lvReportList;
    private ProgressDialog mProgressDialog;
    private PalletReportsListAdapter mPalletReportListAdapter;
    private TimeReportsListAdapter mTimeReportListAdapter;
    Timer timer = new Timer();

    private ArrayList<TimeReport> allTimeReports;
    private ArrayList<PalletReport> allPalletReports;

    String getReportsUrl = "https://prod-29.northeurope.logic.azure.com:443/workflows/ea5bb264a52844a4850a8e29a05ce591/triggers/request/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Frequest%2Frun&sv=1.0&sig=CVSEQrIkyjKeoG2weFdzLYG6oCnqEQTTgKHjGpLZH7g";
    String palletBalanceUpdaterUrl = "https://prod-01.northeurope.logic.azure.com:443/workflows/67e40f7e1daf4eeb8f111cbb37f4c9a7/triggers/request/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Frequest%2Frun&sv=1.0&sig=jHvh0qp4-tfcTS81bxO5hDqbPwCcYYZZMD4nTXDAZcg";

    boolean editTimeReport = false;
    boolean deleteTimeReport = false;
    boolean addNewTimeReport = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (AdminActivityFragmentCommunicator) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MainActivityFragmentCommunicator");
        }
    }

    @Override
    public void onDetach() {
        mCallback=null;
        super.onDetach();
    }

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

                    ArrayList<ArrayList> reportArray = new ArrayList<>();
                    reportArray.add(allTimeReports);
                    reportArray.add(allPalletReports);

                    mCallback.sendReportArraysToGenerateReportFragment(reportArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rgIntervalPickerGroup.setEnabled(true);
                rgReportPickerGroup.setEnabled(true);
            }

            @Override
            public void onError(String message) {
                allTimeReports = null;
                allPalletReports = null;
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
        lvReportList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(rgReportPickerGroup.getCheckedRadioButtonId()==rbPickTimeReport.getId() &&
                        rgIntervalPickerGroup.getCheckedRadioButtonId()!=-1){
                    showAdminTimeReportOptionsDialog();
                }
                if(rgReportPickerGroup.getCheckedRadioButtonId()==rbPickPalletReport.getId() &&
                        rgIntervalPickerGroup.getCheckedRadioButtonId()!=-1){
                    showUpdatePalletBalanceDialog();
                }
                return false;
            }
        });
    }
    private void showAdminTimeReportOptionsDialog() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertCustomLayout = inflater.inflate(R.layout.custom_admin_time_report_options_dialog, null);

        final Button bEditTimeReport = (Button) alertCustomLayout.findViewById(R.id.bEditTimeReport);
        final Button bDeleteTimeReport = (Button) alertCustomLayout.findViewById(R.id.bDeleteTimeReport);
        final Button bAddNewTimeReport = (Button) alertCustomLayout.findViewById(R.id.bAddNewTimeReport);

        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        alert.setTitle(getString(R.string.admin_time_report_options));
        alert.setView(alertCustomLayout);
        alert.setCancelable(false);
        alert.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        final AlertDialog dialog = alert.create();
        dialog.show();

        bEditTimeReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bEditTimeReport.setBackgroundResource(R.drawable.button_pressed);
                bDeleteTimeReport.setBackgroundResource(R.drawable.button_unpressed);
                bAddNewTimeReport.setBackgroundResource(R.drawable.button_unpressed);
                editTimeReport = true;
                deleteTimeReport = false;
                addNewTimeReport = false;
            }
        });
        bDeleteTimeReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bEditTimeReport.setBackgroundResource(R.drawable.button_unpressed);
                bDeleteTimeReport.setBackgroundResource(R.drawable.button_pressed);
                bAddNewTimeReport.setBackgroundResource(R.drawable.button_unpressed);
                editTimeReport = false;
                deleteTimeReport = true;
                addNewTimeReport = false;
            }
        });
        bAddNewTimeReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bEditTimeReport.setBackgroundResource(R.drawable.button_unpressed);
                bDeleteTimeReport.setBackgroundResource(R.drawable.button_unpressed);
                bAddNewTimeReport.setBackgroundResource(R.drawable.button_pressed);
                editTimeReport = false;
                deleteTimeReport = false;
                addNewTimeReport = true;
            }
        });

        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTimeReport){
                    showEditTimereportDialog();
                    editTimeReport = false;
                    deleteTimeReport = false;
                    addNewTimeReport = false;

                }
                else if(deleteTimeReport){
                    showDeleteTimereportDialog();
                    editTimeReport = false;
                    deleteTimeReport = false;
                    addNewTimeReport = false;

                }
                else if(addNewTimeReport){
                    showAddNewTimereportDialog();
                    editTimeReport = false;
                    deleteTimeReport = false;
                    addNewTimeReport = false;

                }
                else{
                    Toast.makeText(getActivity(), getString(R.string.error_no_button_pressed), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
    private void showEditTimereportDialog() {
        Toast.makeText(getActivity(), "Editera tidrapport!", Toast.LENGTH_SHORT).show();
    }
    private void showDeleteTimereportDialog() {
        Toast.makeText(getActivity(), "Ta bort tidrapport!", Toast.LENGTH_SHORT).show();
    }
    private void showAddNewTimereportDialog() {
        Toast.makeText(getActivity(), "Lägg till ny tidrapport!", Toast.LENGTH_SHORT).show();
    }

    private void showUpdatePalletBalanceDialog() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertCustomLayout = inflater.inflate(R.layout.custom_update_pallet_balance_dialog, null);

        final EditText etInputJABBalance = (EditText) alertCustomLayout.findViewById(R.id.etInputJABbalance);
        final EditText etInputHedealance = (EditText) alertCustomLayout.findViewById(R.id.etInputHedeBalance);
        final EditText etInputFashionService = (EditText) alertCustomLayout.findViewById(R.id.etInputFashionServicebalance);

        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        alert.setTitle(getString(R.string.update_pallet_balance));
        alert.setView(alertCustomLayout);
        alert.setCancelable(false);
        alert.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        final AlertDialog dialog = alert.create();
        dialog.show();

        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String halfHour;
                if(!HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etInputJABBalance.getText().toString())
                   || !HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etInputHedealance.getText().toString())
                   || !HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etInputFashionService.getText().toString())){
                    String timeStamp = HelpMethods.getTimeStamp();
                    PalletBalanceUpdater palletBalanceUpdater = new PalletBalanceUpdater(timeStamp, etInputJABBalance.getText().toString(),
                                                                                         etInputHedealance.getText().toString(),
                                                                                         etInputFashionService.getText().toString(), true);
                    mProgressDialog = new ProgressDialog(getActivity());
                    mProgressDialog.setTitle(getString(R.string.wait));
                    mProgressDialog.setMessage(getString(R.string.updating_pallet_balance_on_server));
                    mProgressDialog.show();

                    DbHelperMethods.postRequester(getActivity(), HelpMethods.prepareReportDataObject(palletBalanceUpdater), palletBalanceUpdaterUrl, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getActivity(), getString(R.string.pallet_balance_update_success), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String message) {
                            HelpMethods.vibrate(getActivity(), getString(R.string.error_vibrate));
                            mProgressDialog.dismiss();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.dismiss();
                }
                else{
                    HelpMethods.vibrate(getActivity(), getString(R.string.error_vibrate));
                    Toast.makeText(getActivity(), getString(R.string.error_no_input_from_mandatory_fields), Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
        StringBuilder dateFilterBuilder;
        String dateFilterString = "";
        HashMap actualDateHolder = HelpMethods.getTodaysDateInHashMap();
        String thisYear = actualDateHolder.get("year").toString();
        String thisMonth = actualDateHolder.get("month").toString();
        String thisDay = actualDateHolder.get("day").toString();
        String thisWeek = actualDateHolder.get("week").toString();
        String todaysDateFormatted = HelpMethods.getTodaysDate();

        if(reportList.size()!=0 || reportList == null){
            if(rgIntervalPickerGroup.getCheckedRadioButtonId()==rbPickToday.getId()) {

                if (reportList.get(0).getClass().isAssignableFrom(TimeReport.class)) {
                    for (Object obj : reportList) {
                        dateFilterBuilder = new StringBuilder();
                        dateFilterBuilder.append(((TimeReport) obj).getYear());
                        dateFilterBuilder.append("-");
                        dateFilterBuilder.append(((TimeReport) obj).getMonth());
                        dateFilterBuilder.append("-");
                        dateFilterBuilder.append(((TimeReport) obj).getDay());
                        dateFilterString = dateFilterBuilder.toString();

                        if (dateFilterString.equals(todaysDateFormatted)) {
                            parsedArrayList.add(obj);
                        }
                    }
                }
                if(reportList.get(0).getClass().isAssignableFrom(PalletReport.class)){
                    for (Object obj: reportList) {
                        HashMap dateSplittedFromPalletReport = HelpMethods.getDateFromPalletReportTimeStamp(((PalletReport) obj).getReportTimeStamp());
                        String palletReportYear = dateSplittedFromPalletReport.get("year").toString();
                        String palletReportMonth = dateSplittedFromPalletReport.get("month").toString();;
                        String palletReportDay = dateSplittedFromPalletReport.get("day").toString();;
                        String palletReportDayFormatted = palletReportYear+"-"+palletReportMonth+"-"+palletReportDay;

                        if(palletReportDayFormatted.equals(todaysDateFormatted)){
                            parsedArrayList.add(obj);
                        }
                    }
                }
            }

            if(rgIntervalPickerGroup.getCheckedRadioButtonId()==rbPickThisWeek.getId()){
                if(reportList.get(0).getClass().isAssignableFrom(TimeReport.class)){
                    for (Object obj: reportList) {
                        if(((TimeReport) obj).getYear().equals(thisYear) && ((TimeReport) obj).getWeek().equals(thisWeek)){
                            parsedArrayList.add(obj);
                        }
                    }
                }
                if(reportList.get(0).getClass().isAssignableFrom(PalletReport.class)){
                    for (Object obj: reportList) {
                        HashMap dateSplittedFromPalletReport = HelpMethods.getDateFromPalletReportTimeStamp(((PalletReport) obj).getReportTimeStamp());
                        String palletReportYear = dateSplittedFromPalletReport.get("year").toString();
                        String palletReportWeek = dateSplittedFromPalletReport.get("week").toString();;

                        if(palletReportYear.equals(thisYear) && palletReportWeek.equals(thisWeek)){
                            parsedArrayList.add(obj);
                        }
                    }
                }
            }
            if(rgIntervalPickerGroup.getCheckedRadioButtonId()==rbPickThisMonth.getId()){
                if(reportList.get(0).getClass().isAssignableFrom(TimeReport.class)) {
                    for (Object obj: reportList) {
                        if(((TimeReport) obj).getYear().equals(thisYear) && ((TimeReport) obj).getMonth().equals(thisMonth)){
                            parsedArrayList.add(obj);
                        }
                    }
                }
                if(reportList.get(0).getClass().isAssignableFrom(PalletReport.class)){
                    for (Object obj: reportList) {
                        HashMap dateSplittedFromPalletReport = HelpMethods.getDateFromPalletReportTimeStamp(((PalletReport) obj).getReportTimeStamp());
                        String palletReportYear = dateSplittedFromPalletReport.get("year").toString();
                        String palletReportMonth = dateSplittedFromPalletReport.get("month").toString();;

                        if(palletReportYear.equals(thisYear) && palletReportMonth.equals(thisMonth)){
                            parsedArrayList.add(obj);
                        }
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
                    ((TimeReport) object).setReportId(tempObj.getString("tRId"));
                    ((TimeReport) object).setYear(tempObj.getString("year"));
                    ((TimeReport) object).setMonth(tempObj.getString("month"));
                    ((TimeReport) object).setDay(tempObj.getString("day"));
                    ((TimeReport) object).setWeek(tempObj.getString("week"));
                    ((TimeReport) object).setReportDriverName(tempObj.getString("driver"));
                    ((TimeReport) object).setReportReporterId(tempObj.getString("driverId"));
                    ((TimeReport) object).setCostumer(tempObj.getString("costumer"));
                    ((TimeReport) object).setArea(tempObj.getString("area"));
                    ((TimeReport) object).setHours(tempObj.getString("hours"));
                    ((TimeReport) object).setRoute(tempObj.getBoolean("isRoute"));
                    ((TimeReport) object).setWorkDescription(tempObj.getString("workDescription"));
                    ((TimeReport) object).setChangedByAdmin(tempObj.getBoolean("changedByAdmin"));
                    ((TimeReport) object).setReportReporterName(tempObj.getString("reportedBy"));
                    ((TimeReport) object).setReportTimeStamp(tempObj.getString("inputTimeStamp"));

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
                    ((PalletReport) object).setReportId(tempObj.getString("pRId"));
                    ((PalletReport) object).setReportTimeStamp(tempObj.getString("inputTimeStamp"));
                    ((PalletReport) object).setReportDriverName(tempObj.getString("driver"));
                    ((PalletReport) object).setReportReporterId(tempObj.getString("driverId"));
                    ((PalletReport) object).setFromPlace(tempObj.getString("fromPlace"));
                    ((PalletReport) object).setToPlace(tempObj.getString("toPlace"));
                    ((PalletReport) object).setNoOfpallets(tempObj.getString("noOfpallets"));
                    ((PalletReport) object).setReportReporterName(tempObj.getString("reportedBy"));

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