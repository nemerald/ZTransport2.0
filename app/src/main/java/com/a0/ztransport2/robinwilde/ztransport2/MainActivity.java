package com.a0.ztransport2.robinwilde.ztransport2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.a0.ztransport2.robinwilde.ztransport2.HelpMethods.vibrate;

public class MainActivity extends AppCompatActivity implements FragmentCommunicator{

    private TabsPagerAdapter adapter;
    private TabLayout tabLayout;
    private final String LOG_TAG = "MainActivity";
    SharedPreferences sharedPreferences;
    User user;
    ProgressDialog mProgressDialog;

    //TODO ta bort vid senare tillfälle.
    String newUserUrl = "https://prod-10.northeurope.logic.azure.com:443/workflows/d6583a8f3ba7432897b6063b5609821e/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=hh06olZG4uQtLd1W_tmJ7q54y7hXKCcKRQa3kMsgeW8";
    String getUserAdminStatusUrl = "https://prod-07.northeurope.logic.azure.com:443/workflows/18f0b6fb8a404d669937487b37b6630a/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=N2ZuR7tFwB9TEXEDzZ1oGXegqmRqZCsrLsBFo8pBFRg";
    String checkIfNewUserIsValidUrl="https://prod-13.northeurope.logic.azure.com:443/workflows/48562dd612944dc09e0290871d4fe273/triggers/request/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Frequest%2Frun&sv=1.0&sig=jEej9NkRKbTizRFrZ0Ep7m7Wk_GnZqQSGY2S3ZU0QWg";
    String sendNewUserErrorWarningUrl = "https://prod-09.northeurope.logic.azure.com:443/workflows/fd26e84d7a4040edb3aeb987f0cb95e0/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=_9AEqNINnoDXTXySr54qEk8Sii394otrqS7q6sKWmlc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_preference_name),Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<String> tabNames = new ArrayList<>();
        tabNames.add(getString(R.string.tab_title_user));
        tabNames.add(getString(R.string.tab_title_time_report));
        tabNames.add(getString(R.string.tab_title_pallet_report));
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new TabsPagerAdapter(getSupportFragmentManager(), tabNames);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

