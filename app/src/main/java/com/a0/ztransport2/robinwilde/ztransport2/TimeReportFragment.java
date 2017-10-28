package com.a0.ztransport2.robinwilde.ztransport2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.TimeReport;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.User;

import static com.a0.ztransport2.robinwilde.ztransport2.HelpMethods.vibrate;

public class TimeReportFragment extends Fragment {
    FragmentCommunicator mCallback;
    SharedPreferences sharedPreferences;

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
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        if(!HelpMethods.ifSharedPrefsHoldsData(sharedPreferences, getContext())){
            setUserData();
        }
    }

    private void setUserData() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertCustomLayout = inflater.inflate(R.layout.custom_ask_user_to_input_user_data_dialog, null);

        final EditText etUserName = (EditText) alertCustomLayout.findViewById(R.id.etUserName);
        final EditText etEmail = (EditText) alertCustomLayout.findViewById(R.id.etEmail);
        final EditText etPhoneNumber = (EditText) alertCustomLayout.findViewById(R.id.etPhoneNumber);

        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        alert.setTitle(TimeReportFragment.this.getString(R.string.user_data_input));
        alert.setView(alertCustomLayout);
        alert.setCancelable(false);
        alert.setPositiveButton(TimeReportFragment.this.getString(R.string.confirm), new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        alert.setNegativeButton(TimeReportFragment.this.getString(R.string.cancel), new DialogInterface.OnClickListener(){

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
                if(HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etUserName.getText().toString()) ||
                        HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etEmail.getText().toString()) ||
                        HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etPhoneNumber.getText().toString())){
                    vibrate(getContext(), getString(R.string.error_vibrate));
                    Toast.makeText(getActivity(), TimeReportFragment.this.getString(R.string.error_no_input_from_user), Toast.LENGTH_LONG).show();

                }
                else if(!HelpMethods.isEmailValid(etEmail.getText().toString())){
                    vibrate(getContext(), getString(R.string.error_vibrate));
                    Toast.makeText(getActivity(), TimeReportFragment.this.getString(R.string.error_not_correct_email), Toast.LENGTH_LONG).show();
                }
                else{
                    User user = new User(etUserName.getText().toString(), etEmail.getText().toString(),
                                        etPhoneNumber.getText().toString(), false);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.shared_prefs_user_id), user.getuId());
                    editor.putString(getString(R.string.shared_prefs_user_name), user.getName());
                    editor.putString(getString(R.string.shared_prefs_user_email), user.geteMail());
                    editor.putString(getString(R.string.shared_prefs_user_phone_number), user.getPhoneNumber());
                    editor.putBoolean(getString(R.string.shared_prefs_is_admin), user.getIsAdmin());
                    editor.commit();

                    dialog.dismiss();
                }

            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finishAffinity();

            }
        });
    }

    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment Time Report: Refresh called.",
                Toast.LENGTH_SHORT).show();
    }
}