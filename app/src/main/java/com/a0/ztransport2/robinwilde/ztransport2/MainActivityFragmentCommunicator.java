package com.a0.ztransport2.robinwilde.ztransport2;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.User;

public interface MainActivityFragmentCommunicator {

    void setUserDataInUserFragment();
    void setSharedPrefsInTimeReportFragment();
    void sendDateToFragment(String date);
}
