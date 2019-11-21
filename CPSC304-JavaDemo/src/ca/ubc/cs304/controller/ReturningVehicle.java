package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.ReturningVehicleDelegate;
import ca.ubc.cs304.ui.ReturningVehicleWindow;

import javax.swing.*;
import java.sql.SQLException;
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

    // String[0] = confNo, String[1] =
    // TODO: @Ryan put a vlicense parameter
    public String[] returnVehicle(String dateTimeReturned, int odometerReading, boolean isTankFull) {
        String vlicense = "1";
        try {
            dbHandler.isRented(vlicense);
            ArrayList<String> retVal = dbHandler.getValue(vlicense, dateTimeReturned);
            if(retVal.size() < 1){
                throw new SQLException("Error computing value");
            }
            int value = Integer.parseInt(retVal.get(0));
            String howCalculate = retVal.get(1);
            dbHandler.returnVehicle(dateTimeReturned, odometerReading, isTankFull, value);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println(odometerReading);
        System.out.println(isTankFull);
        return new String[0];
    }
}
