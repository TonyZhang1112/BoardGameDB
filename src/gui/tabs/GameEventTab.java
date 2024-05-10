package ca.ubc.cs304.gui.tabs;

import ca.ubc.cs304.controller.DBHub;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.gui.UserSigninWindow;
import ca.ubc.cs304.model.FeaturedModel;
import ca.ubc.cs304.model.GameEventModel;
import ca.ubc.cs304.model.GameEventSeriesModel;
import ca.ubc.cs304.model.HostsModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GameEventTab extends Tab {

    private static final String menuTitle = "Game Event and Series Menu";
    private DBHub dbHub;
    private JLabel menu;
    private DatabaseConnectionHandler dbHandler = null;

    public GameEventTab(UserSigninWindow window) {
        super(window);
        dbHub = window.getDbHub();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        placeTitle();
        placeButtons();
    }

    private void placeTitle() {
        menu = new JLabel(menuTitle, JLabel.CENTER);
        menu.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(menu);
    }

    private void placeButtons() {
        JButton b1 = new JButton("View Game Event");
        JButton b2 = new JButton("View Game Event Series");
        JButton b3 = new JButton("View Number of Hosted GameEvents per user");
        JButton b4 = new JButton("Insert Game Event Series");
        JButton b5 = new JButton("Insert Game Event");
        JButton b6 = new JButton("View Featured");
        JButton b7 = new JButton("View Hosts");
        JButton b8 = new JButton("Delete Game Event Series");

        JPanel buttonRow1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow1.add(b1);
        buttonRow1.add(b2);
        this.add(buttonRow1);

        JPanel buttonRow2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow2.add(b3);
        buttonRow2.add(b4);
        this.add(buttonRow2);

        JPanel buttonRow3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow3.add(b5);
        buttonRow3.add(b6);
        buttonRow3.add(b7);
        this.add(buttonRow3);

        JPanel buttonRow4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow4.add(b8);
        this.add(buttonRow4);

        b1.addActionListener(e -> showGE());
        b2.addActionListener(e -> showGES());
        b3.addActionListener(e -> showGEHosted());
        b4.addActionListener(e -> insertGES());
        b5.addActionListener(e -> insertGE());
        b6.addActionListener(e -> showFeatured());
        b7.addActionListener(e -> showHosts());
        b8.addActionListener(e -> deleteGES());

    }

    private void deleteGES() {

        JTextField seriesNameField = new JTextField(15);
        JPanel inputPanel = new JPanel(new GridLayout(1,1));
        inputPanel.add(new JLabel("Series Name to remove:"));
        inputPanel.add(seriesNameField);

        int result = JOptionPane.showConfirmDialog(
                null,
                inputPanel,
                "Delete Game Event Series",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            String seriesName = seriesNameField.getText().trim();
            if (checkSemicolon(seriesName)) {
                return;
            }

            if (seriesName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Series Name cannot be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (dbHub.getDbHandler().countSeriesName(seriesName) == 0) {
                JOptionPane.showMessageDialog(null, "Series Name" + seriesName +
                                " does not exist!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dbHub.getDbHandler().deleteGES(seriesName);
            confirmSuccess("Deletion of Game Event Series " + seriesName);

        }
    }

    private void showHosts() {

        HostsModel[] models = dbHub.getDbHandler().getHostsInfo();

        String[] columnNames = {"Username", "Session Number", "Series Name"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        String seriesName = null;
        String username = null;
        int sessionNumber = 0;

        for (HostsModel model : models) {

            sessionNumber = model.getSessionNum();
            seriesName = model.getSeriesName();
            username = model.getUsername();

            Object[] row = {username, sessionNumber, seriesName};
            tableModel.addRow(row);

        }

        createTableWindow("Game Event Hosted", tableModel);
    }

    private void showFeatured() {

        FeaturedModel[] models = dbHub.getDbHandler().getFeatInfo();

        String[] columnNames = {"Board Game ID", "Session Number", "Series Name"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        String seriesName = null;
        int bgID = 0;
        int sessionNumber = 0;

        for (FeaturedModel model : models) {

            sessionNumber = model.getSessionNum();
            seriesName = model.getSeriesName();
            bgID = model.getBoardGameId();

            Object[] row = {bgID, sessionNumber, seriesName};
            tableModel.addRow(row);

        }

        createTableWindow("Featured Board Games in Game Event", tableModel);

    }

    private void insertGES() {

        JTextField seriesNameField = new JTextField(15);
        JTextField locationField = new JTextField(15);
        JTextField startDateField = new JTextField(15);
        JTextField endDateField = new JTextField(15);
        JTextField creatorNameField = new JTextField(15);

        JPanel inputPanel = new JPanel(new GridLayout(5,1));

        inputPanel.add(new JLabel("Series Name:"));
        inputPanel.add(seriesNameField);
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locationField);
        inputPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        inputPanel.add(startDateField);
        inputPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        inputPanel.add(endDateField);
        inputPanel.add(new JLabel("Creator Name:"));
        inputPanel.add(creatorNameField);

        int result = JOptionPane.showConfirmDialog(
                null,
                inputPanel,
                "Insert Game Event Series",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            String seriesName = seriesNameField.getText().trim();
            String location = locationField.getText().trim();
            String startDateStr = startDateField.getText().trim();
            String endDateStr = endDateField.getText().trim();
            String creatorName = creatorNameField.getText().trim();

            if (checkSemicolon(seriesName) ||
            checkSemicolon(location) ||
            checkSemicolon(startDateStr) ||
            checkSemicolon(endDateStr) ||
            checkSemicolon(creatorName)) {
                return;
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setLenient(false);

            if (seriesName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Series Name cannot be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (location.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Location cannot be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (startDateStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Start Date cannot be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (endDateStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "End Date cannot be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (creatorName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Creator name cannot be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (dbHub.getDbHandler().countSeriesName(seriesName) > 0) {
                JOptionPane.showMessageDialog(null, "Series Name" + seriesName +
                                " already exists already!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!dbHub.getDbHandler().isUserName(creatorName)) {
                JOptionPane.showMessageDialog(null,
                        "Creator name " + creatorName + " does not exist!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Date startDate = new Date(simpleDateFormat.parse(startDateStr).getTime());
                Date endDate =  new Date(simpleDateFormat.parse(endDateStr).getTime());
                GameEventSeriesModel gesModel = new GameEventSeriesModel(seriesName, location, startDate, endDate, creatorName);
                dbHub.getDbHandler().insertGameEventSeries(gesModel);
                confirmSuccess("Insertion of Series " + seriesName);

            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid Date Format Detected",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private void insertGE() {

        JTextField seriesNameField = new JTextField(15);
        JTextField creatorNameField = new JTextField(15);
        JTextField bgIDField = new JTextField(15);
        JTextField locationField = new JTextField(15);
        JTextField eventDateField = new JTextField(15);

        JPanel inputPanel = new JPanel(new GridLayout(5,1));

        inputPanel.add(new JLabel("Series Name:"));
        inputPanel.add(seriesNameField);
        inputPanel.add(new JLabel("Host Username:"));
        inputPanel.add(creatorNameField);
        inputPanel.add(new JLabel("Board Game ID:"));
        inputPanel.add(bgIDField);
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locationField);
        inputPanel.add(new JLabel("Event Date (Format: YYYY-MM-DD):"));
        inputPanel.add(eventDateField);

        int result = JOptionPane.showConfirmDialog(
                null,
                inputPanel,
                "Insert Game Event",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            String seriesName = seriesNameField.getText().trim();
            String creatorName = creatorNameField.getText().trim();
            int bgID = fieldToInt(bgIDField);
            String location = locationField.getText().trim();
            String eventDateStr = eventDateField.getText().trim();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setLenient(false);

            if (checkSemicolon(seriesName) ||
            checkSemicolon(creatorName) ||
            checkSemicolon(location) ||
            checkSemicolon(eventDateStr)) {
                return;
            }

            if (seriesName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Series Name cannot be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (creatorName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Creator Name cannot be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (location.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Location cannot be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (dbHub.getDbHandler().countSeriesName(seriesName) < 1) {
                JOptionPane.showMessageDialog(null, "Series Name " + seriesName +
                                " does not exist!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!dbHub.getDbHandler().isUserName(creatorName)) {
                JOptionPane.showMessageDialog(null,
                        "Creator name " + creatorName + " does not exist!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }


            int sessionNum = dbHub.getDbHandler().getEventSessionNum(seriesName) + 1;

            Date eventDate = null;

            try {
                eventDate = new Date(simpleDateFormat.parse(eventDateStr).getTime());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid Date Format Detected",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            GameEventModel model = new GameEventModel(sessionNum,
                    seriesName,
                    location,
                    eventDate);

            FeaturedModel model2 = new FeaturedModel(bgID,
                    sessionNum,
                    seriesName);

            HostsModel model3 = new HostsModel(creatorName,
                    sessionNum,
                    seriesName);

            dbHub.getDbHandler().insertGameEvent(model);
            dbHub.getDbHandler().insertFeatured(model2);
            dbHub.getDbHandler().insertHosts(model3);
            confirmSuccess("Insertion of event " +
                    Integer.toString(sessionNum) + " for " + seriesName);
        }

    }

    private void showGEHosted() {
        DefaultTableModel tableModel = dbHub.getDbHandler().getNumHostsForUserFE();
        createTableWindow("Games Hosted by Users", tableModel);
    }

    private void showGES() {
        DefaultTableModel tableModel = dbHub.getDbHandler().getGESFullInfo();
        createTableWindow("Full Game Event Series", tableModel);
    }

    private void showGES1() {

        GameEventSeriesModel[] models = dbHub.getDbHandler().getGESInfo();

        String[] columnNames = {"Session Number", "Series Name", "Location", "Game Series Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        String seriesName, location, creatorName;
        int duration;
        Date startDate, endDate;

        for (GameEventSeriesModel model : models) {

            seriesName = model.getSeriesName();
            location = model.getLocation();
            creatorName = model.getCreatorName();
            startDate = model.getStartDate();
            endDate = model.getEndDate();

            Object[] row = {seriesName, seriesName, startDate, endDate, creatorName};
            tableModel.addRow(row);

        }

        createTableWindow("Game Event", tableModel);

    }

    private void showGE() {

        GameEventModel[] models = dbHub.getDbHandler().getGEInfo();

        String[] columnNames = {"Session Number", "Series Name", "Location", "Event Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        String seriesName = null, location = null;
        int sessionNumber = 0;
        Date gameSeriesDate = null;

        for (GameEventModel model : models) {

            sessionNumber = model.getSessionNum();
            seriesName = model.getSeriesName();
            location = model.getLocation();
            gameSeriesDate = model.getDate();

            Object[] row = {sessionNumber, seriesName, location, gameSeriesDate};
            tableModel.addRow(row);

            }

            createTableWindow("Game Event", tableModel);

        }

    }



