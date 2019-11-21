package ca.ubc.cs304.delegates;

public interface RentingVehicleDelegate {
    void rentVehicle(String location, String vehicleType, String fromDateTime, String toDateTime, String fullName, String dlNumber);

    void rentReservedVehicle(int confirmation, String dlNumber);
}
