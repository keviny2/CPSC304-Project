package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import ca.ubc.cs304.delegates.RentingVehicleDelegate;
import ca.ubc.cs304.ui.utils.VehicleTypeNames;

public class RentingVehicleWindow extends JFrame implements ActionListener {

    private JTextField locationField;
    private JComboBox vehicleTypeComboBox;
    private JTextField fromDateTimeField;
    private JTextField toDateTimeField;
    private JTextField customerNameField;
    private JTextField customerDLField;
    private VehicleTypeNames vtNames = new VehicleTypeNames();
    private JTextField cNumField;
    private JTextField CDLNField;
    private JTextField cardNumberField;
    private JTextField cardNumberField2;
    private JTextField cardExpDateField;
    private JTextField cardExpDateField2;
    private JFrame rentReservedWindow = new JFrame();

    private RentingVehicleDelegate delegate;

    public RentingVehicleWindow() {
        super("Rent Out a Vehicle");
    }

    public void showFrame(RentingVehicleDelegate delegate) {
        this.delegate = delegate;

        // declare all labels here
        JLabel reservationInfoLabel = new JLabel("New Rental: ");
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
        JLabel rentReservedLabel = new JLabel("Made a reservation? Click here: ");
        JLabel cardNumberLabel = new JLabel("Credit Card #: ");
        JLabel cardExpDateLabel = new JLabel("Expiry date (mm/yy): ");

        // declares all Swing input objects here
        vehicleTypeComboBox = new JComboBox(vtNames.getNames());
        vehicleTypeComboBox.setSelectedItem(null);
        locationField = new JTextField(10);
        fromDateTimeField = new JTextField(10);
        toDateTimeField = new JTextField(10);
        customerNameField = new JTextField(10);
        customerDLField = new JTextField(10);
        cardNumberField = new JTextField(10);
        cardExpDateField = new JTextField(10);

        JButton rentButton = new JButton("Rent");
        JButton rentReservedButton = new JButton("Rent Reserved");

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
        gb.setConstraints(rentReservedLabel, c);
        contentPane.add(rentReservedLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(rentReservedButton, c);
        contentPane.add(rentReservedButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(17, 10, 15, 0);
        c.anchor = GridBagConstraints.WEST;
        gb.setConstraints(reservationInfoLabel, c);
        contentPane.add(reservationInfoLabel);

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
        c.insets = new Insets(0, 235, 0, 0);
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

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(cardNumberLabel, c);
        contentPane.add(cardNumberLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(cardNumberField, c);
        contentPane.add(cardNumberField);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(cardExpDateLabel, c);
        contentPane.add(cardExpDateLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(cardExpDateField, c);
        contentPane.add(cardExpDateField);

        // place the find button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(rentButton, c);
        contentPane.add(rentButton);

        // register objects with action event handler
        rentButton.setActionCommand("rent");
        rentButton.addActionListener(this);
        rentReservedButton.setActionCommand("rentReserved");
        rentReservedButton.addActionListener(this);

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
            if (e.getActionCommand().equals("rent")) {
                if (!locationField.getText().trim().equals("")) {
                    if (vehicleTypeComboBox.getSelectedItem() != null) {
                        if (!fromDateTimeField.getText().trim().equals("")) {
                            if (!toDateTimeField.getText().trim().equals("")) {
                                if (!customerNameField.getText().trim().equals("")) {
                                    if (!customerDLField.getText().trim().equals("")) {
                                        // displays success message with conf num from what reserve func returns
                                        if (delegate.rentVehicle(locationField.getText(), (String) vehicleTypeComboBox.getSelectedItem(), fromDateTimeField.getText(), toDateTimeField.getText(), customerNameField.getText(), customerDLField.getText(), cardNumberField.getText(), cardNumberField.getText())) {
                                            JOptionPane.showMessageDialog(new JFrame(), "You have successfully rented a vehicle!\n\nReceipt:\n" + "Location: " + locationField.getText() +
                                                    "\nVehicle Type: " + vehicleTypeComboBox.getSelectedItem() +
                                                    "\nPick up date & time: " + fromDateTimeField.getText() +
                                                    "\nReturn date & time: " + toDateTimeField.getText(), "Success", JOptionPane.INFORMATION_MESSAGE);
                                        }
                                        this.dispose();
                                    } else JOptionPane.showMessageDialog(new JFrame(), "Please enter your driver's license #", "Error", JOptionPane.ERROR_MESSAGE);
                                } else JOptionPane.showMessageDialog(new JFrame(), "Please enter your full name", "Error", JOptionPane.ERROR_MESSAGE);
                            } else JOptionPane.showMessageDialog(new JFrame(), "Please enter the date & time rental will end", "Error", JOptionPane.ERROR_MESSAGE);
                        } else JOptionPane.showMessageDialog(new JFrame(), "Please enter the date & time the rental will start", "Error", JOptionPane.ERROR_MESSAGE);
                    } else JOptionPane.showMessageDialog(new JFrame(), "Please select the vehicle type", "Error", JOptionPane.ERROR_MESSAGE);
                } else JOptionPane.showMessageDialog(new JFrame(), "Please enter the location", "Error", JOptionPane.ERROR_MESSAGE);
            }
        if (e.getActionCommand().equals("rentReserved")) {
            rentReservedWindow.setSize(300, 150);
            rentReservedWindow.setTitle("Rent a Reserved Vehicle");

            JLabel CILabel = new JLabel("Customer Information:");
            JLabel cNumLabel = new JLabel("Confirmation #: ");
            JLabel CDLNLabel = new JLabel("Driver's license #: ");
            JLabel cardNumberLabel = new JLabel("Credit Card #: ");
            JLabel cardExpDateLabel = new JLabel("Expiry date (mm/yy): ");

            cNumField = new JTextField(10);
            CDLNField = new JTextField(10);
            cardExpDateField2 = new JTextField(10);
            cardNumberField2 = new JTextField(10);


            JButton rentReservedButton2 = new JButton("Rent");

            JPanel contentPane = new JPanel();
            rentReservedWindow.setContentPane(contentPane);

            // layout components using the GridBag layout manager
            GridBagLayout gb = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            contentPane.setLayout(gb);
            contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // set the grid specs for all labels and objects

            c.gridwidth = GridBagConstraints.REMAINDER;
            c.insets = new Insets(10, 10, 10, 0);
            c.anchor = GridBagConstraints.WEST;
            gb.setConstraints(CILabel, c);
            contentPane.add(CILabel);

            c.gridwidth = GridBagConstraints.RELATIVE;
            c.insets = new Insets(10, 10, 10, 0);
            gb.setConstraints(cNumLabel, c);
            contentPane.add(cNumLabel);

            c.gridwidth = GridBagConstraints.REMAINDER;
            c.insets = new Insets(10, 0, 10, 0);
            c.anchor = GridBagConstraints.WEST;
            gb.setConstraints(cNumField, c);
            contentPane.add(cNumField);

            c.gridwidth = GridBagConstraints.RELATIVE;
            c.insets = new Insets(10, 10, 10, 0);
            gb.setConstraints(CDLNLabel, c);
            contentPane.add(CDLNLabel);

            c.gridwidth = GridBagConstraints.REMAINDER;
            c.insets = new Insets(10, 0, 10, 0);
            gb.setConstraints(CDLNField, c);
            contentPane.add(CDLNField);

            c.gridwidth = GridBagConstraints.RELATIVE;
            c.insets = new Insets(10, 10, 10, 0);
            gb.setConstraints(cardNumberLabel, c);
            contentPane.add(cardNumberLabel);

            c.gridwidth = GridBagConstraints.REMAINDER;
            c.insets = new Insets(10, 0, 10, 0);
            gb.setConstraints(cardNumberField2, c);
            contentPane.add(cardNumberField2);

            c.gridwidth = GridBagConstraints.RELATIVE;
            c.insets = new Insets(10, 10, 10, 0);
            gb.setConstraints(cardExpDateLabel, c);
            contentPane.add(cardExpDateLabel);

            c.gridwidth = GridBagConstraints.REMAINDER;
            c.insets = new Insets(10, 0, 10, 0);
            gb.setConstraints(cardExpDateField2, c);
            contentPane.add(cardExpDateField2);

            c.gridwidth = GridBagConstraints.REMAINDER;
            c.insets = new Insets(10, 0, 10, 10);
            c.anchor = GridBagConstraints.CENTER;
            gb.setConstraints(rentReservedButton2, c);
            contentPane.add(rentReservedButton2);

            // register objects with action event handler
            rentReservedButton2.setActionCommand("rentReserved2");
            rentReservedButton2.addActionListener(this);

            // size the window to obtain a best fit for the components
            rentReservedWindow.pack();

            // center the frame
            Dimension d = rentReservedWindow.getToolkit().getScreenSize();
            Rectangle r = rentReservedWindow.getBounds();
            rentReservedWindow.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

            // make the window visible
            rentReservedWindow.setVisible(true);

            // place the cursor in the text field for the username
            cNumField.requestFocus();
            rentReservedWindow.setLocationRelativeTo(this);
        }

        if (e.getActionCommand().equals("rentReserved2")) {
                if (!cNumField.getText().equals("")) {
                    if (!CDLNField.getText().equals("")) {
                        if (delegate.rentReservedVehicle(cNumField.getText(), CDLNField.getText(), fromDateTimeField.getText(), toDateTimeField.getText(), cardNumberField2.getText(), cardExpDateField2.getText())) {
                            ;
                            JOptionPane.showMessageDialog(new JFrame(), "You have successfully rented a vehicle!\n\nReceipt: " +
                                    "\nConfirmation #: " + cNumField.getText() +
                                    "\nDriver's license #: " + CDLNField.getText(), "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                        rentReservedWindow.dispose();
                    } else JOptionPane.showMessageDialog(new JFrame(), "Please enter your driver's license #", "Error", JOptionPane.ERROR_MESSAGE);
                } else JOptionPane.showMessageDialog(new JFrame(), "Please enter your confirmation #", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

