package com.a0.ztransport2.robinwilde.ztransport2.Objects;

public class TimeReport extends Report {

        private String year;
        private String month;
        private String day;
        private String week;
        private String costumer;
        private String area;
        private String hours;
        private boolean isRoute;
        private String workDescription;
        private boolean changedByAdmin;

    public TimeReport(){
        super();
    }

    public TimeReport(String reportReporterId, String reportReporterName, String reportTimeStamp, String reportDriverId, String reportDriverName,
                          String year, String month, String day, String week, String costumer, String area,
                          String hours, boolean isRoute, String workDescription, boolean changedByAdmin) {
        super(reportReporterId, reportReporterName, reportTimeStamp, reportDriverName);

        this.year = year;
        this.month = month;
        this.day = day;
        this.week = week;
        this.costumer = costumer;
        this.area = area;
        this.hours = hours;
        this.isRoute = isRoute;
        this.workDescription = workDescription;
        this.changedByAdmin = changedByAdmin;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getCostumer() {
        return costumer;
    }

    public void setCostumer(String costumer) {
        this.costumer = costumer;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public boolean isRoute() {
        return isRoute;
    }

    public void setRoute(boolean route) {
        isRoute = route;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public boolean isChangedByAdmin() {
        return changedByAdmin;
    }

    public void setChangedByAdmin(boolean changedByAdmin) {
        this.changedByAdmin = changedByAdmin;
    }

    @Override
    public String toString() {
        return "TimeReportTest{}";
    }
}
