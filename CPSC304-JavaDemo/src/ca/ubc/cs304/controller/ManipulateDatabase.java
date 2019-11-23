package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.ManipulateDatabaseDelegate;
import ca.ubc.cs304.model.ColumnData;
import ca.ubc.cs304.ui.ManipulateDatabaseWindow;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManipulateDatabase implements ManipulateDatabaseDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private ManipulateDatabaseWindow manipulateDatabaseWindow = null;

    public ManipulateDatabase(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public void start() {
        manipulateDatabaseWindow = new ManipulateDatabaseWindow();
        manipulateDatabaseWindow.setResizable(false);
        manipulateDatabaseWindow.showFrame(this);
    }

    public void tableList() {
        try {
            ArrayList<String> tableNames = dbHandler.getTableNames();

            String[] columnNames = { "Table Names" };
            String[][] tnArray = new String[tableNames.size()][1];

            for (int i = 0; i < tableNames.size(); i++) {
                tnArray[i][0] = tableNames.get(i);
            }

            JFrame window = new JFrame();
            window.setTitle("All Table Names");
            window.setLocationRelativeTo(manipulateDatabaseWindow);

            // create new table with above data
            JTable table = new JTable(tnArray, columnNames);
            table.getTableHeader().setReorderingAllowed(false);

            // set the window to be scrollable and set some preferences
            JScrollPane sp = new JScrollPane(table);
            window.add(sp);
            window.setSize(200,250);
            window.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void viewTable(String tableName) {
        try {
            ColumnData columnData = dbHandler.viewTable(tableName);

            JFrame window = new JFrame();
            window.setTitle("Table: " + tableName);
            window.setLocationRelativeTo(manipulateDatabaseWindow);

            // create new table with above data
            JTable table = new JTable(columnData.data, columnData.columns);
            table.getTableHeader().setReorderingAllowed(false);

            // set the window to be scrollable and set some preferences
            JScrollPane sp = new JScrollPane(table);
            window.add(sp);
            window.setSize(650,650);
            window.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void insertIntoTable(String tableName, String values) {
        try {
            if (dbHandler.insertDataIntoTable(tableName, values))
                JOptionPane.showMessageDialog(new JFrame(), "Insert into " + tableName + " successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
        JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }

    public void deleteFromTable(String tableName, String conditions) {
        try {
            if (dbHandler.deleteDataFromTable(tableName, conditions))
                JOptionPane.showMessageDialog(new JFrame(), "Delete from " + tableName + " successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
