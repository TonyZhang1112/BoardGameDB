package ca.ubc.cs304.gui.tabs;

import ca.ubc.cs304.controller.DBHub;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.gui.UserSigninWindow;
import ca.ubc.cs304.model.BoardGameModel;
import ca.ubc.cs304.model.ReviewsModel;
import ca.ubc.cs304.model.TeamGameModel;
import ca.ubc.cs304.model.TournamentGameModel;
import ca.ubc.cs304.util.TextChecker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class BoardGameTab extends Tab {

    private static final String menuTitle = "Board Game Menu";
    private DBHub dbHub;
    private JLabel bgMenu;
    private DatabaseConnectionHandler dbHandler = null;

    public BoardGameTab(UserSigninWindow window) {
        super(window);
        dbHub = window.getDbHub();
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
        JButton b1 = new JButton("Insert Board Game");
        JButton b2 = new JButton("View Board Games");
        JButton b3 = new JButton("View Team Games");
        JButton b4 = new JButton("View Tournament Games");
        JButton b5 = new JButton("View Board Game Reviews");
        JButton b6 = new JButton("View Board Game w/ Publisher");
        JButton b7 = new JButton("Show Users wishing X Board Games");


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
        this.add(buttonRow3);

        JPanel buttonRow4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow4.add(b7);
        this.add(buttonRow4);

        b1.addActionListener(e -> insertBG());
        b2.addActionListener(e -> showBG());
        b3.addActionListener(e -> showTeamBG());
        b4.addActionListener(e -> showTourBG());
        b5.addActionListener(e -> showReview());
        b6.addActionListener(e -> showBGPub());
        b7.addActionListener(e -> showUserWBGX());

    }

    private void showUserWBGX() {

        JPanel inputPanel = new JPanel(new GridLayout());
        JTextField bIDField = new JTextField(15);
        inputPanel.add(new JLabel("Enter the Board Game IDs to check (separated by commas, for example: 1,2):"));
        inputPanel.add(bIDField);

        int result = JOptionPane.showConfirmDialog(
                null,
                inputPanel,
                "Check Users Wishing X Board Games",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String inputText = bIDField.getText().trim();

            if (checkSemicolon(inputText)) {
                return;
            }
            String[] idArray = inputText.split(",");
            int[] ids = new int[idArray.length];

            for (int i = 0; i< idArray.length; i++) {
                if (!TextChecker.isInteger(idArray[i])) {
                    JOptionPane.showMessageDialog(null,
                            "The Board Game ID must be an integer!",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ids[i] = Integer.parseInt(idArray[i]);
                if (!dbHub.getDbHandler().isBoardGameID(ids[i])) {
                    JOptionPane.showMessageDialog(null,
                            "Board Game ID" + ids[i] +" does not exist!",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }
            DefaultTableModel tableModel = dbHub.getDbHandler().findUsersWishingGamesFE(idArray);
            createTableWindow("Users wishing Board Game ID" + Arrays.toString(idArray), tableModel);

        }

    }

    private void showBGPub() {
        DefaultTableModel tableModel = dbHub.getDbHandler().getBGPubInfo();
        createTableWindow("Board Game with Publisher", tableModel);
    }

    private void showReview() {
        ReviewsModel[] models = dbHub.getDbHandler().getReviewInfo();

        String[] columnNames = {"Username", "Board Game ID", "Review Date", "Score", "Review"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        int bID, score;
        String username, review;
        Date date;

        for (ReviewsModel model : models) {

            bID = model.getBoardGameId();

            username = model.getUsername();

            if (model.getReview() != null) {
                review = model.getReview();
            } else {
                review = " ";
            }

            date = model.getRDate();
            score = model.getScore();


            Object[] row = {username, bID, date, score, review};
            tableModel.addRow(row);
        }

        createTableWindow("Board Game Review: ", tableModel);

    }

    private void showTourBG() {
        TournamentGameModel[] models = dbHub.getDbHandler().getTournamentInfo();
        String[] columnNames = {"BoardGameID", "Tournament Rules", "Min Participants"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        int bID, minPar;
        String rules;

        for (TournamentGameModel model : models) {

            bID = model.getId();

            if (model.getRules() != null) {
                rules = model.getRules();
            } else {
                rules = " ";
            }

            minPar= model.getMinParticipants();

            Object[] row = {bID, rules, minPar};
            tableModel.addRow(row);

        }

        createTableWindow("Tournament Game: ", tableModel);

    }

    private void showTeamBG() {
        TeamGameModel[] models = dbHub.getDbHandler().getTeamInfo();
        String[] columnNames = {"BoardGameID", "MinTeamSize"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        int bID, minTeamSize;

        for (TeamGameModel model : models) {

            bID = model.getId();
            minTeamSize= model.getMinTeamSize();

            Object[] row = {bID, minTeamSize};
            tableModel.addRow(row);

        }

        createTableWindow("Team Game: ", tableModel);

    }

    private void showBG() {
        BoardGameModel[] models = dbHub.getDbHandler().getBGInfo();

        String[] columnNames = {"BoardGameID", "Title", "Genre", "Description", "PublisherID"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        String genre, title, description;
        int bID, pID;

        for (BoardGameModel model : models) {

            bID = model.getId();
            pID = model.getPid();
            title = model.getTitle();

            if (model.getGenre() != null) {
                genre = model.getGenre();
            } else {
                genre = " ";
            }

            if (model.getDescription() != null) {
                description = model.getDescription();
            } else {
                description = " ";
            }

            Object[] row = {bID, title, genre, description, pID};
            tableModel.addRow(row);

        }

        createTableWindow("BoardGame", tableModel);

    }

    private void insertBG() {

        AtomicBoolean isTournamentGame = new AtomicBoolean(false);
        AtomicBoolean isTeamGame = new AtomicBoolean(false);

        JTextField bIDField = new JTextField(15);
        JTextField titleField = new JTextField(15);
        JTextField genreField = new JTextField(15);
        JTextField desField = new JTextField(15);
        JTextField pIDField = new JTextField(15);

        JCheckBox isTeamGameCheckBox = new JCheckBox("Is Team Game?");
        JTextField minTeamSizeField = new JTextField((15));
        minTeamSizeField.setEnabled(false);

        JCheckBox isTournamentGameCheckBox = new JCheckBox("Is Tournament Game?");
        JTextField rulesField = new JTextField(15);
        JTextField minParField = new JTextField(15);

        rulesField.setEnabled(false);
        minParField.setEnabled(false);

        JPanel inputPanel = new JPanel(new GridLayout(10,1));

        inputPanel.add(new JLabel("Board Game ID:"));
        inputPanel.add(bIDField);
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Genre:"));
        inputPanel.add(genreField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(desField);
        inputPanel.add(new JLabel("PublisherID:"));
        inputPanel.add(pIDField);

        inputPanel.add(isTeamGameCheckBox);
        inputPanel.add(new JLabel());

        inputPanel.add(isTournamentGameCheckBox);
        inputPanel.add(new JLabel());

        inputPanel.add(new JLabel("Minimum Team Size:"));
        inputPanel.add(minTeamSizeField);

        inputPanel.add(new JLabel("Tournament Rules:"));
        inputPanel.add(rulesField);
        inputPanel.add(new JLabel("Minimum Participants:"));
        inputPanel.add(minParField);

        isTeamGameCheckBox.addActionListener(e -> {
            isTeamGame.set(isTeamGameCheckBox.isSelected());
            minTeamSizeField.setEnabled(isTeamGame.get());
        });

        isTournamentGameCheckBox.addActionListener(e -> {
            isTournamentGame.set(isTournamentGameCheckBox.isSelected());
            minParField.setEnabled(isTournamentGame.get());
            rulesField.setEnabled(isTournamentGame.get());
        });

        int result = JOptionPane.showConfirmDialog(
                null,
                inputPanel,
                "Insert Board Game",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            int bID = fieldToInt(bIDField);

            if (!TextChecker.isInteger(bIDField.getText())) {
                return;
            }

            if(dbHub.getDbHandler().isBoardGameID(bID)){
                JOptionPane.showMessageDialog(null, "Board Game ID exists already!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String title = titleField.getText().trim();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Title cannot be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String genre = genreField.getText().trim();
            String description = desField.getText().trim();

            int pID = fieldToInt(pIDField);

            if (!TextChecker.isInteger(pIDField.getText())) {
                return;
            }

            if(!dbHub.getDbHandler().isPublisherID(pID)){
                JOptionPane.showMessageDialog(null, "Publisher ID does not exist!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (checkSemicolon(title) ||
            checkSemicolon(genre) ||
            checkSemicolon(description)){
                return;
            }

            BoardGameModel bgModel = new BoardGameModel(bID, title, genre, description, pID);

            dbHub.getDbHandler().insertBoardGame(bgModel);
            confirmSuccess("Inserting Board Game");

            if(isTeamGame.get()){
                int minTeamSize = fieldToInt(minTeamSizeField);
                if (!TextChecker.isInteger(minTeamSizeField.getText())) {
                    return;
                }
                TeamGameModel tgModel = new TeamGameModel(bID, minTeamSize);
                dbHub.getDbHandler().insertTeamGame(tgModel);
                confirmSuccess("Inserting Team Game");
            }

            if(isTournamentGame.get()) {
                int minPar = fieldToInt(minParField);
                String rules = rulesField.getText().trim();
                if (checkSemicolon(rules)) {
                    return;
                }
                if (!TextChecker.isInteger(minParField.getText())) {
                    return;
                }
                TournamentGameModel tModel = new TournamentGameModel(bID, rules, minPar);
                dbHub.getDbHandler().insertTournamentGame(tModel);
                confirmSuccess("Inserting Tournament Game");
            }

        }

    }


}
