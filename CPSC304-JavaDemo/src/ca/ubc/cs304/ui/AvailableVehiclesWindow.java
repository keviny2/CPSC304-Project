package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.AvailableVehiclesDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AvailableVehiclesWindow extends JFrame implements ActionListener {
    private static final int TEXT_FIELD_WIDTH = 7;

    private JTextField vehicleTypeField;
    private JTextField locationField;
    private JTextField timeIntervalField;
    private JButton NOACNumber;

    private AvailableVehiclesDelegate delegate;

    public AvailableVehiclesWindow() {
        super("Find Available Vehicles");
    }

    public void showFrame(AvailableVehiclesDelegate delegate) {
        this.delegate = delegate;

        JLabel vehicleTypeLabel = new JLabel("Vehicle Type: ");
        JLabel locationLabel = new JLabel("Location: ");
        JLabel timeIntervalLabel = new JLabel("Time Interval: ");
        // label for number of available vehicles
        JLabel NOACLabel = new JLabel("Available vehicles: ");
        JLabel clickNOACLabel = new JLabel("Click the above number for details");
        clickNOACLabel.setFont(new Font(NOACLabel.getFont().getFontName(), Font.ITALIC, (int)(NOACLabel.getFont().getSize()*0.8)));
        clickNOACLabel.setForeground(Color.DARK_GRAY);

        vehicleTypeField = new JTextField(TEXT_FIELD_WIDTH);
        locationField = new JTextField(TEXT_FIELD_WIDTH);
        timeIntervalField = new JTextField(TEXT_FIELD_WIDTH);
        NOACNumber = new JButton("0");

        JButton findButton = new JButton("Find");

        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(NOACLabel, c);
        contentPane.add(NOACLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(NOACNumber, c);
        contentPane.add(NOACNumber);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(clickNOACLabel, c);
        contentPane.add(clickNOACLabel);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(15, 10, 10, 0);
        gb.setConstraints(vehicleTypeLabel, c);
        contentPane.add(vehicleTypeLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(15, 0, 10, 0);
        gb.setConstraints(vehicleTypeField, c);
        contentPane.add(vehicleTypeField);

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
        gb.setConstraints(timeIntervalLabel, c);
        contentPane.add(timeIntervalLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(timeIntervalField, c);
        contentPane.add(timeIntervalField);

        // place the find button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(findButton, c);
        contentPane.add(findButton);

        // register login button with action event handler
        findButton.addActionListener(this);

        // anonymous inner class for closing the window
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // size the window to obtain a best fit for the components
        this.pack();

        // center the frame
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

        // make the window visible
        this.setVisible(true);

        // place the cursor in the text field for the username
        vehicleTypeField.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        delegate.find(vehicleTypeField.getText(), locationField.getText(), Integer.parseInt(timeIntervalField.getText()));
    }
}
