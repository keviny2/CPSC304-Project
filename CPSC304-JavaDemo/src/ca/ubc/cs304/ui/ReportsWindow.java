package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ca.ubc.cs304.delegates.ReportsDelegate;

public class ReportsWindow extends JFrame implements ActionListener {

    private ReportsDelegate delegate;

    private JTextField rentalsCityField;
    private JTextField rentalsLocationField;
    private JTextField returnsCityField;
    private JTextField returnsLocationField;

    public ReportsWindow() {
        super("Generate Reports");
    }

    public void showFrame(ReportsDelegate delegate) {
        this.delegate = delegate;

        // declare all labels here
        JLabel dailyRentalsLabel = new JLabel("Daily Rentals: ");
        JLabel everyBranchLabel = new JLabel("For every branch: ");
        JLabel specificBranchLabel = new JLabel("For a specific branch: ");
        JLabel dailyReturnsLabel = new JLabel("Daily Returns: ");
        JLabel cityLabel = new JLabel("City: ");
        JLabel locationLabel = new JLabel("Location: ");
        JLabel cityLabel2 = new JLabel("City: ");
        JLabel locationLabel2 = new JLabel("Location: ");
        JLabel everyBranchLabel2 = new JLabel("For every branch: ");
        JLabel specificBranchLabel2 = new JLabel("For a specific branch: ");

        dailyRentalsLabel.setFont(new Font(everyBranchLabel.getFont().getFontName(), Font.BOLD, everyBranchLabel.getFont().getSize()));
        dailyReturnsLabel.setFont(new Font(everyBranchLabel.getFont().getFontName(), Font.BOLD, everyBranchLabel.getFont().getSize()));

        // declares all Swing input objects here
        JButton dailyRentalsButton = new JButton("Generate");
        JButton dailyRentalsForBranchButton = new JButton("Generate");
        JButton dailyReturnsButton = new JButton("Generate");
        JButton dailyReturnsForBranchButton = new JButton("Generate");

        rentalsCityField = new JTextField(10);
        rentalsLocationField = new JTextField(10);
        returnsCityField = new JTextField(10);
        returnsLocationField = new JTextField(10);

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
        gb.setConstraints(dailyRentalsLabel, c);
        contentPane.add(dailyRentalsLabel);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 25, 10, 0);
        gb.setConstraints(everyBranchLabel, c);
        contentPane.add(everyBranchLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(dailyRentalsButton, c);
        contentPane.add(dailyRentalsButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 25, 10, 0);
        gb.setConstraints(specificBranchLabel, c);
        contentPane.add(specificBranchLabel);

        c.gridwidth = GridBagConstraints.WEST;
        c.insets = new Insets(10, 60, 10, 0);
        gb.setConstraints(cityLabel, c);
        contentPane.add(cityLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(rentalsCityField, c);
        contentPane.add(rentalsCityField);

        c.gridwidth = GridBagConstraints.WEST;
        c.insets = new Insets(10, 60, 10, 10);
        gb.setConstraints(locationLabel, c);
        contentPane.add(locationLabel);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 0, 10, 10);
        gb.setConstraints(rentalsLocationField, c);
        contentPane.add(rentalsLocationField);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 10);
        gb.setConstraints(dailyRentalsForBranchButton, c);
        contentPane.add(dailyRentalsForBranchButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 0);
        c.anchor = GridBagConstraints.WEST;
        gb.setConstraints(dailyReturnsLabel, c);
        contentPane.add(dailyReturnsLabel);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 25, 10, 0);
        gb.setConstraints(everyBranchLabel2, c);
        contentPane.add(everyBranchLabel2);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(dailyReturnsButton, c);
        contentPane.add(dailyReturnsButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 25, 10, 0);
        gb.setConstraints(specificBranchLabel2, c);
        contentPane.add(specificBranchLabel2);

        c.gridwidth = GridBagConstraints.WEST;
        c.insets = new Insets(10, 60, 10, 0);
        gb.setConstraints(cityLabel2, c);
        contentPane.add(cityLabel2);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(returnsCityField, c);
        contentPane.add(returnsCityField);

        c.gridwidth = GridBagConstraints.WEST;
        c.insets = new Insets(10, 60, 10, 10);
        gb.setConstraints(locationLabel2, c);
        contentPane.add(locationLabel2);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 0, 10, 10);
        gb.setConstraints(returnsLocationField, c);
        contentPane.add(returnsLocationField);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 10);
        gb.setConstraints(dailyReturnsForBranchButton, c);
        contentPane.add(dailyReturnsForBranchButton);

        // register objects with action event handler
        dailyRentalsButton.setActionCommand("dailyRentals");
        dailyRentalsButton.addActionListener(this);
        dailyRentalsForBranchButton.setActionCommand("dailyRentalsBranch");
        dailyRentalsForBranchButton.addActionListener(this);
        dailyReturnsButton.setActionCommand("dailyReturns");
        dailyReturnsButton.addActionListener(this);
        dailyReturnsForBranchButton.setActionCommand("dailyReturnsBranch");
        dailyReturnsForBranchButton.addActionListener(this);

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
        if (e.getActionCommand().equals("dailyRentals")) {
            delegate.dailyRentals();
        } else if (e.getActionCommand().equals("dailyRentalsBranch")) {
            // TODO: @Ryan plug in city and location pls
            delegate.dailyRentalsBranch("Akron", "OH");
        }
    }
}

