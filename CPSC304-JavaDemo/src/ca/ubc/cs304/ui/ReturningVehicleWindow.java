package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ca.ubc.cs304.delegates.ReturningVehicleDelegate;
import ca.ubc.cs304.ui.utils.VehicleTypeNames;

public class ReturningVehicleWindow extends JFrame implements ActionListener {

    private JTextField dateTimeReturnedField;
    private JTextField odometerReadingField;
    private JCheckBox tankFullBox;

    private ReturningVehicleDelegate delegate;

    public ReturningVehicleWindow() {
        super("Return a Vehicle");
    }

    public void showFrame(ReturningVehicleDelegate delegate) {
        this.delegate = delegate;

        // declare all labels here
        JLabel dateTimeReturendLabel = new JLabel("Date & Time Returned: ");
        JLabel odometerReadingLabel = new JLabel("Odometer Reading: ");
        JLabel isTankFullLabel = new JLabel("Is gas tank full?: ");
        JLabel fromDateTimeLabel = new JLabel("From date & time: ");
        JLabel dateFormatLabel = new JLabel(" yyyy-mm-dd ");
        dateFormatLabel.setFont(new Font(fromDateTimeLabel.getFont().getFontName(), Font.ITALIC, (int)(fromDateTimeLabel.getFont().getSize()*0.8)));
        dateFormatLabel.setForeground(Color.DARK_GRAY);

        // declares all Swing input objects here
        tankFullBox = new JCheckBox();
        dateTimeReturnedField = new JTextField(10);
        odometerReadingField = new JTextField(10);

        JButton returnButton = new JButton("Return");

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
        gb.setConstraints(dateTimeReturendLabel, c);
        contentPane.add(dateTimeReturendLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(dateTimeReturnedField, c);
        contentPane.add(dateTimeReturnedField);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(17, 10, 15, 0);
        gb.setConstraints(odometerReadingLabel, c);
        contentPane.add(odometerReadingLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(odometerReadingField, c);
        contentPane.add(odometerReadingField);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(isTankFullLabel, c);
        contentPane.add(isTankFullLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(tankFullBox, c);
        contentPane.add(tankFullBox);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(returnButton, c);
        contentPane.add(returnButton);

        // register objects with action event handler
        returnButton.setActionCommand("return");
        returnButton.addActionListener(this);

        // size the window to obtain a best fit for the components
        this.pack();

        // center the frame
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

        // make the window visible
        this.setVisible(true);

        // place the cursor in the text field for the username
        dateTimeReturnedField.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("return")) {
            try {
                if (!dateTimeReturnedField.getText().trim().equals("")) {
                    if (!odometerReadingField.getText().trim().equals("")) {
                        // displays success message with conf num from what reserve func returns
                        int totalCost = 0;
                        String cNum = "1234";
                        delegate.returnVehicle(dateTimeReturnedField.getText(), Integer.parseInt(odometerReadingField.getText()), tankFullBox.isSelected());
                        JOptionPane.showMessageDialog(new JFrame(), "You have successfully returned a vehicle!\n\nReceipt:" +
                                "\nDate & Time Returned: " + dateTimeReturnedField.getText() +
                                "\nTotal cost: " + totalCost +
                                "\nHow cost: " + "enter how cost was calculated" +
                                (cNum != null ? "\nReservation Confirmation #: " + cNum : ""), "Success", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                    } else
                        JOptionPane.showMessageDialog(new JFrame(), "Please enter the vehicle's odometer reading", "Error", JOptionPane.ERROR_MESSAGE);
                } else
                    JOptionPane.showMessageDialog(new JFrame(), "Please enter the date & time the vehicle was returned", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(new JFrame(), "Please enter only numbers the odometer reading", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

