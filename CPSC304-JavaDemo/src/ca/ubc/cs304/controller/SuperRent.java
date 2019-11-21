package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;

public class SuperRent {
    private static final DatabaseConnectionHandler dbHandler = new DatabaseConnectionHandler();
    /**
     * Main method called at launch time
     */
    public static void main(String args[]) {
        LogInORACLE login = new LogInORACLE(dbHandler);
        login.start();
        AvailableVehicles availableVehicles = new AvailableVehicles(dbHandler);
        availableVehicles.start();
        SignUp signUp = new SignUp(dbHandler);
        signUp.start();
        MakeReservation makeReservation = new MakeReservation(dbHandler);
        makeReservation.start();
        RentingVehicle rentingVehicle = new RentingVehicle(dbHandler);
        rentingVehicle.start();
        ReturningVehicle returningVehicle = new ReturningVehicle(dbHandler);
        returningVehicle.start();
        login.requestFocus();
    }

}
