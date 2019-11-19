package ca.ubc.cs304.model;

public class Status {
    private final int statusID;
    private final String status;

    public Status(int statusID, String status) {
        this.statusID = statusID;
        this.status = status;
    }

    public int getStatusID() {
        return statusID;
    }

    public String getStatus() {
        return status;
    }
}
