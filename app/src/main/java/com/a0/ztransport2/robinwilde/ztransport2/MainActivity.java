package com.a0.ztransport2.robinwilde.ztransport2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.User;

import java.util.ArrayList;

import static com.a0.ztransport2.robinwilde.ztransport2.HelpMethods.vibrate;

public class MainActivity extends AppCompatActivity implements FragmentCommunicator{

    private TabsPagerAdapter adapter;
    private TabLayout tabLayout;
    private final String LOG_TAG = "MainActivity";
    SharedPreferences sharedPreferences;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_preference_name),Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<String> tabNames = new ArrayList<>();
        tabNames.add(MainActivity.this.getString(R.string.tab_title_user));
        tabNames.add(MainActivity.this.getString(R.string.tab_title_time_report));
        tabNames.add(MainActivity.this.getString(R.string.tab_title_log_and_status));
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new TabsPagerAdapter(getSupportFragmentManager(), tabNames);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

        if(!HelpMethods.ifSharedPrefsHoldsData(this)){
            requestUserDataDialog();
        }
    }
    private void requestUserDataDialog() {
        LayoutInflater inflater = this.getLayoutInflater();
        View alertCustomLayout = inflater.inflate(R.layout.custom_ask_user_to_input_user_data_dialog, null);

        final EditText etUserName = (EditText) alertCustomLayout.findViewById(R.id.etUserName);
        final EditText etEmail = (EditText) alertCustomLayout.findViewById(R.id.etEmail);
        final EditText etPhoneNumber = (EditText) alertCustomLayout.findViewById(R.id.etPhoneNumber);

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
                if(HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etUserName.getText().toString()) ||
                        HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etEmail.getText().toString()) ||
                        HelpMethods.checkIfStringIsEmptyOrBlankOrNull(etPhoneNumber.getText().toString())){
                    vibrate(MainActivity.this, getString(R.string.error_vibrate));
                    Toast.makeText(MainActivity.this, getString(R.string.error_no_input_from_user), Toast.LENGTH_LONG).show();

                }
                else if(!HelpMethods.isEmailValid(etEmail.getText().toString())){
                    vibrate(MainActivity.this, getString(R.string.error_vibrate));
                    Toast.makeText(MainActivity.this, getString(R.string.error_not_correct_email), Toast.LENGTH_LONG).show();
                }
                else{
                    ArrayList<String> userInput = new ArrayList<>();
                    userInput.add(etUserName.getText().toString().trim());
                    userInput.add(etPhoneNumber.getText().toString().trim());
                    userInput.add(etEmail.getText().toString().trim());
                    //TODO check if user exist in Database
                    setSharedPrefsAndSendUserData(userInput);

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
    private void setSharedPrefsAndSendUserData(ArrayList userInput) {

        user = new User(userInput.get(0).toString(), userInput.get(1).toString(),
                userInput.get(2).toString(), false, false);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.shared_prefs_user_id), user.getuId());
        editor.putString(getString(R.string.shared_prefs_user_name), user.getName());
        editor.putString(getString(R.string.shared_prefs_user_email), user.geteMail());
        editor.putString(getString(R.string.shared_prefs_user_phone_number), user.getPhoneNumber());
        editor.putBoolean(getString(R.string.shared_prefs_is_admin), user.getIsAdmin());
        editor.commit();

        //TODO insert to database

        setUserDataInUserFragment();
        setSharedPrefsInTimeReportFragment();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                        ((LogAndStatusFragment) fragment).onRefresh();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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
