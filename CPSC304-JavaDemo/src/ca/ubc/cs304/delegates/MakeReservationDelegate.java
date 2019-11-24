package ca.ubc.cs304.delegates;

import java.util.ArrayList;

public interface MakeReservationDelegate {
    int reserve(String location, String vehicleType, String fromDateTime, String toDateTime, String customerName, long customerDL);
    ArrayList<String> getEstimation(String vtname, String fromDate, String toDate);
}
