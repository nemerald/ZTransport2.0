package com.a0.ztransport2.robinwilde.ztransport2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class TimeReportFragment extends Fragment {
    FragmentCommunicator mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (FragmentCommunicator) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentCommunicator");
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

        return inflater.inflate(R.layout.fragment_time_report, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment Time Report: Refresh called.",
                Toast.LENGTH_SHORT).show();
    }
}