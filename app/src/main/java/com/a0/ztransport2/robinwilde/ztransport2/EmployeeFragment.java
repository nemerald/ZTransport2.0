package com.a0.ztransport2.robinwilde.ztransport2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.a0.ztransport2.robinwilde.ztransport2.NotificationHelpers.SendNotificationHelper;

public class EmployeeFragment extends Fragment {

    ListView lvEmployees;
    CheckBox cbReminder, cbFreeText;
    EditText etTextToEmployees;
    Button bSendMessageToEmployees;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_employees, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        lvEmployees = view.findViewById(R.id.lvEmployees);
        cbReminder = view.findViewById(R.id.cbReminder);
        cbFreeText = view.findViewById(R.id.cbFreeText);
        etTextToEmployees = view.findViewById(R.id.etTextToEmployees);
        bSendMessageToEmployees = view.findViewById(R.id.bSendMessageToEmployees);

        etTextToEmployees.setEnabled(false);
        bSendMessageToEmployees.setEnabled(false);

        cbReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbFreeText.isChecked()){
                    cbFreeText.setChecked(false);
                }
                if(!cbReminder.isChecked()){
                    etTextToEmployees.setText("");
                    bSendMessageToEmployees.setEnabled(false);
                }else{
                    bSendMessageToEmployees.setEnabled(true);
                    etTextToEmployees.setText("Var vänlig lägg in din tidrapport");
                }
                etTextToEmployees.setEnabled(false);
            }
        });

        cbFreeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbReminder.isChecked()){
                    cbReminder.setChecked(false);
                    bSendMessageToEmployees.setEnabled(true);
                }
                if(!cbFreeText.isChecked()){
                    etTextToEmployees.clearFocus();
                    etTextToEmployees.setEnabled(false);
                    bSendMessageToEmployees.setEnabled(false);
                }else{
                    bSendMessageToEmployees.setEnabled(true);
                    etTextToEmployees.setEnabled(true);
                    etTextToEmployees.setText("");
                    etTextToEmployees.requestFocus();
                }
            }
        });

        bSendMessageToEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendNotificationHelper sendObj = new SendNotificationHelper();
                sendObj.sendNotification(etTextToEmployees.getText().toString());

                etTextToEmployees.clearFocus();
                etTextToEmployees.setEnabled(false);
                etTextToEmployees.setText("");
                bSendMessageToEmployees.setEnabled(false);
                cbReminder.setChecked(false);
                cbFreeText.setChecked(false);
            }
        });

    }
    public void onRefresh() {
        Toast.makeText(getActivity(), "Fragment Reports: Refresh called.",
                Toast.LENGTH_SHORT).show();
    }
}