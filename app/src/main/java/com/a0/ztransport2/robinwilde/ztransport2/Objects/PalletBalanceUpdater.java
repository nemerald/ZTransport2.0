package com.a0.ztransport2.robinwilde.ztransport2.Objects;

public class PalletBalanceUpdater {

    private String inpuTimeStamp;
    private String jblBalance;
    private String hedeBalance;
    private String fashionServiceBalance;

    public PalletBalanceUpdater(String inpuTimeStamp, String jblBalance, String hedeBalance, String fashionServiceBalance) {
        this.inpuTimeStamp = inpuTimeStamp;
        this.jblBalance = jblBalance;
        this.hedeBalance = hedeBalance;
        this.fashionServiceBalance = fashionServiceBalance;
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

    @Override
    public String toString() {
        return "PalletBalanceUpdater{}";
    }
}
