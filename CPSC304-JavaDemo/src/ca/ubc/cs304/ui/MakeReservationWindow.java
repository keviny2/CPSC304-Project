package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ca.ubc.cs304.delegates.MakeReservationDelegate;
import ca.ubc.cs304.ui.utils.VehicleTypeNames;

public class MakeReservationWindow extends JFrame implements ActionListener {

    private JTextField locationField;
    private JComboBox vehicleTypeComboBox;
    private JTextField fromDateTimeField;
    private JTextField toDateTimeField;
    private JTextField customerNameField;
    private JTextField customerDLField;
    private VehicleTypeNames vtNames = new VehicleTypeNames();

    private MakeReservationDelegate delegate;

    public MakeReservationWindow() {
        super("Make Reservations");
    }

    public void showFrame(MakeReservationDelegate delegate) {
        this.delegate = delegate;

        // declare all labels here
        JLabel reservationInfoLabel = new JLabel("Reservation Information: ");
        JLabel locationLabel = new JLabel("Location: ");
        JLabel vehicleTypeLabel = new JLabel("Vehicle Type: ");
        JLabel fromDateTimeLabel = new JLabel("From date & time: ");
        JLabel dateFormatLabel = new JLabel(" yyyy-mm-dd ");
        dateFormatLabel.setFont(new Font(fromDateTimeLabel.getFont().getFontName(), Font.ITALIC, (int)(fromDateTimeLabel.getFont().getSize()*0.8)));
        dateFormatLabel.setForeground(Color.DARK_GRAY);
        JLabel toDateTimeLabel = new JLabel("To date & time: ");
        // label for number of available vehicles
        JLabel customerInfoLabel = new JLabel("Customer Information: ");
        JLabel customerNameLabel = new JLabel("Full name: ");
        JLabel customerDLLabel = new JLabel("Driver's License #: ");

        // declares all Swing input objects here
        vehicleTypeComboBox = new JComboBox(vtNames.getNames());
        vehicleTypeComboBox.setSelectedItem(null);
        locationField = new JTextField(10);
        fromDateTimeField = new JTextField(10);
        toDateTimeField = new JTextField(10);
        customerNameField = new JTextField(10);
        customerDLField = new JTextField(10);

        JButton reserveButton = new JButton("Reserve");

        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(17, 10, 15, 0);
        c.anchor = GridBagConstraints.WEST;
        gb.setConstraints(reservationInfoLabel, c);
        contentPane.add(reservationInfoLabel);

        // set the grid specs for all labels and objects
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
        gb.setConstraints(vehicleTypeLabel, c);
        contentPane.add(vehicleTypeLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(vehicleTypeComboBox, c);
        contentPane.add(vehicleTypeComboBox);

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

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(20, 10, 15, 0);
        gb.setConstraints(customerInfoLabel, c);
        contentPane.add(customerInfoLabel);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(15, 10, 10, 0);
        gb.setConstraints(customerNameLabel, c);
        contentPane.add(customerNameLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(15, 0, 10, 0);
        gb.setConstraints(customerNameField, c);
        contentPane.add(customerNameField);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(customerDLLabel, c);
        contentPane.add(customerDLLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(customerDLField, c);
        contentPane.add(customerDLField);

        // place the find button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(reserveButton, c);
        contentPane.add(reserveButton);

        // register objects with action event handler
        reserveButton.setActionCommand("reserve");
        reserveButton.addActionListener(this);

//        // anonymous inner class for closing the window
//        this.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                System.exit(0);
//            }
//        });

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

    // TODO: Unsuccessful reservation shows success with confNo -1
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand().equals("reserve")) {
                if (!locationField.getText().trim().equals("")) {
                    if (vehicleTypeComboBox.getSelectedItem() != null) {
                        if (!fromDateTimeField.getText().trim().equals("")) {
                            if (!toDateTimeField.getText().trim().equals("")) {
                                if (!customerNameField.getText().trim().equals("")) {
                                    if (!customerDLField.getText().trim().equals("")) {
                                        // displays success message with conf num from what reserve func returns
                                        int confNum = delegate.reserve(locationField.getText(), (String) vehicleTypeComboBox.getSelectedItem(), fromDateTimeField.getText(), toDateTimeField.getText(), customerNameField.getText(), Long.parseLong(customerDLField.getText()));
                                        JOptionPane.showMessageDialog(new JFrame(), "You have reserved a vehicle!\n\nHere is your confirmation number: " + confNum, "Success", JOptionPane.INFORMATION_MESSAGE);
                                        // this.dispose();
                                    } else JOptionPane.showMessageDialog(new JFrame(), "Please enter your driver's license #", "Error", JOptionPane.ERROR_MESSAGE);
                                } else JOptionPane.showMessageDialog(new JFrame(), "Please enter your full name", "Error", JOptionPane.ERROR_MESSAGE);
                            } else JOptionPane.showMessageDialog(new JFrame(), "Please enter the date & time rental will end", "Error", JOptionPane.ERROR_MESSAGE);
                        } else JOptionPane.showMessageDialog(new JFrame(), "Please enter the date & time the rental will start", "Error", JOptionPane.ERROR_MESSAGE);
                    } else JOptionPane.showMessageDialog(new JFrame(), "Please select the vehicle type", "Error", JOptionPane.ERROR_MESSAGE);
                } else JOptionPane.showMessageDialog(new JFrame(), "Please enter the location", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(new JFrame(), "Please enter only numbers in the driver's license # field", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

