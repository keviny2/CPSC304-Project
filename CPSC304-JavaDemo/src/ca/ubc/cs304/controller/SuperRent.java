package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.ui.LoginWindow;

public class SuperRent {
    /**
     * Main method called at launch time
     */
    public static void main(String args[]) {
        AvailableVehicles availableVehicles = new AvailableVehicles();
        SignUp signUp = new SignUp();
        availableVehicles.start();
        signUp.start();
        MakeReservation makeReservation = new MakeReservation();
        makeReservation.start();
    }

}
