package ca.ubc.cs304.delegates;

public interface MakeReservationDelegate {
    int reserve(String location, String vehicleType, String fromDateTime, String toDateTime, String customerName, long customerDL);
}
