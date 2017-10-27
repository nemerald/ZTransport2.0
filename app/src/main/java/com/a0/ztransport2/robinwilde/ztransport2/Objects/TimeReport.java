package com.a0.ztransport2.robinwilde.ztransport2.Objects;

import java.util.UUID;

/**
 * Created by robin.wilde on 2017-10-27.
 */

public class TimeReport {
    private String tRId = UUID.randomUUID().toString();
    private String year;
    private String month;
    private String day;
    private String week;
    private String driver;
    private String costumer;
    private String area;
    private String hours;
    private boolean isRoute;
    private String workDescription;
    private boolean changedByAdmin;
    private String reportedBy;
    private String inputTimeStamp;

    public TimeReport(String year, String month, String day, String week, String driver,
                      String costumer, String area, String hours, boolean isRoute,
                      String workDescription, boolean changedByAdmin, String reportedBy,
                      String inputTimeStamp){
        this.year = year;
        this.month = month;
        this.day = day;
        this.week = week;
        this.driver = driver;
        this.costumer = costumer;
        this.area = area;
        this.hours = hours;
        this.isRoute = isRoute;
        this.workDescription = workDescription;
        this.changedByAdmin = changedByAdmin;
        this.reportedBy = reportedBy;
        this.inputTimeStamp = inputTimeStamp;
    }

    public String gettRId() {
        return tRId;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getWeek() {
        return week;
    }

    public String getDriver() {
        return driver;
    }

    public String getCostumer() {
        return costumer;
    }

    public String getArea() {
        return area;
    }

    public String getHours() {
        return hours;
    }

    public boolean isRoute() {
        return isRoute;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public boolean isChangedByAdmin() {
        return changedByAdmin;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public String getInputTimeStamp() {
        return inputTimeStamp;
    }

    @Override
    public String toString() {
        return "TimeReport{}";
    }
}
