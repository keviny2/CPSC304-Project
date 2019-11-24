package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.AvailableVehiclesDelegate;
import ca.ubc.cs304.ui.AvailableVehiclesWindow;

import javax.swing.*;
import java.util.ArrayList;

/**
 * This is the main controller class that will orchestrate everything.
 */
public class AvailableVehicles implements AvailableVehiclesDelegate {
	private DatabaseConnectionHandler dbHandler;
	private AvailableVehiclesWindow availableVehiclesWindow = null;

	public AvailableVehicles(DatabaseConnectionHandler dbHandler) {
		this.dbHandler = dbHandler;
	}
	
	public void start() {
		availableVehiclesWindow = new AvailableVehiclesWindow();
		availableVehiclesWindow.setResizable(false);
		availableVehiclesWindow.showFrame(this);
	}

	public String find(String vehicleType, String location, String fromDate, String toDate) {
		// show number of vehicles of above spec
		// if all empty/null, then display # of all available vehicles
		ArrayList<String> criteria = new ArrayList<>();
		if (vehicleType != null && !vehicleType.trim().equals("")) {
			criteria.add("vtname = \'" + vehicleType + "\'");
		}
		if (location != null && !location.trim().equals("")) {
			criteria.add("location = \'" + location + "\'");
		}
		String result = dbHandler.findVehicles(criteria);
		if (result == "") {
			JOptionPane.showMessageDialog(new JFrame(), "Invalid query", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return result;
	}

	public void details(String vehicleType, String location, String fromDate, String toDate) {
		// show frame of another window with a list of vehicles under above spec
		// basically, query it, make a list, then show it

		// create a new window named 'Available Vehicles Details'
		JFrame window = new JFrame();
		window.setTitle("Available Vehicles Details");
		window.setLocationRelativeTo(availableVehiclesWindow);

		// replace the data array below with sql queries
		String[] columnNames = { "Make", "Model", "Year", "Color", "Gas Type", "Vehicle Type", "Location", "City"};
		ArrayList<String> criteria = new ArrayList<>();
		if (vehicleType != null && !vehicleType.trim().equals("")) {
			criteria.add("vtname = \'" + vehicleType + "\'");
		}
		if (location != null && !location.trim().equals("")) {
			criteria.add("location = \'" + location + "\'");
		}
		String[][] data = dbHandler.getVehicles(criteria);
		if (data.length == 0 || data[0].length == 0) {
			JOptionPane.showMessageDialog(new JFrame(), "No vehicles to show.", "Error", JOptionPane.ERROR_MESSAGE);
		} else if (data[0][0] == "") {
			JOptionPane.showMessageDialog(new JFrame(), "Invalid query.", "Error", JOptionPane.ERROR_MESSAGE);
		}

		// create new table with above data
		JTable table = new JTable(data, columnNames);
		table.getTableHeader().setReorderingAllowed(false);

		// set the window to be scrollable and set some preferences
		JScrollPane sp = new JScrollPane(table);
		window.add(sp);
		window.setSize(650,300);
		window.setVisible(true);
		window.requestFocusInWindow();
	}
}
