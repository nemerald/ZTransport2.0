package com.a0.ztransport2.robinwilde.ztransport2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReportsFragment extends Fragment {

    RadioGroup rgReportPickerGroup, rgIntervalPickerGroup;
    RadioButton rbPickPalletReport, rbPickTimeReport, rbPickToday, rbPickThisWeek, rbPickThisMonth;
    private ProgressDialog mProgressDialog;

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

        getReportsFromDb();
    }

    private void getReportsFromDb() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.wait));
        mProgressDialog.setMessage(getString(R.string.getting_reports));
        mProgressDialog.show();

        DbHelperMethods.getRequester(getActivity(), getReportsUrl, null, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                mProgressDialog.dismiss();
                try {
                    JSONArray jsonTimeReportsArray = response.getJSONArray("timeReportArray");
                    JSONArray jsonPalletReportsArray = response.getJSONArray("palletReportArray");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.error_network_error)+" "+message, Toast.LENGTH_SHORT).show();
            }
        });

        setReportDataToList();
    }

    private void setReportDataToList() {
    }

    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment Reports: Refresh called.",
                Toast.LENGTH_SHORT).show();
    }
}