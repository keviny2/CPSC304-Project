package ca.ubc.cs304.delegates;

public interface ReportsDelegate {
    void dailyRentals();

    void dailyRentalsBranch(String city, String location);

    void dailyReturns();

    void dailyReturnsBranch(String city, String location);
}
