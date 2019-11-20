package ca.ubc.cs304.delegates;

import javax.swing.*;
import java.awt.event.ActionListener;

public interface AvailableVehiclesDelegate {
    String find (String vehicleType, String location, String fromDate, String toDate);

    void details (String vehicleType, String location, String fromDate, String toDate);
}
