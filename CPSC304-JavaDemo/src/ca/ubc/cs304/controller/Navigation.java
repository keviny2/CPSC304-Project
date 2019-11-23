package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.NavigationDelegate;
import ca.ubc.cs304.ui.NavigationWindow;

public class Navigation implements NavigationDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private NavigationWindow navigationWindow = null;

    public Navigation(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public void start() {
        navigationWindow = new NavigationWindow();
        navigationWindow.setResizable(false);
        navigationWindow.showFrame(this);
    }

    public void signUp() {
        SignUp signUp = new SignUp(dbHandler);
        signUp.start();
    }

    public void available() {
        AvailableVehicles availableVehicles = new AvailableVehicles(dbHandler);
        availableVehicles.start();
    }

    public void makeRes() {
        MakeReservation makeReservation = new MakeReservation(dbHandler);
        makeReservation.start();
    }

    public void renting() {
        RentingVehicle rentingVehicle = new RentingVehicle(dbHandler);
        rentingVehicle.start();
    }

    public void returning() {
        ReturningVehicle returningVehicle = new ReturningVehicle(dbHandler);
        returningVehicle.start();
    }

    public void reports() {
        Reports reports = new Reports(dbHandler);
        reports.start();
    }

    public void manip() {
        ManipulateDatabase manipulateDatabase = new ManipulateDatabase(dbHandler);
        manipulateDatabase.start();
    }
}
