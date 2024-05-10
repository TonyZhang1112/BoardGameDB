package ca.ubc.cs304.gui.tabs;

import ca.ubc.cs304.controller.DBHub;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.gui.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserTab extends Tab {
    private static final String menuTitle = "User Menu";
    private DBHub dbHub;
    private JLabel bgMenu;
    private DatabaseConnectionHandler dbHandler = null;
    public UserTab(UserSigninWindow window) {
        super(window);
        dbHub = window.getDbHub();
        dbHandler = dbHub.getDbHandler();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        placeTitle();
        placeButtons();
    }

    private void placeTitle() {
        bgMenu = new JLabel(menuTitle, JLabel.CENTER);
        bgMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(bgMenu);
    }

    private void placeButtons() {
        JButton b1 = new JButton("View Users");
        JButton b2 = new JButton("View Games on User Wishlists");


        JPanel buttonRow1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow1.add(b1);
        buttonRow1.add(b2);
        this.add(buttonRow1);

        b1.addActionListener(e -> viewUsers());
        b2.addActionListener(e -> viewWishlists());
    }

    public void viewUsers() {
        DefaultTableModel tableModel = dbHandler.showUser();
        createTableWindow("Users of this Glorious Platform", tableModel);
    }

    public void viewWishlists() {
        DefaultTableModel tableModel = dbHandler.showWishlistItems();
        createTableWindow("Games on All Wishlists", tableModel);
    }
}
