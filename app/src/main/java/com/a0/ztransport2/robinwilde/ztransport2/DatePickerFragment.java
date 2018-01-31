package com.a0.ztransport2.robinwilde.ztransport2;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    MainActivityFragmentCommunicator mCallback;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (MainActivityFragmentCommunicator) context;
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(c.getTime());

        mCallback.sendDateToFragment(formattedDate);
    }
}