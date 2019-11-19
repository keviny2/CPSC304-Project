package ca.ubc.cs304.model;

import java.util.Date;

public class Return {
    private final int rid;
    private final Date dateTime;
    private final int odometer;
    private final boolean fullTank;
    private final int value;

    public Return(int rid, Date dateTime, int odometer, boolean fullTank, int value) {
        this.rid = rid;
        this.dateTime = dateTime;
        this.odometer = odometer;
        this.fullTank = fullTank;
        this.value = value;
    }

    public int getRid() {
        return rid;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public int getOdometer() {
        return odometer;
    }

    public boolean isFullTank() {
        return fullTank;
    }

    public int getValue() {
        return value;
    }
}
