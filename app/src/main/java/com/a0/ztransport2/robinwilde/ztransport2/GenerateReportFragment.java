package com.a0.ztransport2.robinwilde.ztransport2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class GenerateReportFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_generate_report, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment Generate Reports: Refresh called.",
                Toast.LENGTH_SHORT).show();
    }
}