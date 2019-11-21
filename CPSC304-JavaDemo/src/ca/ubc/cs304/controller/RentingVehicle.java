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

    public void rentVehicle(String location, String vehicleType, String fromDateTime, String toDateTime, String fullName, String dlNumber, String cardNumber, String cardExpDate) {
        try {
            String confNo = dbHandler.getReservation(dlNumber);
            if (confNo.equals("")) {
                rentReservedVehicle(confNo, dlNumber, fromDateTime, toDateTime, cardNumber, cardExpDate);
            } else {
                dbHandler.doRentalNoReservation(location, vehicleType, fromDateTime, toDateTime, fullName, dlNumber);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void rentReservedVehicle(String confirmation, String dlNumber, String fromDate, String toDate, String cardNumber, String cardExpDate) {
        try {
            dbHandler.doRentalWithReservation(confirmation, dlNumber);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
