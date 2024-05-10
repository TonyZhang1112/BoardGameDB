package ca.ubc.cs304.gui.tabs;


import ca.ubc.cs304.controller.DBHub;
import ca.ubc.cs304.gui.UserSigninWindow;
import ca.ubc.cs304.model.BGPModel;
import ca.ubc.cs304.model.PublisherModel;
import ca.ubc.cs304.model.ReviewsModel;
import ca.ubc.cs304.util.TextChecker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PublisherTab extends Tab {
    private static final String menuTitle = "Publisher Menu";
    private DBHub dbHub;
    private JLabel bgMenu;


    public PublisherTab(UserSigninWindow window){
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
        JButton b1 = new JButton("View Publishers");
        JButton b2 = new JButton("Update Publishers");
        JButton b3 = new JButton("Show Publishers with at least X board games published");
        JButton b4 = new JButton("Show Board Games From Specific Publisher");
        JButton b5 = new JButton("Get average board game score for a publisher");


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
        this.add(buttonRow3);


        b1.addActionListener(e -> showPublishers());
        b2.addActionListener(e -> updatePublishers());
        b3.addActionListener(e -> showPubX());
        b4.addActionListener(e -> showBBG());
        b5.addActionListener(e -> showAvg());
    }

    private void showAvg() {
        JTextField idField = new JTextField(15);
        JPanel inputPanel = new JPanel(new GridLayout(1,1));
        inputPanel.add(new JLabel("Publisher ID to look for:"));
        inputPanel.add(idField);

        int result = JOptionPane.showConfirmDialog(
                null,
                inputPanel,
                "Get average board game score for a publisher",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            int pID = fieldToInt(idField);
            String checkS = idField.getText().trim();
            if (checkSemicolon(checkS)){
                return;
            }

            if(!dbHub.getDbHandler().isPublisherID(pID)){
                JOptionPane.showMessageDialog(null, "This Publisher ID doesn't exist!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ReviewsModel[] models = dbHub.getDbHandler().averageScoreBG(pID);
            String[] columnNames = {"Publisher ID","Average Score"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            int publisherID, avgScore;

            for (ReviewsModel model : models) {
                if (model.getBoardGameId() == 0) {
                    JOptionPane.showMessageDialog(null, "The Publisher ID you inputted does not have any ratings yet",
                            "No ratings :(", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                publisherID = model.getBoardGameId();
                avgScore = model.getScore();
                Object[] row = {publisherID, avgScore};
                tableModel.addRow(row);


            }
            createTableWindow("Average Score for Publishers", tableModel);

        }
    }

    private void showBBG() {
        JTextField nameField = new JTextField(15);
        JPanel inputPanel = new JPanel(new GridLayout(1,1));
        inputPanel.add(new JLabel("The publisher name to check for:"));
        inputPanel.add(nameField);

        int result = JOptionPane.showConfirmDialog(
                null,
                inputPanel,
                "Show board games from publishers",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );


        if (result == JOptionPane.OK_OPTION) {

            String pName = nameField.getText().trim();

            if (checkSemicolon(pName)){
                return;
            }

            if (pName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Publisher Name can't be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            } else if(!dbHub.getDbHandler().isPublisherName(pName)){
                JOptionPane.showMessageDialog(null, "This Publisher Name doesn't exist!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BGPModel[] models = dbHub.getDbHandler().selectBGP(pName);
            String[] columnNames = {"Title","Genre"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            String publisherName = "", title, genre;

            for (BGPModel model : models) {

                publisherName = model.getpublisherName();
                title = model.getTitle();
                genre = model.getGenre();


                Object[] row = {title,genre};
                tableModel.addRow(row);

            }
            createTableWindow("Games By " + publisherName, tableModel);

        }
    }

    private void showPubX() {
        JTextField countField = new JTextField(15);
        JPanel inputPanel = new JPanel(new GridLayout(1,1));
        inputPanel.add(new JLabel("Minimum Board Games Published Required:"));
        inputPanel.add(countField);

        int result = JOptionPane.showConfirmDialog(
                null,
                inputPanel,
                "Show Publishers with at least X Board Games",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            int count = fieldToInt(countField);

            String checkS = countField.getText().trim();

            if (checkSemicolon(checkS)){
                return;
            }
            if (!TextChecker.isInteger(countField.getText())) {
                return;
            }

            ArrayList<Object> items = dbHub.getDbHandler().showPubGx2(count);
            String[] columnNames = {"Publisher Name", "Website","Game Count"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            for (int i = 1;i<items.size()-1;i+=3){
                Object[] row = {items.get(i-1), items.get(i),items.get(i+1)};
                tableModel.addRow(row);
            }
            createTableWindow("Publishers", tableModel);

        }
    }

    private void updatePublishers() {

        JTextField idField = new JTextField(15);
        JTextField nameField = new JTextField(30);
        JTextField websiteField = new JTextField(30);

        JPanel inputPanel = new JPanel(new GridLayout(3,1));

        inputPanel.add(new JLabel("Publisher ID to update:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("New Publisher Name Value:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("New Website Value:"));
        inputPanel.add(websiteField);

        int result = JOptionPane.showConfirmDialog(
                null,
                inputPanel,
                "Update Publishers",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            int pID = fieldToInt(idField);

            if(!dbHub.getDbHandler().isPublisherID(pID)){
                JOptionPane.showMessageDialog(null, "This Publisher ID doesn't exist!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String pName = nameField.getText().trim();
            if (checkSemicolon(pName)) {
                return;
            }

            if (pName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Publisher Name can't be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            } else if(dbHub.getDbHandler().isPublisherName(pName)){
                JOptionPane.showMessageDialog(null, "This Publisher Name already exists!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String wValue= websiteField.getText().trim();

            if (checkSemicolon(wValue) ||
                    checkSemicolon(pName)){
                return;
            }

            dbHub.getDbHandler().updatePublisher(pID,pName,wValue);
            confirmSuccess("Update Publisher ID " + pID);
        }
    }

    private void showPublishers() {
        PublisherModel[] models = dbHub.getDbHandler().getPublisherInfo();

        String[] columnNames = {"PublisherID", "Publisher Name","Website"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        String publisherName, website;
        int pID;

        for (PublisherModel model : models) {

            pID = model.getId();
            publisherName = model.getpublisherName();

            if (model.getWebsite() != null) {
                website = model.getWebsite();
            } else {
                website = " ";
            }


            Object[] row = {pID, publisherName,website};
            tableModel.addRow(row);

        }
        createTableWindow("Publishers", tableModel);

    }

}
