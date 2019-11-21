package ca.ubc.cs304.delegates;

public interface ReturningVehicleDelegate {
    String[] returnVehicle(String dateTimeReturned, int odometerReading, boolean isTankFull);
}
