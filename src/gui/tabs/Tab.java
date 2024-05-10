package ca.ubc.cs304.gui.tabs;

import ca.ubc.cs304.gui.UserSigninWindow;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.math.BigDecimal;

public abstract class Tab extends JPanel {

    private final UserSigninWindow controller;
    public Tab(UserSigninWindow controller) {
        this.controller = controller;
    }

    //EFFECTS: creates and returns row with button included
    public JPanel formatButtonRow(JButton b) {
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(b);
        return p;
    }

    public UserSigninWindow getController() {
        return controller;
    }

    public boolean checkSemicolon(String string) {
        if (string.contains(";")) {
            JOptionPane.showMessageDialog(null, "Your input contains illegal character ;",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }
    public boolean checkSemicolonNoError(JTextField check) {
        String string = check.getText().trim();
        return string.contains(";");
    }

    public boolean checkSemicolonAny(String[] strings) {

            for (String s: strings) {
                if (s.contains(";")) {
                JOptionPane.showMessageDialog(null, "Your input contains illegal character ;",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return true;
            }
        }
        return false;
    }

    public void createTableWindow(String title, TableModel tableModel) {
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JFrame tableFrame = new JFrame(title);
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.setLayout(new BorderLayout());
        tableFrame.add(scrollPane, BorderLayout.CENTER);
        tableFrame.pack();
        tableFrame.setLocationRelativeTo(null);
        tableFrame.setVisible(true);
    }

    public int fieldToInt(JTextField textField) {
        int field = 0;

        try {
            String inputStr = textField.getText().trim();
            if (!inputStr.isEmpty()) {
                field = Integer.parseInt(inputStr);

            } else {
                JOptionPane.showMessageDialog(null, "Please enter a valid integer",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid integer",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }

        return field;
    }

public boolean fieldToIntCheck(JTextField textField) {
        int field = 0;

        try {
            String inputStr = textField.getText().trim();
            if (!inputStr.isEmpty()) {
                field = Integer.parseInt(inputStr);

            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public int fieldToIntGuaranteed(JTextField textField) {
        int field = 0;
        String inputStr = textField.getText().trim();
        field = Integer.parseInt(inputStr);
        return field;
    }

    public boolean fieldToDoubleCheck(JTextField textField) {
        double field = 0;

        try {
            String inputStr = textField.getText().trim();
            if (!inputStr.isEmpty()) {
                field = Double.parseDouble(inputStr);

            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public double fieldToDoubleGuaranteed(JTextField textField) {
        double field = 0;
        String inputStr = textField.getText().trim();
        field = Double.parseDouble(inputStr);
        return field;
    }


    public BigDecimal fieldToBD(JTextField textField) {
        BigDecimal field = BigDecimal.valueOf(0);
        try {
            String inputStr = textField.getText().trim();
            if (!inputStr.isEmpty()) {
                field = new BigDecimal(inputStr);
                return field;
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a valid decimal format",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid decimal format",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }

        return field;
    }


    public void confirmSuccess(String op) {
        JOptionPane.showMessageDialog(null, "Operation " + op + " was successful!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    }
