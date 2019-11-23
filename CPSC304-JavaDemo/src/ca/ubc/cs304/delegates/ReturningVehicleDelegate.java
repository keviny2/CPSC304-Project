package ca.ubc.cs304.delegates;
import java.util.ArrayList;

public interface ReturningVehicleDelegate {
    ArrayList<String> returnVehicle(String vlicense, String dlicense, String dateTimeReturned, int odometerReading, boolean isTankFull);
}
