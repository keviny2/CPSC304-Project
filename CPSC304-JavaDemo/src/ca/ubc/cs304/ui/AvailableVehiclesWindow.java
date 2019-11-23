package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.AvailableVehiclesDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ca.ubc.cs304.ui.utils.VehicleTypeNames;

public class AvailableVehiclesWindow extends JFrame implements ActionListener {

    private JComboBox vehicleTypeComboBox;
    private JTextField locationField;
    private JTextField fromDateTimeField;
    private JTextField toDateTimeField;
    private JButton NOACNumber;
    private VehicleTypeNames vtNames = new VehicleTypeNames();

    private AvailableVehiclesDelegate delegate;

    public AvailableVehiclesWindow() {
        super("Find Available Vehicles");
    }

    public void showFrame(AvailableVehiclesDelegate delegate) {
        this.delegate = delegate;

        // declare all labels here
        JLabel vehicleTypeLabel = new JLabel("Vehicle Type: ");
        JLabel locationLabel = new JLabel("Location: ");
        JLabel fromDateTimeLabel = new JLabel("From date & time: ");
        JLabel toDateTimeLabel = new JLabel("To date & time: ");
        // label for number of available vehicles
        JLabel NOACLabel = new JLabel("Available vehicles: ");
        JLabel clickNOACLabel = new JLabel("Click the above number for detail");
        clickNOACLabel.setFont(new Font(NOACLabel.getFont().getFontName(), Font.ITALIC, (int)(NOACLabel.getFont().getSize()*0.8)));
        clickNOACLabel.setForeground(Color.DARK_GRAY);
        JLabel dateFormatLabel = new JLabel(" yyyy-mm-dd ");
        dateFormatLabel.setFont(new Font(NOACLabel.getFont().getFontName(), Font.ITALIC, (int)(NOACLabel.getFont().getSize()*0.8)));
        dateFormatLabel.setForeground(Color.DARK_GRAY);

        // declares all Swing input objects here
        vehicleTypeComboBox = new JComboBox(vtNames.getNames());
        vehicleTypeComboBox.setSelectedItem(null);
        locationField = new JTextField(10);
        fromDateTimeField = new JTextField(10);
        toDateTimeField = new JTextField(10);
        NOACNumber = new JButton("0");

        JButton findButton = new JButton("Find");

        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // set the grid specs for all labels and objects
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(NOACLabel, c);
        contentPane.add(NOACLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(NOACNumber, c);
        contentPane.add(NOACNumber);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 10, 20, 0);
        gb.setConstraints(clickNOACLabel, c);
        contentPane.add(clickNOACLabel);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(15, 10, 10, 0);
        c.anchor = GridBagConstraints.WEST;
        gb.setConstraints(vehicleTypeLabel, c);
        contentPane.add(vehicleTypeLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(15, 0, 10, 0);
        gb.setConstraints(vehicleTypeComboBox, c);
        contentPane.add(vehicleTypeComboBox);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(locationLabel, c);
        contentPane.add(locationLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(locationField, c);
        contentPane.add(locationField);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(fromDateTimeLabel, c);
        contentPane.add(fromDateTimeLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(fromDateTimeField, c);
        contentPane.add(fromDateTimeField);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 160, 0, 0);
        gb.setConstraints(dateFormatLabel, c);
        contentPane.add(dateFormatLabel);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(toDateTimeLabel, c);
        contentPane.add(toDateTimeLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(toDateTimeField, c);
        contentPane.add(toDateTimeField);

        // place the find button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(findButton, c);
        contentPane.add(findButton);

        // register objects with action event handler
        findButton.setActionCommand("find");
        findButton.addActionListener(this);
        NOACNumber.setActionCommand("details");
        NOACNumber.addActionListener(this);

        // size the window to obtain a best fit for the components
        this.pack();

        // center the frame
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

        // make the window visible
        this.setVisible(true);

        // place the cursor in the text field for the username
        vehicleTypeComboBox.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("find")) {
            NOACNumber.setText(delegate.find((String) vehicleTypeComboBox.getSelectedItem(), locationField.getText(), fromDateTimeField.getText(), toDateTimeField.getText()));
        } else if (e.getActionCommand().equals("details")) {
            delegate.details((String) vehicleTypeComboBox.getSelectedItem(), locationField.getText(), fromDateTimeField.getText(), toDateTimeField.getText());
        }
    }
}
