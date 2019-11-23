package ca.ubc.cs304.ui;

import ca.ubc.cs304.controller.ManipulateDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManipulateDatabaseWindow extends JFrame implements ActionListener {

    private JTextField viewTableNameField;
    private JTextField insertTableNameField;
    private JTextField insertTableValuesField;
    private JTextField deleteTableNameField;
    private JTextField deleteTableConditionsField;

    private ManipulateDatabase delegate;

    public ManipulateDatabaseWindow() {
        super("Manipulate the Database");
    }

    public void showFrame(ManipulateDatabase delegate) {
        this.delegate = delegate;

        // declare all labels here
        JLabel tableListLabel = new JLabel("Click to View All Table Names: ");
        JLabel viewTableLabel = new JLabel("View All Rows of a Table: ");
        JLabel viewTableNameLabel = new JLabel("Table name: ");
        JLabel insertTableLabel = new JLabel("Insert a row into a Table: ");
        JLabel deleteTableLabel = new JLabel("Delete a row from a Table: ");
        JLabel insertTableNameLabel = new JLabel("Table name: ");
        JLabel deleteTableNameLabel = new JLabel("Table name: ");
        JLabel valuesLabel = new JLabel("VALUES(");
        JLabel valuesLabel2 = new JLabel(")");
        JLabel conditionsLabel = new JLabel("Conditions (WHERE): ");
        JLabel conditionsLabel2 = new JLabel("i.e. id = '10', name = 'John'");

        tableListLabel.setFont(new Font(viewTableNameLabel.getFont().getFontName(), Font.BOLD, viewTableNameLabel.getFont().getSize()));
        viewTableLabel.setFont(new Font(viewTableNameLabel.getFont().getFontName(), Font.BOLD, viewTableNameLabel.getFont().getSize()));
        insertTableLabel.setFont(new Font(viewTableNameLabel.getFont().getFontName(), Font.BOLD, viewTableNameLabel.getFont().getSize()));
        deleteTableLabel.setFont(new Font(viewTableNameLabel.getFont().getFontName(), Font.BOLD, viewTableNameLabel.getFont().getSize()));
        conditionsLabel2.setFont(new Font(viewTableNameLabel.getFont().getFontName(), Font.ITALIC, viewTableNameLabel.getFont().getSize()));


        // declares all Swing input objects here
        viewTableNameField = new JTextField(8);
        insertTableNameField = new JTextField(8);
        insertTableValuesField = new JTextField(15);
        deleteTableNameField = new JTextField(8);
        deleteTableConditionsField = new JTextField(15);

        JButton tableListButton = new JButton("View");
        JButton viewTableButton = new JButton("View");
        JButton insertTableButton = new JButton("Insert");
        JButton deleteTableButton = new JButton("Delete");

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
        gb.setConstraints(tableListLabel, c);
        contentPane.add(tableListLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(tableListButton, c);
        contentPane.add(tableListButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(viewTableLabel, c);
        contentPane.add(viewTableLabel);

        c.gridwidth = GridBagConstraints.WEST;
        c.insets = new Insets(10, 25, 10, 0);
        gb.setConstraints(viewTableNameLabel, c);
        contentPane.add(viewTableNameLabel);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(viewTableNameField, c);
        contentPane.add(viewTableNameField);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(viewTableButton, c);
        contentPane.add(viewTableButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(insertTableLabel, c);
        contentPane.add(insertTableLabel);

        c.gridwidth = GridBagConstraints.WEST;
        c.insets = new Insets(10, 25, 10, 0);
        gb.setConstraints(insertTableNameLabel, c);
        contentPane.add(insertTableNameLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(insertTableNameField, c);
        contentPane.add(insertTableNameField);

        c.gridwidth = GridBagConstraints.WEST;
        c.insets = new Insets(10, 25, 10, 0);
        gb.setConstraints(valuesLabel, c);
        contentPane.add(valuesLabel);

        c.gridwidth = GridBagConstraints.WEST;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(insertTableValuesField, c);
        contentPane.add(insertTableValuesField);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(valuesLabel2, c);
        contentPane.add(valuesLabel2);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(insertTableButton, c);
        contentPane.add(insertTableButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 0);
        gb.setConstraints(deleteTableLabel, c);
        contentPane.add(deleteTableLabel);

        c.gridwidth = GridBagConstraints.WEST;
        c.insets = new Insets(10, 25, 10, 0);
        gb.setConstraints(deleteTableNameLabel, c);
        contentPane.add(deleteTableNameLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(deleteTableNameField, c);
        contentPane.add(deleteTableNameField);

        c.gridwidth = GridBagConstraints.WEST;
        c.insets = new Insets(10, 25, 10, 0);
        gb.setConstraints(conditionsLabel, c);
        contentPane.add(conditionsLabel);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(deleteTableConditionsField, c);
        contentPane.add(deleteTableConditionsField);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(deleteTableButton, c);
        contentPane.add(deleteTableButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 0, 10, 0);
        gb.setConstraints(conditionsLabel2, c);
        contentPane.add(conditionsLabel2);

        // register objects with action event handler
        tableListButton.setActionCommand("tableList");
        tableListButton.addActionListener(this);
        viewTableButton.setActionCommand("viewTable");
        viewTableButton.addActionListener(this);
        insertTableButton.setActionCommand("insert");
        insertTableButton.addActionListener(this);
        deleteTableButton.setActionCommand("delete");
        deleteTableButton.addActionListener(this);

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
        if (e.getActionCommand().equals("tableList")) {
            delegate.tableList();
        } else if (e.getActionCommand().equals("viewTable")) {
            delegate.viewTable(viewTableNameField.getText());
        } else if (e.getActionCommand().equals("insert")) {
            delegate.insertIntoTable(insertTableNameField.getText(), insertTableValuesField.getText());
        } else if (e.getActionCommand().equals("delete")) {
            delegate.deleteFromTable(deleteTableNameField.getText(), deleteTableConditionsField.getText());
        }
    }
}
