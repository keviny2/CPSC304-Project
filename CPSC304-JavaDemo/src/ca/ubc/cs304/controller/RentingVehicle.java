package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.RentingVehicleDelegate;
import ca.ubc.cs304.ui.RentingVehicleWindow;

import javax.swing.*;
import java.sql.SQLException;

public class RentingVehicle implements RentingVehicleDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private RentingVehicleWindow rentingVehicleWindow = null;

    public RentingVehicle(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public void start() {
        rentingVehicleWindow = new RentingVehicleWindow();
        rentingVehicleWindow.setResizable(false);
        rentingVehicleWindow.showFrame(this);
    }

    public boolean rentVehicle(String location, String vehicleType, String fromDateTime, String toDateTime, String fullName, String dlNumber, String cardNumber, String cardExpDate) {
        try {
            dbHandler.doRentalNoReservation(location, vehicleType, fromDateTime, toDateTime, fullName, dlNumber, cardNumber);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean rentReservedVehicle(String confirmation, String dlNumber, String fromDate, String toDate, String cardNumber, String cardExpDate) {
        try {
            dbHandler.doRentalWithReservation(confirmation, dlNumber, cardNumber);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
