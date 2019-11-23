package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ca.ubc.cs304.delegates.SignUpDelegate;

public class SignUpWindow extends JFrame implements ActionListener {

    private JTextField fullNameField;
    private JTextField addressField;
    private JTextField DLNumberField;

    private SignUpDelegate delegate;

    public SignUpWindow() {
        super("New Customer Registration");
    }

    public void showFrame(SignUpDelegate delegate) {
        this.delegate = delegate;

        // declare all labels here
        JLabel fullNameLabel = new JLabel("Full name: ");
        JLabel addressLabel = new JLabel("Address: ");
        JLabel DLNumberLabel = new JLabel("Driver's License #: ");

        // declares all Swing input objects here
        fullNameField = new JTextField(8);
        addressField = new JTextField(11);
        DLNumberField = new JTextField(9);

        JButton signUpButton = new JButton("Register");

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
        c.anchor = GridBagConstraints.WEST;
        gb.setConstraints(fullNameLabel, c);
        contentPane.add(fullNameLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(fullNameField, c);
        contentPane.add(fullNameField);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(addressLabel, c);
        contentPane.add(addressLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(addressField, c);
        contentPane.add(addressField);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(DLNumberLabel, c);
        contentPane.add(DLNumberLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(DLNumberField, c);
        contentPane.add(DLNumberField);

        // place the reserve button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(signUpButton, c);
        contentPane.add(signUpButton);

        // register objects with action event handler
        signUpButton.setActionCommand("signup");
        signUpButton.addActionListener(this);

        // size the window to obtain a best fit for the components
        this.pack();

        // center the frame
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

        // make the window visible
        this.setVisible(true);

        // place the cursor in the text field for the username
        fullNameField.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand().equals("signup")) {
                if (!fullNameField.getText().trim().equals("")) {
                    if (!addressField.getText().trim().equals("")) {
                        if (!DLNumberField.getText().trim().equals("")) {
                            delegate.signUp(fullNameField.getText(), addressField.getText(), Long.parseLong(DLNumberField.getText()));
                            JOptionPane.showMessageDialog(new JFrame(), "You are now a registered customer at SuperRent!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            this.dispose();
                        } else
                            JOptionPane.showMessageDialog(new JFrame(), "Please enter your driver's license #", "Error", JOptionPane.ERROR_MESSAGE);
                    } else
                        JOptionPane.showMessageDialog(new JFrame(), "Please enter your address", "Error", JOptionPane.ERROR_MESSAGE);
                } else
                    JOptionPane.showMessageDialog(new JFrame(), "Please enter your full name", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(new JFrame(), "Please enter only numbers in the driver's license # field", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
