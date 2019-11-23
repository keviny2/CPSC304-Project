package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.NavigationDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NavigationWindow extends JFrame implements ActionListener {

    private NavigationDelegate delegate;

    public NavigationWindow() {
        super("SuperRent");
    }

    public void showFrame(NavigationDelegate delegate) {
        this.delegate = delegate;

        // declare all labels here
        JLabel customerLabel = new JLabel("Customers: ");
        JLabel clerkLabel = new JLabel("Clerks: ");
        JLabel adminLabel = new JLabel("Administrative Features: ");
        customerLabel.setFont(new Font(customerLabel.getFont().getFontName(), Font.BOLD, customerLabel.getFont().getSize()));
        clerkLabel.setFont(new Font(clerkLabel.getFont().getFontName(), Font.BOLD, clerkLabel.getFont().getSize()));

        JButton availableButton = new JButton("View Available Vehicles");
        JButton signUpButton = new JButton("Register as a New Customer");
        JButton makeResButton = new JButton("Make a Reservation");
        JButton rentingButton = new JButton("Rent Out a Vehicle");
        JButton returningButton = new JButton("Return a Vehicle");
        JButton reportsButton = new JButton("View Reports");
        JButton manipDBButton = new JButton("Database Manipulation");

        availableButton.setPreferredSize(new Dimension(250, 35));
        signUpButton.setPreferredSize(new Dimension(250, 35));
        makeResButton.setPreferredSize(new Dimension(250, 35));
        rentingButton.setPreferredSize(new Dimension(250, 35));
        returningButton.setPreferredSize(new Dimension(250, 35));
        reportsButton.setPreferredSize(new Dimension(250, 35));
        manipDBButton.setPreferredSize(new Dimension(250, 35));

        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // set the grid specs for all labels and objects
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 0);
        c.anchor = GridBagConstraints.WEST;
        gb.setConstraints(customerLabel, c);
        contentPane.add(customerLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(signUpButton, c);
        contentPane.add(signUpButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(availableButton, c);
        contentPane.add(availableButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(makeResButton, c);
        contentPane.add(makeResButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(clerkLabel, c);
        contentPane.add(clerkLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(rentingButton, c);
        contentPane.add(rentingButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(returningButton, c);
        contentPane.add(returningButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 15, 0);
        gb.setConstraints(reportsButton, c);
        contentPane.add(reportsButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(17, 10, 10, 0);
        gb.setConstraints(adminLabel, c);
        contentPane.add(adminLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(manipDBButton, c);
        contentPane.add(manipDBButton);

        // register objects with action event handler
        signUpButton.setActionCommand("signup");
        signUpButton.addActionListener(this);
        availableButton.setActionCommand("available");
        availableButton.addActionListener(this);
        makeResButton.setActionCommand("makeres");
        makeResButton.addActionListener(this);
        rentingButton.setActionCommand("rent");
        rentingButton.addActionListener(this);
        returningButton.setActionCommand("return");
        returningButton.addActionListener(this);
        reportsButton.setActionCommand("reports");
        reportsButton.addActionListener(this);
        manipDBButton.setActionCommand("manip");
        manipDBButton.addActionListener(this);

        // size the window to obtain a best fit for the components
        this.pack();

        // center the frame
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

        // make the window visible
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("signup")) {
            delegate.signUp();
        } else if (e.getActionCommand().equals("available")) {
            delegate.available();
        } else if (e.getActionCommand().equals("makeres")) {
            delegate.makeRes();
        } else if (e.getActionCommand().equals("rent")) {
            delegate.renting();
        } else if (e.getActionCommand().equals("return")) {
            delegate.returning();
        } else if (e.getActionCommand().equals("reports")) {
            delegate.reports();
        } else if (e.getActionCommand().equals("manip")) {
            delegate.manip();
        }
    }
}
