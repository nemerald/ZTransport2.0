package com.a0.ztransport2.robinwilde.ztransport2.Objects;

import java.util.UUID;

/**
 * Created by robin.wilde on 2017-10-27.
 */

public class User {
    private String uId = UUID.randomUUID().toString();
    private String name;
    private String phoneNumber;
    private String eMail;
    private boolean isAdmin;

    public User(String name, String phoneNumber, String eMail, boolean isAdmin){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.eMail = eMail;
        this.isAdmin = isAdmin;
    }

    public String getuId() {
        return uId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String geteMail() {
        return eMail;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    @Override
    public String toString() {
        return "User{}";
    }
}
