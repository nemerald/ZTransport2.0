package com.a0.ztransport2.robinwilde.ztransport2.Objects;

public class PalletReport extends Report{

    private String fromPlace;
    private String toPlace;
    private String noOfpallets;

    public PalletReport(){
        super();
    }

    public PalletReport(String reportReporterId, String reportReporterName, String reportTimeStamp,
                        String reportDriverName, String fromPlace, String toPlace, String noOfpallets) {
        super(reportReporterId, reportReporterName, reportTimeStamp, reportDriverName);

        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.noOfpallets = noOfpallets;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public String getNoOfpallets() {
        return noOfpallets;
    }

    public void setNoOfpallets(String noOfpallets) {
        this.noOfpallets = noOfpallets;
    }

    @Override
    public String toString() {
        return "PalletReportTest{}";
    }
}
