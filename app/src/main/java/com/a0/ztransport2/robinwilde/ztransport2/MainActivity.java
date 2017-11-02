package com.a0.ztransport2.robinwilde.ztransport2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.TimeReport;
import com.a0.ztransport2.robinwilde.ztransport2.Objects.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentCommunicator{

    private TabsPagerAdapter adapter;
    private TabLayout tabLayout;
    private final String LOG_TAG = "MainActivity";
    SharedPreferences sharedPreferences;

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
    public void sendUserToUserFragment(User user) {
        UserFragment fragment = (UserFragment) adapter.getFragment(0);
        if (fragment != null) {
            fragment.getUserData(user);
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
}
