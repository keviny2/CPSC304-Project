package ca.ubc.cs304.model;

import java.util.Date;

public class Reservations {
    private final int confNo;
    private final String vtname;
    private final String dlicense;
    private final Date fromDateTime;
    private final Date toDateTime;

    public Reservations(int confNo, String vtname, String dlicense, Date fromDateTime, Date toDateTime) {
        this.confNo = confNo;
        this.vtname = vtname;
        this.dlicense = dlicense;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    public int getConfNo() {
        return confNo;
    }

    public String getVtname() {
        return vtname;
    }

    public String getDlicense() {
        return dlicense;
    }

    public Date getFromDateTime() {
        return fromDateTime;
    }

    public Date getToDateTime() {
        return toDateTime;
    }
}
