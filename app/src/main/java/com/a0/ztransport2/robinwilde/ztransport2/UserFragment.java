package com.a0.ztransport2.robinwilde.ztransport2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.User;

public class UserFragment extends Fragment {
    FragmentCommunicator mCallback;

    EditText etUserName, etUserMail, etUserPhoneNumber;
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
        bUpdateInfo = (Button) view.findViewById(R.id.bUpdateUserInfo);

    }
    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment user: Refresh called.",
                Toast.LENGTH_SHORT).show();
    }

    public void getUserData(User user) {
        etUserName.setText(user.getName());
        etUserMail.setText(user.geteMail());
        etUserPhoneNumber.setText(user.getPhoneNumber());
    }
}