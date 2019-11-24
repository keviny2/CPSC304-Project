package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.MakeReservationDelegate;
import ca.ubc.cs304.ui.MakeReservationWindow;

import javax.swing.*;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

public class MakeReservation implements MakeReservationDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private MakeReservationWindow makeReservationWindow = null;

    public MakeReservation(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public void start() {
        makeReservationWindow = new MakeReservationWindow();
        makeReservationWindow.setResizable(false);
        makeReservationWindow.showFrame(this);
    }

    public int reserve(String location, String vehicleType, String fromDateTime, String toDateTime, String customerName, long customerDL) {
        // if reservation didn't go through return -1
        ArrayList<String> criteria = new ArrayList<>();
        criteria.add("vtname = \'" + vehicleType + "\'");
        criteria.add("location = \'" + location + "\'");

        String result = dbHandler.findVehicles(criteria);
        if (result.equals("")) {
            JOptionPane.showMessageDialog(new JFrame(), "Invalid query", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        } else if (result.equals("0")) {
            JOptionPane.showMessageDialog(new JFrame(), "No matching vehicles found", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        return dbHandler.reserveVehicle(location, vehicleType, fromDateTime, toDateTime, customerName, customerDL);
    }

    public ArrayList<String> getEstimation(String vtname, String fromDate, String toDate){
        ArrayList<String> result = new ArrayList<>();
        try{
            result = dbHandler.getEstimation(vtname, fromDate, toDate);
        } catch (SQLException | ParseException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Cannot get Estimation", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }
}
