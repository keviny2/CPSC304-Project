package ca.ubc.cs304.model;

import java.util.Date;

public class Cards {
    private final String cardNo;
    private final Date expDate;
    private final String cardName;

    public Cards(String cardNo, Date expDate, String cardName) {
        this.cardNo = cardNo;
        this.expDate = expDate;
        this.cardName = cardName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public Date getExpDate() {
        return expDate;
    }

    public String getCardName() {
        return cardName;
    }
}
