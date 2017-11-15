package com.a0.ztransport2.robinwilde.ztransport2.Objects;

import java.util.UUID;

public class PalletReport {



    private String pRId = UUID.randomUUID().toString();
    private String inputTimeStamp;
    private String driver;
    private String driverId;
    private String fromPlace;
    private String toPlace;
    private String noOfpallets;
    private String reportedBy;

    public PalletReport(String inputTimeStamp, String driver, String driverId,
                        String fromPlace, String toPlace, String noOfpallets, String reportedBy) {

        this.inputTimeStamp = inputTimeStamp;
        this.driver = driver;
        this.driverId = driverId;
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.noOfpallets = noOfpallets;
        this.reportedBy = reportedBy;
    }

    public String getpRId() {
        return pRId;
    }

    public String getInputTimeStamp() {
        return inputTimeStamp;
    }

    public String getDriver() {
        return driver;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public String getNoOfpallets() {
        return noOfpallets;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setpRId(String pRId) {
        this.pRId = pRId;
    }

    public void setInputTimeStamp(String inputTimeStamp) {
        this.inputTimeStamp = inputTimeStamp;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public void setNoOfpallets(String noOfpallets) {
        this.noOfpallets = noOfpallets;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    @Override
    public String toString() {
        return "PalletReport{}";
    }
}
