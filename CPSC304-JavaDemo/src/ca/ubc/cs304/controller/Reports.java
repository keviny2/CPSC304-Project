package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.ReportsDelegate;
import ca.ubc.cs304.ui.ReportsWindow;
import oracle.sql.DATE;

public class Reports implements ReportsDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private ReportsWindow reportsWindow = null;

    public Reports(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public void start() {
        reportsWindow = new ReportsWindow();
        reportsWindow.setResizable(false);
        reportsWindow.showFrame(this);
    }

    public void dailyRentals() {
        DATE today = new DATE();

    }

    public void dailyRentalsBranch(String city, String location) {

    }

    public void dailyReturns() {

    }

    public void dailyReturnsBranch(String city, String location) {

    }
}
