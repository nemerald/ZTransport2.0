package com.a0.ztransport2.robinwilde.ztransport2;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.User;

public interface FragmentCommunicator {

    void sendUserToUserFragment(User user);
    void sendDateToFragment(String date);
}
