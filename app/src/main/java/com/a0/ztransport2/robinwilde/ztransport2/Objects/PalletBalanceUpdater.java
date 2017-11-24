package com.a0.ztransport2.robinwilde.ztransport2.Objects;

public class PalletBalanceUpdater {

    private String inpuTimeStamp;
    private String jblBalance;
    private String hedeBalance;
    private String fashionServiceBalance;
    private boolean adminUpdate;

    public PalletBalanceUpdater(String inpuTimeStamp, String jblBalance, String hedeBalance, String fashionServiceBalance, boolean adminUpdate) {
        this.inpuTimeStamp = inpuTimeStamp;
        this.jblBalance = jblBalance;
        this.hedeBalance = hedeBalance;
        this.fashionServiceBalance = fashionServiceBalance;
        this.adminUpdate = adminUpdate;
    }

    public String getInpuTimeStamp() {
        return inpuTimeStamp;
    }

    public String getJblBalance() {
        return jblBalance;
    }

    public String getHedeBalance() {
        return hedeBalance;
    }

    public String getFashionServiceBalance() {
        return fashionServiceBalance;
    }

    public boolean getAdminUpdate() {
        return adminUpdate;
    }

    public void setInpuTimeStamp(String inpuTimeStamp) {
        this.inpuTimeStamp = inpuTimeStamp;
    }

    public void setJblBalance(String jblBalance) {
        this.jblBalance = jblBalance;
    }

    public void setHedeBalance(String hedeBalance) {
        this.hedeBalance = hedeBalance;
    }

    public void setFashionServiceBalance(String fashionServiceBalance) {
        this.fashionServiceBalance = fashionServiceBalance;
    }

    public void setAdminUpdate(boolean adminUpdate) {
        this.adminUpdate = adminUpdate;
    }

    @Override
    public String toString() {
        return "PalletBalanceUpdater{}";
    }
}
