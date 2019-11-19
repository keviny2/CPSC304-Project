package ca.ubc.cs304.model;

import java.util.Date;

/**
 * The intent for this class is to update/store information about a single branch
 */
public class Rent {
    private final int rid;
    private final String vlicense;
    private final String dlicense;
    private final Date date;
    private final Date fromDateTime;
    private final Date toDateTime;
    private final int odometer;
    private final String cardNo;
    private final int confNo;

    public Rent(int rid, String vlicense, String dlicense, Date date, Date fromDateTime, Date toDateTime, int odometer, String cardNo, int confNo) {
        this.rid = rid;
        this.vlicense = vlicense;
        this.dlicense = dlicense;
        this.date = date;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.odometer = odometer;
        this.cardNo = cardNo;
        this.confNo = confNo;
    }

    public int getRid() {
        return rid;
    }

    public String getVlicense() {
        return vlicense;
    }

    public String getDlicense() {
        return dlicense;
    }

    public Date getDate() {
        return date;
    }

    public Date getFromDateTime() {
        return fromDateTime;
    }

    public Date getToDateTime() {
        return toDateTime;
    }

    public int getOdometer() {
        return odometer;
    }

    public String getCardNo() {
        return cardNo;
    }

    public int getConfNo() {
        return confNo;
    }
}
