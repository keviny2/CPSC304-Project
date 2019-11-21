package ca.ubc.cs304.delegates;

import java.sql.Date;
import java.sql.SQLException;

public interface RentingVehicleDelegate {
    void rentVehicle(String location, String vehicleType, String fromDateTime, String toDateTime, String fullName, String dlNumber);

    void rentReservedVehicle(String confirmation, String dlNumber);
}
