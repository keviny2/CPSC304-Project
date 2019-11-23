package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.NavigationDelegate;
import ca.ubc.cs304.ui.NavigationWindow;

public class SuperRent {
    private static final DatabaseConnectionHandler dbHandler = new DatabaseConnectionHandler();
    /**
     * Main method called at launch time
     */
    public static void main(String args[]) {
       dbHandler.login();
       Navigation navigation = new Navigation(dbHandler);
       navigation.start();
    }
}
