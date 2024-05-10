package ca.ubc.cs304.gui.tabs;

import ca.ubc.cs304.controller.DBHub;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.gui.UserSigninWindow;
import ca.ubc.cs304.util.TextChecker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SearchTab extends Tab {
    private static final String menuTitle = "View and Filter Tables";
    private DBHub dbHub;
    private JLabel search;
    private DatabaseConnectionHandler dbHandler = null;
    private JLabel selectTableText;
    private JComboBox tableOptions;
    private UserSigninWindow window;

    private JLabel errorMessage;

    private JPanel topButtonPanel;
    private JPanel selectTablePanel;
    private JPanel columnOptionsPanel;
    private JPanel enterOptionsPanel;
    private JPanel bottomButtonPanel;
    private JPanel errorPanel;

    private JTextField attrTextBox;

    String tableSelected;
    String[] attributeOptions;
    public SearchTab(UserSigninWindow window) {
        super(window);
        this.window = window;
        dbHub = window.getDbHub();
        dbHandler = dbHub.getDbHandler();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initTitle();
        initPanelLayout();
        initButton();
        update(this.getGraphics());
    }

    private void initPanelLayout() {
        topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectTablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        columnOptionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        enterOptionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.add(topButtonPanel);
        this.add(selectTablePanel);
        this.add(columnOptionsPanel);
        this.add(enterOptionsPanel);
        this.add(bottomButtonPanel);
        this.add(errorPanel);
    }
    private void initTitle() {
        search = new JLabel(menuTitle, JLabel.CENTER);
        search.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(search);
    }

    private void initButton() {
        JButton initButton = new JButton("Begin Process");

        topButtonPanel.add(initButton);

        initButton.addActionListener(e -> initTableSelect());
    }

    private void initTableSelect() {
        selectTablePanel.removeAll();
        columnOptionsPanel.removeAll();
        enterOptionsPanel.removeAll();
        bottomButtonPanel.removeAll();
        
        selectTableText = new JLabel("Select a table to view:");
        selectTablePanel.add(selectTableText);
        String[] tables = dbHandler.getTableNames();

        tableOptions = new JComboBox(tables);
        tableOptions.addItemListener(e -> {initColumnOptions(); attrTextBox.setText("");});
        selectTablePanel.add(tableOptions);

        initColumnOptions();
        initEnterOptions();
        initSubmitButton();
        initErrorPanel();
    }

    private void initColumnOptions() {
        tableSelected = tableOptions.getSelectedItem().toString();
        attributeOptions = dbHandler.getColumnNames(tableSelected);
        String colNamesStr = String.join(", ", attributeOptions);
        selectTableText = new JLabel("The attributes available for " + tableSelected + " are: " + colNamesStr);

        columnOptionsPanel.removeAll();
        columnOptionsPanel.add(selectTableText);

        update(getGraphics());
        revalidate();
        repaint();
    }

    private void initEnterOptions() {
        JLabel message = new JLabel("Enter the attributes you want to see, seperated by commas " +
                "(Leave empty for all attributes): ");
        attrTextBox = new JTextField(20);
        enterOptionsPanel.add(message);
        enterOptionsPanel.add(attrTextBox);

        update(getGraphics());
        revalidate();
        repaint();
    }

    private void initSubmitButton() {
        JButton initButton = new JButton("View Table");
        initButton.setBackground(Color.GREEN);

        bottomButtonPanel.add(initButton);

        initButton.addActionListener(e -> showProjectedTable());

        update(getGraphics());
        revalidate();
        repaint();
    }

    private void initErrorPanel() {
        errorMessage = new JLabel("");
        errorPanel.add(errorMessage);

        update(getGraphics());
        revalidate();
        repaint();
    }

    private void showProjectedTable() {
        tableSelected = tableOptions.getSelectedItem().toString();
        String attrsSelected = attrTextBox.getText().trim();
        DefaultTableModel tableModel;
        if (TextChecker.isEmpty(attrsSelected)) {
            tableModel = dbHandler.getSearchResult(tableSelected, new String[]{"*"});
            createTableWindow("View " + tableSelected, tableModel);
            return;
        }
        else if (TextChecker.hasSemicolon(attrsSelected)) {
            errorMessage.setText("Your input contains a semicolon, which we do not allow.");
            return;
        }
        String[] chosenAttrs = attrsSelected.split(",");

        for (String s: chosenAttrs) {
            s = s.trim();
            if (s == "") {
                errorMessage.setText("You left an attribute blank.");
                return;
            }
            boolean isIn = false;
            for (int i = 0; i < attributeOptions.length; i++) {
                if (attributeOptions[i].equals(s.toUpperCase())) {
                    isIn = true;
                    break;
                }
            }
            if (!isIn) {
                errorMessage.setText("Attribute " + s + " does not exist in table " + tableSelected);
                return;
            }
        }

        tableModel = dbHandler.getSearchResult(tableSelected, chosenAttrs);
        createTableWindow("View " + tableSelected, tableModel);
    }
}
