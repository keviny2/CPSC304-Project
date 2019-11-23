package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.ReturningVehicleDelegate;
import ca.ubc.cs304.ui.ReturningVehicleWindow;

import javax.swing.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class ReturningVehicle implements ReturningVehicleDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private ReturningVehicleWindow returningVehicleWindow = null;

    public ReturningVehicle(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public void start() {
        returningVehicleWindow = new ReturningVehicleWindow();
        returningVehicleWindow.setResizable(false);
        returningVehicleWindow.showFrame(this);
    }

    // ArrayList = {rid, dateTimeReturned, value, howCalculate}
    public ArrayList<String> returnVehicle(String vlicense, String dlicense, String dateTimeReturned, int odometerReading, boolean isTankFull) {
        ArrayList<String> returnInfo = new ArrayList<>();
        try {
            dbHandler.isRented(vlicense); //Check if the vehicle is rented
            ArrayList<String> retVal = dbHandler.getRevenue(vlicense, dateTimeReturned); //retVal has value and howCalculated
            int value = Integer.parseInt(retVal.get(0));
            String howCalculate = retVal.get(1);
            int rid = dbHandler.getReturnId(vlicense);
            dbHandler.returnVehicle(rid, dateTimeReturned, odometerReading, isTankFull, value); //Insert into table
            dbHandler.updateVehicle(vlicense);
            returnInfo.add(0, Integer.toString(rid));
            returnInfo.add(1, Integer.toString(value));
            returnInfo.add(2, howCalculate);
        } catch (SQLException | ParseException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return returnInfo;
    }
}
