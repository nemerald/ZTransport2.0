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
    private boolean hasPermissionToReport;

    public User(){

    }

    public User(String name, String phoneNumber, String eMail, boolean isAdmin, boolean hasPermissionToReport){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.eMail = eMail;
        this.isAdmin = isAdmin;
        this.hasPermissionToReport = hasPermissionToReport;

    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public boolean getHasPermissionToReport() {
        return hasPermissionToReport;
    }

    @Override
    public String toString() {
        return "User{}";
    }
}
