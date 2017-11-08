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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.a0.ztransport2.robinwilde.ztransport2.HelpMethods.vibrate;

public class UserFragment extends Fragment {
    FragmentCommunicator mCallback;

    EditText etUserName, etUserMail, etUserPhoneNumber;
    CheckBox cbMakeUpdateInfoPossible;
    Button bUpdateInfo;

    String oldUserName;
    String oldUserPhonerNumber;
    String oldUserEmail;

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

        return inflater.inflate(R.layout.fragment_user, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        etUserName = (EditText) view.findViewById(R.id.etUserFragmentUserName);
        etUserMail = (EditText) view.findViewById(R.id.etUserFragmentEmail);
        etUserPhoneNumber = (EditText) view.findViewById(R.id.etUserFragmentPhonenumber);
        cbMakeUpdateInfoPossible = (CheckBox) view.findViewById(R.id.cbActivateUpdateInfo);
        bUpdateInfo = (Button) view.findViewById(R.id.bUpdateUserInfo);

        cbMakeUpdateInfoPossible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cbMakeUpdateInfoPossible.isChecked()){
                    etUserName.setEnabled(true);
                    etUserPhoneNumber.setEnabled(true);
                    etUserMail.setEnabled(true);
                    bUpdateInfo.setEnabled(true);
                }
                else{
                    resetUserData();
                    setDefaultState();
                }
            }
        });
        bUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInfoChanged()){
                    showConfirmUserInfoChangeDialog();
                }
                else{
                    resetUserData();
                }
                setDefaultState();
            }
        });
        setDefaultState();
        setUserData();
    }

    private void showConfirmUserInfoChangeDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertCustomLayout = inflater.inflate(R.layout.custom_confirm_user_info_changed_dialog, null);

        final TextView tvNewUserName = (TextView) alertCustomLayout.findViewById(R.id.tvNewUserName);
        final TextView tvNewEmail = (TextView) alertCustomLayout.findViewById(R.id.tvNewEmail);
        final TextView tvNewPhoneNumber = (TextView) alertCustomLayout.findViewById(R.id.tvNewPhoneNumber);

        tvNewUserName.setText(etUserName.getText().toString());
        tvNewEmail.setText(etUserMail.getText().toString());
        tvNewPhoneNumber.setText(etUserPhoneNumber.getText().toString());

        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        alert.setTitle(UserFragment.this.getString(R.string.confirm_user_change_input));
        alert.setView(alertCustomLayout);
        alert.setCancelable(false);
        alert.setPositiveButton(UserFragment.this.getString(R.string.confirm), new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        alert.setNegativeButton(UserFragment.this.getString(R.string.cancel), new DialogInterface.OnClickListener(){

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
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                        getString(R.string.shared_preference_name), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(getString(R.string.shared_prefs_user_name), etUserName.getText().toString().trim());
                editor.putString(getString(R.string.shared_prefs_user_email), etUserMail.getText().toString().trim());
                editor.putString(getString(R.string.shared_prefs_user_phone_number), etUserPhoneNumber.getText().toString().trim());
                editor.commit();

                setUserData();
                mCallback.setSharedPrefsInTimeReportFragment();

                Toast.makeText(getActivity(), getString(R.string.user_info_changed), Toast.LENGTH_SHORT).show();
                //TODO update database

                dialog.dismiss();
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetUserData();

                dialog.dismiss();
            }
        });
    }

    private void resetUserData() {

        etUserName.setText(oldUserName);
        etUserPhoneNumber.setText(oldUserPhonerNumber);
        etUserMail.setText(oldUserEmail);
    }

    private boolean isInfoChanged() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                getString(R.string.shared_preference_name), Context.MODE_PRIVATE);

        String newUserName = etUserName.getText().toString().trim();
        String newUserPhonerNumber = etUserPhoneNumber.getText().toString().trim();
        String newUserEmail = etUserMail.getText().toString().trim();

        if(newUserName.equals(oldUserName) && newUserEmail.equals(oldUserEmail) && newUserPhonerNumber.equals(oldUserPhonerNumber)){
            Toast.makeText(getActivity(), getString(R.string.error_no_user_info_changed), Toast.LENGTH_SHORT).show();
            vibrate(getActivity(), getString(R.string.error_vibrate));
            return false;
        }
        else if(HelpMethods.checkIfStringIsEmptyOrBlankOrNull(newUserName) || HelpMethods.checkIfStringIsEmptyOrBlankOrNull(newUserPhonerNumber) ||
                HelpMethods.checkIfStringIsEmptyOrBlankOrNull(newUserEmail)){

                Toast.makeText(getActivity(), getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
                vibrate(getActivity(), getString(R.string.error_vibrate));
                return false;
        }
        else if(!HelpMethods.isEmailValid(newUserEmail)){
            Toast.makeText(getActivity(), getString(R.string.error_not_correct_email), Toast.LENGTH_SHORT).show();
            vibrate(getActivity(), getString(R.string.error_vibrate));
            return false;
        }
        else{
            return true;
        }
    }

    private void setDefaultState() {
        etUserName.setEnabled(false);
        etUserPhoneNumber.setEnabled(false);
        etUserMail.setEnabled(false);
        bUpdateInfo.setEnabled(false);
        cbMakeUpdateInfoPossible.setChecked(false);
    }

    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment user: Refresh called.",
                Toast.LENGTH_SHORT).show();
    }

    public void setUserData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(
                getString(R.string.shared_preference_name), Context.MODE_PRIVATE);

        oldUserName = sharedPreferences.getString(getString(R.string.shared_prefs_user_name), null);
        oldUserEmail = sharedPreferences.getString(getString(R.string.shared_prefs_user_email), null);
        oldUserPhonerNumber = sharedPreferences.getString(getString(R.string.shared_prefs_user_phone_number), null);

        etUserName.setText(oldUserName);
        etUserMail.setText(oldUserEmail);
        etUserPhoneNumber.setText(oldUserPhonerNumber);
    }
}