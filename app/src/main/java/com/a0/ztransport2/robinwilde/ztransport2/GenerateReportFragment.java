package com.a0.ztransport2.robinwilde.ztransport2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class GenerateReportFragment extends Fragment{

    Spinner spPickWeek, spPickMonth, spPickYear, spPickReportContent;
    EditText etOtherWeek;
    Button bGenerateAndSendReport;

    ArrayList<ArrayList> reportArrays = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_generate_report, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        spPickWeek = (Spinner) view.findViewById(R.id.spReportWeek);
        spPickMonth = (Spinner) view.findViewById(R.id.spReportMonth);
        spPickYear = (Spinner) view.findViewById(R.id.spReportYear);
        spPickReportContent = (Spinner) view.findViewById(R.id.spReportContent);
        etOtherWeek = (EditText) view.findViewById(R.id.etOtherWeek);
        bGenerateAndSendReport = (Button) view.findViewById(R.id.bGenerateAndSendReportFile);

        setSpinnerValues();

    }
    private void setSpinnerValues() {

        spPickMonth.setEnabled(false);
        spPickYear.setEnabled(false);

        ArrayAdapter<String> adapterWeekPicker = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getActivity().getResources().getStringArray(R.array.week_picker));
        adapterWeekPicker.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPickWeek.setAdapter(adapterWeekPicker);

        ArrayAdapter<String> adapterMonth = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getActivity().getResources().getStringArray(R.array.month_array));
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPickMonth.setAdapter(adapterMonth);

        //TODO set actual years from the report array that exist
        //ArrayAdapter<String> adapterYear = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getActivity().getResources().getStringArray(R.array.));
        //adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spPickYear.setAdapter(adapterYear);

        ArrayAdapter<String> adapterPickContent = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getActivity().getResources().getStringArray(R.array.report_content_array));
        adapterPickContent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPickReportContent.setAdapter(adapterPickContent);
    }

    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment Generate Reports: Refresh called.",
                Toast.LENGTH_SHORT).show();
    }

    public void getReportArrays(ArrayList<ArrayList> reportArrays) {
        this.reportArrays = reportArrays;
    }
}