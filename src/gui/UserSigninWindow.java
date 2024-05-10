package ca.ubc.cs304.gui;

import ca.ubc.cs304.controller.DBHub;
import ca.ubc.cs304.gui.tabs.*;

import javax.swing.*;

public class UserSigninWindow extends JFrame{

    private static final int WIDTH = 1280;
    private static final int  HEIGHT = 720;

    private static final int LoginIdx = 0;
    private static final int userIdx = 1;
    private static final int bgIdx = 2;
    private static final int gesIdx = 3;
    private static final int geIdx = 4;
    private DBHub dbHub;

    private JTabbedPane sidebar;
    public static void main(String[] args) {
        new UserSigninWindow();
    }

    public UserSigninWindow() {

        super("Board Game Database Console");

        dbHub = new DBHub();

        setSize(WIDTH, HEIGHT);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        sidebar = new JTabbedPane();
        setupLoginTab();
        add(sidebar);
        setVisible(true);

        dbHub.start();

    }

    private void setupLoginTab() {
        JPanel homeTab = new HomeTab(this);
        sidebar.add(homeTab, 0);
        sidebar.setTitleAt(0, "Welcome");

        JPanel createAccountTab = new CreateAccountTab(this);
        sidebar.add(createAccountTab, 1);
        sidebar.setTitleAt(1, "Create Account");

        JPanel createBoardGameTab = new BoardGameTab(this);
        sidebar.add(createBoardGameTab, 2);
        sidebar.setTitleAt(2, "Board Game");

        JPanel createGameEventTab = new GameEventTab(this);
        sidebar.add(createGameEventTab, 3);
        sidebar.setTitleAt(3, "Game Event and Series");

        JPanel createPubTab = new PublisherTab(this);
        sidebar.add(createPubTab, 4);
        sidebar.setTitleAt(4, "Publisher");

        JPanel createListingTab = new ListingTab(this);
        sidebar.add(createListingTab, 5);
        sidebar.setTitleAt(5, "Listing");

        JPanel userTab = new UserTab(this);
        sidebar.add(userTab, 6);
        sidebar.setTitleAt(6, "Users");

        JPanel searchTab = new SearchTab(this);
        sidebar.add(searchTab, 7);
        sidebar.setTitleAt(7, "Search");
    }

    public DBHub getDbHub(){
        return this.dbHub;
    }
}
