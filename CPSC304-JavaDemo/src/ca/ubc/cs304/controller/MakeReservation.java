package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.MakeReservationDelegate;
import ca.ubc.cs304.ui.MakeReservationWindow;

import javax.swing.*;
import java.sql.Date;
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
        criteria.add("vtname = " + vehicleType);
        criteria.add("location = " + location);

        String result = dbHandler.findVehicles(criteria);
        if (result == "") {
            return -1;
        }
        Integer confNo = Objects.hash(location, vehicleType, fromDateTime, toDateTime, customerName, customerDL);
        Date fromDate = Date.valueOf(fromDateTime);
        Date toDate = Date.valueOf(toDateTime);
        return dbHandler.reserveVehicle(confNo, location, vehicleType, fromDate, toDate, customerName, customerDL);
    }
}
