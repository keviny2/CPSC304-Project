package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.SignUpDelegate;
import ca.ubc.cs304.ui.SignUpWindow;

public class SignUp implements SignUpDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private SignUpWindow signUpWindow = null;

    public SignUp(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public void start() {
        signUpWindow = new SignUpWindow();
        signUpWindow.setResizable(false);
        signUpWindow.showFrame(this);
    }

   public void signUp(String fullName, String address, long DLNumber) {

   }
}
