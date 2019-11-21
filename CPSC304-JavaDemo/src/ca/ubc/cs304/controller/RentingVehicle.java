package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.RentingVehicleDelegate;
import ca.ubc.cs304.ui.RentingVehicleWindow;
import java.sql.Date;
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

    public void rentVehicle(String location, String vehicleType, String fromDateTime, String toDateTime, String fullName, String dlNumber) {
        Date fromDate = Date.valueOf(fromDateTime);
        Date toDate = Date.valueOf(toDateTime);
        try {
            Integer confNo = dbHandler.getReservation(dlNumber);
            if (confNo != 0) {
                rentReservedVehicle(confNo, dlNumber, fromDate, toDate);
            } else {
                dbHandler.doRentalNoReservation(location, vehicleType, fromDate, toDate, fullName, dlNumber);
            }
        } catch (SQLException e) {
            // TODO: display error window with e.message
        }
    }

    public void rentReservedVehicle(String confirmation, String dlNumber, Date fromDate, Date toDate) {
        dbHandler.doRentalWithReservation(confirmation, dlNumber, fromDate, toDate);
    }
}
