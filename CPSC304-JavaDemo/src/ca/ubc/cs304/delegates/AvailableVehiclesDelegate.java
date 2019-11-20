package ca.ubc.cs304.delegates;

public interface AvailableVehiclesDelegate {
    String find (String vehicleType, String location, String fromDate, String toDate);

    void details (String vehicleType, String location, String fromDate, String toDate);
}
