package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.ReportsDelegate;
import ca.ubc.cs304.model.ColumnData;
import ca.ubc.cs304.ui.ReportsWindow;
import oracle.sql.DATE;

import javax.swing.*;
import java.sql.SQLException;

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
        // ret 1 = Vehicle Per Category, 2 = Rentals at each branch, 3 = total new rentals
        // ColumnData.columns is non-empty, ColumnData.data may be empty if there is no data.
        try {
            ColumnData[] ret = dbHandler.generateDailyRentalReport();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void dailyRentalsBranch(String city, String location) {
        try {
            // ret 1 = Vehicle Per Category, 2 = Rentals at each branch, 3 = total new rentals
            // ColumnData.columns is non-empty, ColumnData.data may be empty if there is no data.
            ColumnData[] ret = dbHandler.generateDailyRentalReportByBranch(city, location);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void dailyReturns() {

    }

    public void dailyReturnsBranch(String city, String location) {

    }
}
