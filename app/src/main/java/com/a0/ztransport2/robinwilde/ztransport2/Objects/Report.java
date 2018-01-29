package com.a0.ztransport2.robinwilde.ztransport2.Objects;

import java.util.UUID;

public abstract class Report {
    private String reportId = UUID.randomUUID().toString();;
    private String reportReporterId;
    private String reportReporterName;
    private String reportTimeStamp;
    private String reportDriverName;

    public Report(String reportReporterId, String reportReporterName, String reportTimeStamp, String reportDriverName){
        this.reportReporterId = reportReporterId;
        this.reportReporterName = reportReporterName;
        this.reportTimeStamp = reportTimeStamp;
        this.reportDriverName = reportDriverName;
    }

    public Report() {

    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportReporterId() {
        return reportReporterId;
    }

    public void setReportReporterId(String reportReporterId) {
        this.reportReporterId = reportReporterId;
    }

    public String getReportReporterName() {
        return reportReporterName;
    }

    public void setReportReporterName(String reportReporterName) {
        this.reportReporterName = reportReporterName;
    }

    public String getReportTimeStamp() {
        return reportTimeStamp;
    }

    public void setReportTimeStamp(String reportTimeStamp) {
        this.reportTimeStamp = reportTimeStamp;
    }

    public String getReportDriverName() {
        return reportDriverName;
    }

    public void setReportDriverName(String reportDriverName) {
        this.reportDriverName = reportDriverName;
    }

    @Override
    public String toString() {
        return "Report{}";
    }
}
