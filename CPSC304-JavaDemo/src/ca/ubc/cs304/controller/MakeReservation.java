package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.MakeReservationDelegate;
import ca.ubc.cs304.ui.MakeReservationWindow;

public class MakeReservation implements MakeReservationDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private MakeReservationWindow makeReservationWindow = null;

    public MakeReservation() {
        dbHandler = new DatabaseConnectionHandler();
    }

    public void start() {
        makeReservationWindow = new MakeReservationWindow();
        makeReservationWindow.setResizable(false);
        makeReservationWindow.showFrame(this);
    }

    public int reserve(String location, String vehicleType, String fromDateTime, String toDateTime, String customerName, long customerDL) {
        // if reservation didn't go through return -1
        return 12345678;
    }
}
