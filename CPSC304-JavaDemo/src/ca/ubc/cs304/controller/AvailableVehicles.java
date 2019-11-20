package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.AvailableVehiclesDelegate;
import ca.ubc.cs304.ui.AvailableVehiclesWindow;

import javax.swing.*;

/**
 * This is the main controller class that will orchestrate everything.
 */
public class AvailableVehicles implements AvailableVehiclesDelegate {
	private DatabaseConnectionHandler dbHandler = null;
	private AvailableVehiclesWindow availableVehiclesWindow = null;

	public AvailableVehicles() {
		dbHandler = new DatabaseConnectionHandler();
	}
	
	public void start() {
		availableVehiclesWindow = new AvailableVehiclesWindow();
		availableVehiclesWindow.setResizable(false);
		availableVehiclesWindow.showFrame(this);
		availableVehiclesWindow.getContentPane().getComponent(1).setName("10");
	}

	public String find(String vehicleType, String location, String fromDate, String toDate) {
		// show number of vehicles of above spec
		// if all empty/null, then display # of all available vehicles
		return "10";
	}

	public void details(String vehicleType, String location, String fromDate, String toDate) {
		// show frame of another window with a list of vehicles under above spec
		// basically, query it, make a list, then show it

		// create a new window named 'Available Vehicles Details'
		JFrame window = new JFrame();
		window.setTitle("Available Vehicles Details");
		window.setLocationRelativeTo(availableVehiclesWindow);

		// replace the data array below with sql queries
		String[] columnNames = { "Make", "Model", "Year", "Color", "Gas Type", "Vehicle Type" };
		String[][] data = { { "Honda", "Civic", "2018", "Blue", "Regular", "Standard" }, { "Honda", "CRV", "2012", "Black", "Regular", "SUV" } };

		// create new table with above data
		JTable table = new JTable(data, columnNames);
		table.getTableHeader().setReorderingAllowed(false);

		// set the window to be scrollable and set some preferences
		JScrollPane sp = new JScrollPane(table);
		window.add(sp);
		window.setSize(500,300);
		window.setVisible(true);
		window.requestFocusInWindow();
	}
}
