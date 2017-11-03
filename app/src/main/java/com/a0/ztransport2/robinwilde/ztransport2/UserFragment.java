package com.a0.ztransport2.robinwilde.ztransport2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.User;

public class UserFragment extends Fragment {
    FragmentCommunicator mCallback;

    EditText etUserName, etUserMail, etUserPhoneNumber;
    CheckBox cbMakeUpdateInfoPossible;
    Button bUpdateInfo;

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
                    setDefaultState();
                }
            }
        });
        bUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultState();
            }
        });

        setDefaultState();
        setUserData();
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

        String userName = sharedPreferences.getString(getString(R.string.shared_prefs_user_name), null);
        String userMail = sharedPreferences.getString(getString(R.string.shared_prefs_user_email), null);
        String userPhoneNumber = sharedPreferences.getString(getString(R.string.shared_prefs_user_phone_number), null);

        etUserName.setText(userName);
        etUserMail.setText(userMail);
        etUserPhoneNumber.setText(userPhoneNumber);
    }
}