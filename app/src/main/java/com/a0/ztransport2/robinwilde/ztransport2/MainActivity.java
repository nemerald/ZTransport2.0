package com.a0.ztransport2.robinwilde.ztransport2;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentCommunicator{


    private TabsPagerAdapter adapter;
    private TabLayout tabLayout;
    private final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
