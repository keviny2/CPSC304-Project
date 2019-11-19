package ca.ubc.cs304.delegates;

import javax.swing.*;
import java.awt.event.ActionListener;

public interface AvailableVehiclesDelegate {
    void find (String carType, String location, int timeInterval);
}