        if(!HelpMethods.ifSharedPrefsHoldsData(this)){
            newUserGateDialog();
        }
    }

    private void newUserGateDialog() {

        LayoutInflater inflater = this.getLayoutInflater();
        View alertCustomLayout = inflater.inflate(R.layout.custom_new_user_gate_dialog, null);

        final EditText etNewUserMailAdress = (EditText) alertCustomLayout.findViewById(R.id.etNewUserMailAdress);
        final EditText etPinCode = (EditText) alertCustomLayout.findViewById(R.id.etPinCode);

        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        alert.setTitle(getString(R.string.new_user_data_input));
        alert.setView(alertCustomLayout);
        alert.setCancelable(false);
        alert.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        final AlertDialog dialog = alert.create();
        dialog.show();

        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etNewUserMailAdress.getText().toString()) ||
                        HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etPinCode.getText().toString())){
                    vibrate(MainActivity.this, getString(R.string.error_vibrate));
                    Toast.makeText(MainActivity.this, getString(R.string.error_no_input_from_user), Toast.LENGTH_LONG).show();
                }
                else if(!HelpMethods.isEmailValid(etNewUserMailAdress.getText().toString().trim())){
                    vibrate(MainActivity.this, getString(R.string.error_vibrate));
                    Toast.makeText(MainActivity.this, getString(R.string.error_not_correct_email), Toast.LENGTH_LONG).show();
                }
                else{
                    final JSONObject newUserData = new JSONObject();
                    try{
                        newUserData.put("newUserEmail", etNewUserMailAdress.getText().toString().trim());
                        newUserData.put("newUserPinCode", etPinCode.getText().toString().trim());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    mProgressDialog = new ProgressDialog(MainActivity.this);
                    mProgressDialog.setTitle(getString(R.string.wait));
                    mProgressDialog.setMessage(getString(R.string.check_input_is_valid));
                    mProgressDialog.show();
                    DbHelperMethods.getRequester(MainActivity.this, checkIfNewUserIsValidUrl, newUserData, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                mProgressDialog.dismiss();
                                JSONArray newUserArray = response.getJSONArray("newUserArray");
                                for(int i=0;i<newUserArray.length();i++){
                                    JSONObject tempObj = new JSONObject();
                                    tempObj = newUserArray.getJSONObject(i);
                                    if(tempObj.getString("newUserMailAdress").equals(etNewUserMailAdress.getText().toString().trim()) &&
                                            tempObj.getString("newUserPinCode").equals(etPinCode.getText().toString().trim())){
                                        boolean isAdmin = tempObj.getBoolean("newUserIsAdmin");
                                        boolean isSuperAdmin = tempObj.getBoolean("newUserIsSuperAdmin");
                                        boolean hasPermissionToReport = tempObj.getBoolean("newUserHasPermissionToReport");
                                        requestUserDataDialog(etNewUserMailAdress.getText().toString().trim(), isAdmin, isSuperAdmin, hasPermissionToReport);
                                        dialog.dismiss();
                                        break;
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, getString(R.string.error_input_no_match), Toast.LENGTH_SHORT).show();

                                        DbHelperMethods.postRequester(MainActivity.this, newUserData, sendNewUserErrorWarningUrl, new VolleyCallback() {
                                            @Override
                                            public void onSuccess(JSONObject result) {

                                            }

                                            @Override
                                            public void onError(String message) {

                                            }
                                        });
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String message) {
                            mProgressDialog.dismiss();
                            Toast.makeText(MainActivity.this, getString(R.string.error_network_error)+" "+message, Toast.LENGTH_SHORT).show();
                            MainActivity.this.finishAffinity();
                        }
                    });
                }

            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finishAffinity();

            }
        });


    }

    private void requestUserDataDialog(String userEmail, final boolean isAdmin, final boolean isSuperAdmin, final boolean hasPermissionToReport) {
        LayoutInflater inflater = this.getLayoutInflater();
        View alertCustomLayout = inflater.inflate(R.layout.custom_ask_user_to_input_user_data_dialog, null);

        final EditText etUserName = (EditText) alertCustomLayout.findViewById(R.id.etUserName);
        final EditText etEmail = (EditText) alertCustomLayout.findViewById(R.id.etEmail);
        final EditText etPhoneNumber = (EditText) alertCustomLayout.findViewById(R.id.etPhoneNumber);

        etEmail.setText(userEmail);
        etEmail.setEnabled(false);

        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        alert.setTitle(getString(R.string.user_data_input));
        alert.setView(alertCustomLayout);
        alert.setCancelable(false);
        alert.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
        final AlertDialog dialog = alert.create();
        dialog.show();

        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etUserName.getText().toString().trim()) ||
                        HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etEmail.getText().toString().trim()) ||
                        HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etPhoneNumber.getText().toString().trim())){
                    vibrate(MainActivity.this, getString(R.string.error_vibrate));
                    Toast.makeText(MainActivity.this, getString(R.string.error_no_input_from_user), Toast.LENGTH_LONG).show();

                }
                else if(!HelpMethods.isEmailValid(etEmail.getText().toString().trim())){
                    vibrate(MainActivity.this, getString(R.string.error_vibrate));
                    Toast.makeText(MainActivity.this, getString(R.string.error_not_correct_email), Toast.LENGTH_LONG).show();
                }
                else{
                    ArrayList<String> userInput = new ArrayList<>();
                    userInput.add(HelpMethods.setFirstCharacterToUpperCase(etUserName.getText().toString().trim()));
                    userInput.add(etPhoneNumber.getText().toString().trim());
                    userInput.add(etEmail.getText().toString().trim());
                    //TODO check if user exist in Database
                    setSharedPrefsAndSendUserData(userInput, isAdmin, isSuperAdmin, hasPermissionToReport);

                    dialog.dismiss();
                }

            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finishAffinity();

            }
        });
    }
    private void setSharedPrefsAndSendUserData(ArrayList userInput, boolean isAdmin, boolean isSuperAdmin, boolean hasPermissionToReport) {

        user = new User(userInput.get(0).toString(), userInput.get(1).toString(),
                userInput.get(2).toString(), isAdmin, isSuperAdmin, hasPermissionToReport);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.shared_prefs_user_id), user.getuId());
        editor.putString(getString(R.string.shared_prefs_user_name), user.getName());
        editor.putString(getString(R.string.shared_prefs_user_email), user.geteMail());
        editor.putString(getString(R.string.shared_prefs_user_phone_number), user.getPhoneNumber());
        editor.putBoolean(getString(R.string.shared_prefs_is_admin), user.getIsAdmin());
        editor.putBoolean(getString(R.string.shared_prefs_is_super_admin), user.getIsSuperAdmin());
        editor.commit();

        JSONObject data = (HelpMethods.prepareReportDataObject(user));
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setTitle(getString(R.string.wait));
        mProgressDialog.setMessage(getString(R.string.adding_user));
        mProgressDialog.show();
        DbHelperMethods.postRequester(this, data, newUserUrl, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                mProgressDialog.dismiss();
                Toast.makeText(MainActivity.this, getString(R.string.new_user_success), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                mProgressDialog.dismiss();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                MainActivity.this.finishAffinity();
            }
        });

        setUserDataInUserFragment();
        setSharedPrefsInTimeReportFragment();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_menu_setup, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_administration:

                if(sharedPreferences.getBoolean(getString(R.string.shared_prefs_is_super_admin), false)){
                    Intent adminIntentStarter = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(adminIntentStarter);
                    finishAffinity();
                }
                else{
                    controlIfIsAdminStatusHasChanged();
                }
        }
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            int position = tabLayout.getSelectedTabPosition();
            Fragment fragment = adapter.getFragment(tabLayout
                    .getSelectedTabPosition());
            if (fragment != null) {
                switch (position) {
                    case 0:
                        ((UserFragment) fragment).onRefresh();
                        break;
                    case 1:
                        ((TimeReportFragment) fragment).onRefresh();
                        break;
                    case 2:
                        ((PalletReportFragment) fragment).onRefresh();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void controlIfIsAdminStatusHasChanged() {
        DbHelperMethods.getRequester(this, getUserAdminStatusUrl, null, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                checkIfAdminStatusHasChanged(response);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, getString(R.string.error_network_error)+" "+message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfAdminStatusHasChanged(JSONObject response) {
        try {
            JSONArray employeeArray = response.getJSONArray("employeeArray");
            for(int i=0;i<employeeArray.length();i++){
                JSONObject tempObj = employeeArray.getJSONObject(i);
                String tempId = sharedPreferences.getString(getString(R.string.shared_prefs_user_id), null);
                if(tempObj.getString("uId").equals(tempId)){
                    boolean isUserStillValid = tempObj.getBoolean("hasPermissionToReport");
                    if(isUserStillValid){
                        boolean tempBool = tempObj.getBoolean("isAdmin");
                        if(tempBool){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(getString(R.string.shared_prefs_is_admin), true);
                            editor.commit();

                            Intent adminIntentStarter = new Intent(MainActivity.this, AdminActivity.class);
                            startActivity(adminIntentStarter);
                            finishAffinity();
                        }else{
                            Toast.makeText(this, getString(R.string.not_authorized), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        finishAffinity();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setUserDataInUserFragment() {
        UserFragment fragment = (UserFragment) adapter.getFragment(0);
        if (fragment != null) {
            fragment.setUserData();
        } else {
            Log.i(LOG_TAG, "UserFragment is not initialized");
        }
    }
    @Override
    public void sendDateToFragment(String date) {
        TimeReportFragment fragment = (TimeReportFragment) adapter.getFragment(1);
        if (fragment != null) {
            fragment.setDateFromFragment(date);
        } else {
            Log.i(LOG_TAG, "UserFragment is not initialized");
        }
    }
    @Override
    public void setSharedPrefsInTimeReportFragment() {
        TimeReportFragment fragment = (TimeReportFragment) adapter.getFragment(1);
        if (fragment != null) {
            fragment.setSharedPref();
        } else {
            Log.i(LOG_TAG, "UserFragment is not initialized");
        }
    }
}
