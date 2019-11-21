package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.RentingVehicleDelegate;
import ca.ubc.cs304.ui.AvailableVehiclesWindow;
import ca.ubc.cs304.ui.RentingVehicleWindow;

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

    }

    public void rentReservedVehicle(int confirmation, String dlNumber) {

    }
}
