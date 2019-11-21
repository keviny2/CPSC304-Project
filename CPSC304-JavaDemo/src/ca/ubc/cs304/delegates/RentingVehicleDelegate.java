package ca.ubc.cs304.delegates;

public interface RentingVehicleDelegate {
    boolean rentVehicle(String location, String vehicleType, String fromDateTime, String toDateTime, String fullName, String dlNumber, String cardNumber, String cardExpDate);

    boolean rentReservedVehicle(String confirmation, String dlNumber, String fromDate, String toDate, String cardNumber, String cardExpDate);
}
