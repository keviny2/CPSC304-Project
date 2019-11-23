package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.ReportsDelegate;
import ca.ubc.cs304.model.ColumnData;
import ca.ubc.cs304.ui.ReportsWindow;
import oracle.sql.DATE;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

            if(ret[0].data.length + ret[1].data.length == 0)
                throw new SQLException("No results!");
            // create new table with above data
            JFrame window1 = new JFrame();
            window1.setTitle("# Vehicles Rented Today By Vehicle Type");
            JTable table1 = new JTable(ret[0].data, ret[0].columns);
            table1.getTableHeader().setReorderingAllowed(false);

            // set the window to be scrollable and set some preferences
            JScrollPane sp1 = new JScrollPane(table1);
            window1.add(sp1);
            window1.setSize(300,200);

            JFrame window2 = new JFrame();
            window2.setTitle("Info on Vehicles Rented Today By Branch");
            JTable table2 = new JTable(ret[1].data, ret[1].columns);
            table2.getTableHeader().setReorderingAllowed(false);

            // set the window to be scrollable and set some preferences
            JScrollPane sp2 = new JScrollPane(table2);
            window2.add(sp2);
            window2.setSize(600,300);

            JFrame window3 = new JFrame();
            window3.setTitle("Total # Vehicles Rented Today Across Company");
            JTable table3 = new JTable(ret[2].data, ret[2].columns);
            table3.getTableHeader().setReorderingAllowed(false);

            // set the window to be scrollable and set some preferences
            JScrollPane sp3 = new JScrollPane(table3);
            window3.add(sp3);
            window3.setSize(250,100);

            window2.setLocationRelativeTo(reportsWindow);
            window1.setLocationRelativeTo(window2);
            window3.setLocationRelativeTo(window1);
            window2.setVisible(true);
            window1.setVisible(true);
            window3.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void dailyRentalsBranch(String city, String location) {
        try {
            // ret 1 = Vehicle Per Category, 2 = Rentals at each branch, 3 = total new rentals
            // ColumnData.columns is non-empty, ColumnData.data may be empty if there is no data.
            ColumnData[] ret = dbHandler.generateDailyRentalReportByBranch(city, location);

            if(ret[0].data.length + ret[1].data.length == 0)
                throw new SQLException("No results!");
            // create new table with above data
            JFrame window1 = new JFrame();
            window1.setTitle("# Vehicles Rented Today By Vehicle Type From Branch");
            JTable table1 = new JTable(ret[0].data, ret[0].columns);
            table1.getTableHeader().setReorderingAllowed(false);

            // set the window to be scrollable and set some preferences
            JScrollPane sp1 = new JScrollPane(table1);
            window1.add(sp1);
            window1.setSize(300,200);

            JFrame window2 = new JFrame();
            window2.setTitle("Info on Vehicles Rented Today From Branch");
            JTable table2 = new JTable(ret[1].data, ret[1].columns);
            table2.getTableHeader().setReorderingAllowed(false);

            // set the window to be scrollable and set some preferences
            JScrollPane sp2 = new JScrollPane(table2);
            window2.add(sp2);
            window2.setSize(600,300);

            JFrame window3 = new JFrame();
            window3.setTitle("Total # Vehicles Rented Today From Branch");
            JTable table3 = new JTable(ret[2].data, ret[2].columns);
            table3.getTableHeader().setReorderingAllowed(false);

            // set the window to be scrollable and set some preferences
            JScrollPane sp3 = new JScrollPane(table3);
            window3.add(sp3);
            window3.setSize(250,100);

            window2.setLocationRelativeTo(reportsWindow);
            window1.setLocationRelativeTo(window2);
            window3.setLocationRelativeTo(window1);
            window2.setVisible(true);
            window1.setVisible(true);
            window3.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void dailyReturns() {
        try {
            ColumnData[] ret = dbHandler.generateDailyReturnsReport();

            if(ret[0].data.length + ret[1].data.length == 0)
                throw new SQLException("No results!");
            // create new table with above data
            JFrame window1 = new JFrame();
            window1.setTitle("# Vehicles Returned Today and Revenue By Vehicle Type");
            JTable table1 = new JTable(ret[0].data, ret[0].columns);
            table1.getTableHeader().setReorderingAllowed(false);

            // set the window to be scrollable and set some preferences
            JScrollPane sp1 = new JScrollPane(table1);
            window1.add(sp1);
            window1.setSize(400,200);

            JFrame window2 = new JFrame();
            window2.setTitle("Subtotals for # of Vehicles Returned Today and Revenue By Branch");
            JTable table2 = new JTable(ret[1].data, ret[1].columns);
            table2.getTableHeader().setReorderingAllowed(false);

            // set the window to be scrollable and set some preferences
            JScrollPane sp2 = new JScrollPane(table2);
            window2.add(sp2);
            window2.setSize(600,300);

            JFrame window3 = new JFrame();
            window3.setTitle("Grand Total Revenue Today");
            JTable table3 = new JTable(ret[2].data, ret[2].columns);
            table3.getTableHeader().setReorderingAllowed(false);

            // set the window to be scrollable and set some preferences
            JScrollPane sp3 = new JScrollPane(table3);
            window3.add(sp3);
            window3.setSize(400,100);

            window2.setLocationRelativeTo(reportsWindow);
            window1.setLocationRelativeTo(window2);
            window3.setLocationRelativeTo(window1);
            window2.setVisible(true);
            window1.setVisible(true);
            window3.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void dailyReturnsBranch(String city, String location) {
        try {
            ColumnData[] ret = dbHandler.generateDailyReturnsReportByBranch(city, location);

            if(ret[0].data.length + ret[1].data.length == 0)
                throw new SQLException("No results!");
            // create new table with above data
            JFrame window1 = new JFrame();
            window1.setTitle("# Vehicles Returned Today and Revenue By Vehicle Type From Branch");
            JTable table1 = new JTable(ret[0].data, ret[0].columns);
            table1.getTableHeader().setReorderingAllowed(false);

            // set the window to be scrollable and set some preferences
            JScrollPane sp1 = new JScrollPane(table1);
            window1.add(sp1);
            window1.setSize(500,150);

            JFrame window2 = new JFrame();
            window2.setTitle("Subtotals for # of Vehicles Returned Today and Revenue By Branch From Branch");
            JTable table2 = new JTable(ret[1].data, ret[1].columns);
            table2.getTableHeader().setReorderingAllowed(false);

            // set the window to be scrollable and set some preferences
            JScrollPane sp2 = new JScrollPane(table2);
            window2.add(sp2);
            window2.setSize(650,200);

            JFrame window3 = new JFrame();
            window3.setTitle("Grand Total Revenue Today From Branch");
            JTable table3 = new JTable(ret[2].data, ret[2].columns);
            table3.getTableHeader().setReorderingAllowed(false);

            // set the window to be scrollable and set some preferences
            JScrollPane sp3 = new JScrollPane(table3);
            window3.add(sp3);
            window3.setSize(500,100);

            window2.setLocationRelativeTo(reportsWindow);
            window1.setLocationRelativeTo(window2);
            window3.setLocationRelativeTo(window1);
            window2.setVisible(true);
            window1.setVisible(true);
            window3.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
