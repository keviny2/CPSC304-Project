package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.ReturningVehicleDelegate;
import ca.ubc.cs304.ui.ReturningVehicleWindow;

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
    public String[] returnVehicle(String dateTimeReturned, int odometerReading, boolean isTankFull) {
        System.out.println(odometerReading);
        System.out.println(isTankFull);
        return new String[0];
    }
}
