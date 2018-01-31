package com.a0.ztransport2.robinwilde.ztransport2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class GenerateReportFragment extends Fragment{

    LinearLayout IlWeekReport, IlMonthReport, IlMainLayout;
    RadioGroup rgIntervalPicker;
    RadioButton rbPickedWeekReport, rbPickedMonthReport;
    Spinner spPickWeek, spPickMonth, spPickYear, spPickReportContent;
    EditText etOtherWeek;
    Button bGenerateAndSendReport;

    ArrayList<ArrayList> reportArrays = new ArrayList<>();
    HashMap yearMonthDayWeekHashMap = new HashMap();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_generate_report, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        IlMainLayout = (LinearLayout) view.findViewById(R.id.IlMainLayout);
        IlWeekReport = (LinearLayout) view.findViewById(R.id.llWeekReport);
        IlMonthReport = (LinearLayout) view.findViewById(R.id.llMonthReport);
        rgIntervalPicker = (RadioGroup) view.findViewById(R.id.rgReportIntervalPicker);
        rbPickedWeekReport = (RadioButton) view.findViewById(R.id.rbWeekReport);
        rbPickedMonthReport = (RadioButton) view.findViewById(R.id.rbMonthReport);
        spPickWeek = (Spinner) view.findViewById(R.id.spReportWeek);
        spPickMonth = (Spinner) view.findViewById(R.id.spReportMonth);
        spPickYear = (Spinner) view.findViewById(R.id.spReportYear);
        spPickReportContent = (Spinner) view.findViewById(R.id.spReportContent);
        etOtherWeek = (EditText) view.findViewById(R.id.etOtherWeek);
        bGenerateAndSendReport = (Button) view.findViewById(R.id.bGenerateAndSendReportFile);

        yearMonthDayWeekHashMap = HelpMethods.getTodaysDateInHashMap();

        rgIntervalPicker.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rbWeekReport:
                        if(rbPickedWeekReport.isChecked()){
                            TransitionManager.beginDelayedTransition(IlMainLayout);
                            IlWeekReport.setVisibility(View.VISIBLE);
                            IlMonthReport.setVisibility(View.GONE);
                            bGenerateAndSendReport.setEnabled(true);

                            etOtherWeek.setEnabled(false);

                            spPickWeek.setSelection(0);
                        }
                        break;
                    case R.id.rbMonthReport:
                        if(rbPickedMonthReport.isChecked()){

                            etOtherWeek.clearFocus();
                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etOtherWeek.getWindowToken(), 0);

                            TransitionManager.beginDelayedTransition(IlMainLayout);
                            IlWeekReport.setVisibility(View.GONE);
                            IlMonthReport.setVisibility(View.VISIBLE);
                            bGenerateAndSendReport.setEnabled(true);

                            spPickMonth.setSelection(Integer.parseInt(yearMonthDayWeekHashMap.get("month").toString())-1);
                            spPickYear.setSelection(getYearId());

                        }
                        break;
                }
            }
        });
        spPickWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        etOtherWeek.setEnabled(false);
                        etOtherWeek.setText(yearMonthDayWeekHashMap.get("week").toString());
                        etOtherWeek.clearFocus();
                        break;
                    case 1:
                        etOtherWeek.setEnabled(true);
                        etOtherWeek.setText("");
                        etOtherWeek.requestFocus();

                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etOtherWeek, 0);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bGenerateAndSendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rgIntervalPicker.getCheckedRadioButtonId() == R.id.rbWeekReport){
                    if(HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etOtherWeek.getText().toString())){
                        Toast.makeText(getActivity(), R.string.error_no_week_picked, Toast.LENGTH_SHORT).show();
                        HelpMethods.vibrate(getActivity(), getString(R.string.error_vibrate));
                    }else if(Integer.parseInt(etOtherWeek.getText().toString())<=0 || Integer.parseInt(etOtherWeek.getText().toString())>53){
                        Toast.makeText(getActivity(), R.string.error_picked_week_out_of_index, Toast.LENGTH_SHORT).show();
                        HelpMethods.vibrate(getActivity(), getString(R.string.error_vibrate));
                    }
                    else{
                        showConfirmationDialog(R.id.rbWeekReport);
                    }
                }
                if(rgIntervalPicker.getCheckedRadioButtonId() == R.id.rbMonthReport){
                    showConfirmationDialog(R.id.rbMonthReport);
                }
            }
        });

        setSpinnerValues();
        setDefaultState();

    }

    private void showConfirmationDialog(int pickedReportInterval) {
        String reportConfirmationMessage="";
        String reportIntervalMessage="";
        if(pickedReportInterval==R.id.rbWeekReport){
            reportConfirmationMessage=getString(R.string.confirm_report_message_week);
            reportIntervalMessage=etOtherWeek.getText().toString();
        }
        if(pickedReportInterval==R.id.rbMonthReport){
            reportConfirmationMessage=getString(R.string.confirm_report_message_month);
            reportIntervalMessage=spPickYear.getSelectedItem().toString() +"/"+ spPickMonth.getSelectedItem().toString();
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertCustomLayout = inflater.inflate(R.layout.custom_confirm_report_interval_dialog, null);

        final TextView tvConfirmReportIntervalMsg = (TextView) alertCustomLayout.findViewById(R.id.tvConfirmMessage);
        final TextView tvConfirmInterval = (TextView) alertCustomLayout.findViewById(R.id.tvConfirmInterval);
        final TextView tvConfirmReportContent = (TextView) alertCustomLayout.findViewById(R.id.tvConfirmReportContent);

        tvConfirmReportIntervalMsg.setText(reportConfirmationMessage);
        tvConfirmInterval.setText(reportIntervalMessage);
        tvConfirmReportContent.setText(spPickReportContent.getSelectedItem().toString());

        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        alert.setTitle(getString(R.string.confirm_report_content));
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
                setDefaultState();
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

    private void setSpinnerValues() {

        ArrayAdapter<String> adapterWeekPicker = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getActivity().getResources().getStringArray(R.array.week_picker));
        adapterWeekPicker.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPickWeek.setAdapter(adapterWeekPicker);

        ArrayAdapter<String> adapterMonth = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getActivity().getResources().getStringArray(R.array.month_array));
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPickMonth.setAdapter(adapterMonth);

        //TODO do other solution than year array with hard coded years.
        ArrayAdapter<String> adapterYear = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getActivity().getResources().getStringArray(R.array.year_array));
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPickYear.setAdapter(adapterYear);

        ArrayAdapter<String> adapterPickContent = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getActivity().getResources().getStringArray(R.array.report_content_array));
        adapterPickContent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPickReportContent.setAdapter(adapterPickContent);
    }
    private void setDefaultState(){

        TransitionManager.beginDelayedTransition(IlMainLayout);
        IlWeekReport.setVisibility(View.GONE);
        IlMonthReport.setVisibility(View.GONE);

        rgIntervalPicker.clearCheck();
        spPickReportContent.setSelection(0);
        bGenerateAndSendReport.setEnabled(false);
    }
    private int getYearId() {
        if(yearMonthDayWeekHashMap.get("year").toString().equals("2017")){
            return 0;
        }
        if(yearMonthDayWeekHashMap.get("year").toString().equals("2018")){
            return 1;
        }
        if(yearMonthDayWeekHashMap.get("year").toString().equals("2019")){
            return 2;
        }
        if(yearMonthDayWeekHashMap.get("year").toString().equals("2020")){
            return 3;
        }
        if(yearMonthDayWeekHashMap.get("year").toString().equals("2021")){
            return 4;
        }
        else{
            return 0;
        }
    }
    public void getReportArrays(ArrayList<ArrayList> reportArrays) {
        this.reportArrays = reportArrays;
    }
    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment Generate Reports: Refresh called.",
                Toast.LENGTH_SHORT).show();
    }
}