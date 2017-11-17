package com.a0.ztransport2.robinwilde.ztransport2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private TabsPagerAdapterAdmin adapter;
    private TabLayout tabLayout;
    private final String LOG_TAG = "AdminActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<String> tabNames = new ArrayList<>();
        tabNames.add(getString(R.string.tab_title_admin_generate_report));
        tabNames.add(getString(R.string.tab_title_admin_report_view));
        tabNames.add(getString(R.string.tab_title_employees));
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new TabsPagerAdapterAdmin(getSupportFragmentManager(), tabNames);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
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
            case R.id.action_main_menu:
                Intent adminIntentStarter = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(adminIntentStarter);
                finishAffinity();
        }
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            int position = tabLayout.getSelectedTabPosition();
            Fragment fragment = adapter.getFragment(tabLayout
                    .getSelectedTabPosition());
            if (fragment != null) {
                switch (position) {
                    case 0:
                        ((GenerateReportFragment) fragment).onRefresh();
                        break;
                    case 1:
                        ((ReportsFragment) fragment).onRefresh();
                        break;
                    case 2:
                        ((EmployeeFragment) fragment).onRefresh();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
