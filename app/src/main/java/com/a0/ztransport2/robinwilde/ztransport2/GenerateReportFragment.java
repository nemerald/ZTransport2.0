package com.a0.ztransport2.robinwilde.ztransport2;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
                            etOtherWeek.setText(yearMonthDayWeekHashMap.get("week").toString());

                        }
                        break;
                    case R.id.rbMonthReport:
                        if(rbPickedMonthReport.isChecked()){
                            TransitionManager.beginDelayedTransition(IlMainLayout);
                            IlWeekReport.setVisibility(View.GONE);
                            IlMonthReport.setVisibility(View.VISIBLE);
                            bGenerateAndSendReport.setEnabled(true);

                            spPickMonth.setSelection(Integer.parseInt(yearMonthDayWeekHashMap.get("month").toString())-1);
                            spPickYear.setSelection(1);

                        }
                        break;
                }
            }
        });

        setSpinnerValues();
        setDefaultState();

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

        rgIntervalPicker.clearCheck();

        TransitionManager.beginDelayedTransition(IlMainLayout);
        IlWeekReport.setVisibility(View.GONE);
        IlMonthReport.setVisibility(View.GONE);

        bGenerateAndSendReport.setEnabled(false);
    }

    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment Generate Reports: Refresh called.",
                Toast.LENGTH_SHORT).show();
    }

    public void getReportArrays(ArrayList<ArrayList> reportArrays) {
        this.reportArrays = reportArrays;
    }
}