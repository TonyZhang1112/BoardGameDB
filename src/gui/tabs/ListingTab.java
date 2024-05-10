package ca.ubc.cs304.gui.tabs;

import ca.ubc.cs304.controller.DBHub;
import ca.ubc.cs304.gui.UserSigninWindow;
import ca.ubc.cs304.model.ListingModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ListingTab extends Tab{
    private static final String menuTitle = "Listing Menu";
    private DBHub dbHub;
    private JLabel bgMenu;
    private JLabel listingStatus = new JLabel("Status: ");
    private JLabel dateStatus = new JLabel("Status: ");
    private JLabel priceStatus = new JLabel("Status: ");
    private JLabel desStatus = new JLabel("Status: ");
    private JLabel bgStatus = new JLabel("Status: ");
    private JLabel usernameStatus = new JLabel("Status: ");
    private JLabel filterStatus = new JLabel("Status: ");
    
    private int lIDBelow = 0;
    private int dateBelow = 0;
    private int priceBelow = 0;
    private int desBelow = 0;
    private int bgBelow = 0;


    public ListingTab(UserSigninWindow window){
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
        JButton b1 = new JButton("View Listings");
        JButton b2 = new JButton("Filter Listings");
        JButton b3 = new JButton("Insert Listing");


        JPanel buttonRow1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow1.add(b1);
        buttonRow1.add(b2);
        this.add(buttonRow1);

        JPanel buttonRow2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow2.add(b3);
        this.add(buttonRow2);

        b1.addActionListener(e -> showListings());
        b2.addActionListener(e -> filterListings());
        b3.addActionListener(e -> insertListing());


    }

    private void filterListings() {
        JFrame frame = new JFrame("Filter Listings");
        AtomicBoolean isLID = new AtomicBoolean(false);
        AtomicBoolean isDateListed = new AtomicBoolean(false);
        AtomicBoolean isPrice = new AtomicBoolean(false);
        AtomicBoolean isDescription = new AtomicBoolean(false);
        AtomicBoolean isBoardGameID = new AtomicBoolean(false);
        AtomicBoolean isUsername = new AtomicBoolean(false);

        lIDBelow = 0;
        dateBelow = 0;
        priceBelow = 0;
        desBelow = 0;
        bgBelow = 0;


        String[] andOr = {"or","and"};
        String[] priceOp = {"equal","more","less"};
        

        JComboBox<String> listingAO = new JComboBox<>(andOr);
        listingAO.setEnabled(false);

        JComboBox<String> dateListedAO = new JComboBox<>(andOr);
        dateListedAO.setEnabled(false);

        JComboBox<String> priceAO = new JComboBox<>(andOr);
        priceAO.setEnabled(false);

        JComboBox<String> desAO = new JComboBox<>(andOr);
        desAO.setEnabled(false);

        JComboBox<String> bGameAO = new JComboBox<>(andOr);
        bGameAO.setEnabled(false);


        JComboBox<String> priceOpAO = new JComboBox<>(priceOp);
        priceOpAO.setEnabled(false);


        JCheckBox isLIDBox = new JCheckBox("Listing ID");
        JTextField lIDField = new JTextField((15));
        lIDField.setEnabled(false);

        JCheckBox isDateListedBox = new JCheckBox("Date Listed");
        JTextField dayField = new JTextField(3);
        JTextField monthField = new JTextField(3);
        JTextField yearField = new JTextField(5);
        dayField.setEnabled(false);
        monthField.setEnabled(false);
        yearField.setEnabled(false);

        JCheckBox isPriceBox = new JCheckBox("Price");
        JTextField priceField = new JTextField(5);
        priceField.setEnabled(false);


        JCheckBox isDescriptionBox = new JCheckBox("Description");
        JTextField descriptionField = new JTextField((15));
        descriptionField.setEnabled(false);

        JCheckBox isBoardGameIDBox = new JCheckBox("BoardGame ID");
        JTextField boardGameIDField = new JTextField((15));
        boardGameIDField.setEnabled(false);

        JCheckBox isUsernameBox = new JCheckBox("Username");
        JTextField usernameField = new JTextField((15));
        usernameField.setEnabled(false);

        JPanel filterArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton filter = new JButton("Filter");
        filter.setBackground(Color.GREEN);
        filterArea.add(filter);
        filterArea.add(filterStatus);

        JPanel inputPanel = new JPanel(new GridLayout(7,1));

        JPanel listingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        listingPanel.add(isLIDBox);
        listingPanel.add(listingAO);
        listingPanel.add(lIDField);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(isDateListedBox);
        datePanel.add(dateListedAO);
        datePanel.add(new JLabel("DD:"));
        datePanel.add(dayField);
        datePanel.add(new JLabel("MM:"));
        datePanel.add(monthField);
        datePanel.add(new JLabel("YYYY:"));
        datePanel.add(yearField);

        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pricePanel.add(isPriceBox);
        pricePanel.add(priceAO);
        pricePanel.add(priceOpAO);
        pricePanel.add(new JLabel("Amount:"));
        pricePanel.add(priceField);

        JPanel desPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        desPanel.add(isDescriptionBox);
        desPanel.add(desAO);
        desPanel.add(descriptionField);

        JPanel bGPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bGPanel.add(isBoardGameIDBox);
        bGPanel.add(bGameAO);
        bGPanel.add(boardGameIDField);

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(isUsernameBox);
        namePanel.add(usernameField);

        inputPanel.add(listingPanel);
        inputPanel.add(listingStatus);
        inputPanel.add(datePanel);
        inputPanel.add(dateStatus);
        inputPanel.add(pricePanel);
        inputPanel.add(priceStatus);
        inputPanel.add(desPanel);
        inputPanel.add(desStatus);
        inputPanel.add(bGPanel);
        inputPanel.add(bgStatus);
        inputPanel.add(namePanel);
        inputPanel.add(usernameStatus);

        inputPanel.add(filterArea);

        frame.add(inputPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        isLIDBox.addActionListener(e -> {
            isLID.set(isLIDBox.isSelected());
            lIDField.setEnabled(isLID.get());
            listingAO.setEnabled(isLIDBox.isSelected() && lIDBelow > 0);
            listingAO.setSelectedIndex(0);
        });
        
        isDateListedBox.addActionListener(e -> {
            isDateListed.set(isDateListedBox.isSelected());
            dayField.setEnabled(isDateListed.get());
            monthField.setEnabled(isDateListed.get());
            yearField.setEnabled(isDateListed.get());

            if(isDateListedBox.isSelected()){
                lIDBelow++;
                listingAO.setEnabled(isLIDBox.isSelected());
            } else{
                lIDBelow--;
                if(lIDBelow == 0){
                    listingAO.setEnabled(false);
                    listingAO.setSelectedIndex(0);
                }

            }
            dateListedAO.setEnabled(isDateListedBox.isSelected() && dateBelow > 0);

            dateListedAO.setSelectedIndex(0);
        });
        isPriceBox.addActionListener(e -> {
            isPrice.set(isPriceBox.isSelected());
            priceField.setEnabled(isPrice.get());

            if(isPriceBox.isSelected()){
                lIDBelow++;
                dateBelow++;
                listingAO.setEnabled(isLIDBox.isSelected());
                dateListedAO.setEnabled(isDateListedBox.isSelected());
            } else{
                lIDBelow--;
                dateBelow--;
                if(lIDBelow == 0){
                    listingAO.setEnabled(false);
                    listingAO.setSelectedIndex(0);
                }
                if(dateBelow == 0){
                    dateListedAO.setEnabled(false);
                    dateListedAO.setSelectedIndex(0);
                }

            }
            priceAO.setEnabled(isPriceBox.isSelected() && priceBelow > 0);

            priceOpAO.setEnabled(isPrice.get());
            priceOpAO.setSelectedIndex(0);
            priceAO.setSelectedIndex(0);
        });
        isDescriptionBox.addActionListener(e -> {
            isDescription.set(isDescriptionBox.isSelected());
            descriptionField.setEnabled(isDescription.get());

            if(isDescriptionBox.isSelected()){
                lIDBelow++;
                dateBelow++;
                priceBelow++;
                listingAO.setEnabled(isLIDBox.isSelected());
                dateListedAO.setEnabled(isDateListedBox.isSelected());
                priceAO.setEnabled((isPriceBox.isSelected()));
            } else{
                lIDBelow--;
                dateBelow--;
                priceBelow--;
                if(lIDBelow == 0){
                    listingAO.setEnabled(false);
                    listingAO.setSelectedIndex(0);
                }
                if(dateBelow == 0){
                    dateListedAO.setEnabled(false);
                    dateListedAO.setSelectedIndex(0);
                }
                if(priceBelow == 0){
                    priceAO.setEnabled(false);
                    priceAO.setSelectedIndex(0);
                }
            }
            desAO.setEnabled(isDescriptionBox.isSelected() && desBelow >0);

            desAO.setSelectedIndex(0);
        });
        isBoardGameIDBox.addActionListener(e -> {
            isBoardGameID.set(isBoardGameIDBox.isSelected());
            boardGameIDField.setEnabled(isBoardGameID.get());

            if(isBoardGameIDBox.isSelected()){
                lIDBelow++;
                dateBelow++;
                priceBelow++;
                desBelow++;
                listingAO.setEnabled(isLIDBox.isSelected());
                dateListedAO.setEnabled(isDateListedBox.isSelected());
                priceAO.setEnabled((isPriceBox.isSelected()));
                desAO.setEnabled(isDescriptionBox.isSelected());
            } else{
                lIDBelow--;
                dateBelow--;
                priceBelow--;
                desBelow--;
                if(lIDBelow == 0){
                    listingAO.setEnabled(false);
                    listingAO.setSelectedIndex(0);
                }
                if(dateBelow == 0){
                    dateListedAO.setEnabled(false);
                    dateListedAO.setSelectedIndex(0);
                }
                if(priceBelow == 0){
                    priceAO.setEnabled(false);
                    priceAO.setSelectedIndex(0);
                }
                if (desBelow == 0){
                    desAO.setEnabled(false);
                    desAO.setSelectedIndex(0);
                }
            }
            bGameAO.setEnabled(isBoardGameIDBox.isSelected() && bgBelow >0);

            bGameAO.setSelectedIndex(0);
        });
        isUsernameBox.addActionListener(e -> {
            isUsername.set(isUsernameBox.isSelected());

            if(isUsernameBox.isSelected()){
                lIDBelow++;
                dateBelow++;
                priceBelow++;
                desBelow++;
                bgBelow++;
                listingAO.setEnabled(isLIDBox.isSelected());
                dateListedAO.setEnabled(isDateListedBox.isSelected());
                priceAO.setEnabled((isPriceBox.isSelected()));
                desAO.setEnabled(isDescriptionBox.isSelected());
                bGameAO.setEnabled(isBoardGameIDBox.isSelected());
            } else{
                lIDBelow--;
                dateBelow--;
                priceBelow--;
                desBelow--;
                bgBelow--;
                if(lIDBelow == 0){
                    listingAO.setEnabled(false);
                    listingAO.setSelectedIndex(0);
                }
                if(dateBelow == 0){
                    dateListedAO.setEnabled(false);
                    dateListedAO.setSelectedIndex(0);
                }
                if(priceBelow == 0){
                    priceAO.setEnabled(false);
                    priceAO.setSelectedIndex(0);
                }
                if (desBelow == 0){
                    desAO.setEnabled(false);
                    desAO.setSelectedIndex(0);
                }
                if(bgBelow == 0){
                    bGameAO.setEnabled(false);
                    bGameAO.setSelectedIndex(0);
                }
            }
            usernameField.setEnabled(isUsername.get());
        });
        filter.addActionListener(e->{
            Map<String,Object> attributeMap = new LinkedHashMap<>();
            ArrayList<String> cState = new ArrayList<>();
            String priceOperator = "";
            boolean failure = false;
            if(isLIDBox.isSelected()){
                if (checkSemicolonNoError(lIDField)){
                    listingStatus.setText("Status: Can't have an ; in your input");
                    failure = true;
                } else if(!fieldToIntCheck(lIDField)){
                    listingStatus.setText("Status: Integers only please");
                    failure = true;
                } else{
                    int val = fieldToIntGuaranteed(lIDField);
                    if(val<0){
                        listingStatus.setText("Status: Input can't be a negative number!");
                        failure = true;
                    } else {
                        listingStatus.setText("Status: Good Input");
                        if(listingAO.isEnabled()){
                            cState.add(listingAO.getItemAt(listingAO.getSelectedIndex()));
                        }
                        attributeMap.put("lid", val);
                    }
                }
            }
            if(isDateListedBox.isSelected()){
                if (checkSemicolonNoError(dayField) || checkSemicolonNoError(monthField) || checkSemicolonNoError(yearField)){
                    dateStatus.setText("Status: Can't have an ; in your input");
                    failure = true;
                } else if(!fieldToIntCheck(dayField) || !fieldToIntCheck(monthField) || !fieldToIntCheck(yearField)) {
                    dateStatus.setText("Status: Integers only please");
                    failure = true;
                } else{
                    int day = fieldToIntGuaranteed(dayField);
                    int month = fieldToIntGuaranteed(monthField);
                    int year = fieldToIntGuaranteed(yearField);
                    if(day<1 || day>31){
                        dateStatus.setText("Status: Day value out of bounds! 1-31 only");
                        failure = true;
                    } else if (month < 1 || month >12 ){
                        dateStatus.setText("Status: Month value out of bounds! 1-12 only");
                        failure = true;
                    } else if (year < 1000 || year > 9999){
                        dateStatus.setText("Status: Year value out of bounds! 1000-9999 only");
                        failure = true;
                    } else{
                        dateStatus.setText("Status: Good Input");
                        String dayS = "" + day;
                        String monthS = "" + month;
                        ArrayList<Integer> singleDigits = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
                        if (singleDigits.contains(day)){
                            dayS = "0"+ dayS;
                        }
                        if (singleDigits.contains(month)){
                            monthS = "0" + monthS;
                        }
                        String strDate = year + "-" + monthS + "-" + dayS;
                        if(dateListedAO.isEnabled()){
                            cState.add(dateListedAO.getItemAt(dateListedAO.getSelectedIndex()));
                        }
                        Date date = Date.valueOf(strDate);
                        attributeMap.put("dateListed",date);

                    }
                }
            }
            if(isPriceBox.isSelected()){
                if (checkSemicolonNoError(priceField)){
                    priceStatus.setText("Status: Can't have an ; in your input");
                    failure = true;
                } else if(!fieldToDoubleCheck(priceField)){
                    priceStatus.setText("Status: Numbers only please");
                    failure = true;
                } else {
                    double val = fieldToDoubleGuaranteed(priceField);
                    if (val < 0) {
                        priceStatus.setText("Status: Input can't be a negative number!");
                        failure = true;
                    } else {
                        priceStatus.setText("Status: Good Input");
                        if(priceAO.isEnabled()){
                            cState.add(priceAO.getItemAt(priceAO.getSelectedIndex()));
                        }
                        priceOperator = priceOpAO.getItemAt(priceOpAO.getSelectedIndex());
                        attributeMap.put("price", val);
                    }
                }
            }
            if(isDescriptionBox.isSelected()){
                if (checkSemicolonNoError(descriptionField)) {
                    desStatus.setText("Status: Can't have an ; in your input");
                    failure = true;
                } else{
                    String text = descriptionField.getText().trim();
                    desStatus.setText("Status: Good Input");
                    if(desAO.isEnabled()){
                        cState.add(desAO.getItemAt(desAO.getSelectedIndex()));
                    }
                    attributeMap.put("description", text);
                }
            }
            if(isBoardGameIDBox.isSelected()){
                if (checkSemicolonNoError(boardGameIDField)){
                    bgStatus.setText("Status: Can't have an ; in your input");
                    failure = true;
                } else if(!fieldToIntCheck(boardGameIDField)){
                    bgStatus.setText("Status: Integers only please");
                    failure = true;
                } else{
                    int val = fieldToIntGuaranteed(boardGameIDField);
                    if(val<0){
                        bgStatus.setText("Status: Input can't be a negative number!");
                        failure = true;
                    } else {
                        bgStatus.setText("Status: Good Input");
                        if(bGameAO.isEnabled()){
                            cState.add(bGameAO.getItemAt(bGameAO.getSelectedIndex()));
                        }
                        attributeMap.put("boardGameID", val);
                    }
                }
            }
            if(isUsernameBox.isSelected()){
                if (checkSemicolonNoError(usernameField)) {
                    usernameStatus.setText("Status: Can't have an ; in your input");
                    failure = true;
                } else{
                    String text = usernameField.getText().trim();
                    usernameStatus.setText("Status: Good Input");
                    attributeMap.put("username", text);
                }
            }
            if(!attributeMap.isEmpty() && !failure){
                showFilteredListings(attributeMap,cState,priceOperator);
                filterStatus.setText("Status: Successfully filtered!");
            }
            if(attributeMap.isEmpty() && !failure){
                filterStatus.setText("Status: Can't filter on nothing!");
                listingStatus.setText("Status: ");
                dateStatus.setText("Status: ");
                priceStatus.setText("Status: ");
                desStatus.setText("Status: ");
                bgStatus.setText("Status: ");
                usernameStatus.setText("Status: ");
            }
            if(failure){
                filterStatus.setText("Status: Errors Found");
            }
//            int i = 0;
//            for (Map.Entry<String, Object> entry :
//                    attributeMap.entrySet()) {
//
//                // Printing keys
//
//                System.out.print(entry.getKey() + ": ");
//                if(Objects.equals(entry.getKey(), "price")){
//                    System.out.print(priceOperator);
//                }
//                System.out.println(entry.getValue());
//                if(i<attributeMap.size()-1){
//                    System.out.println(cState.get(i));
//                }
//                i++;
//            }
        });
    }
    private void showFilteredListings(Map<String, Object> attributeMap, ArrayList<String> cState, String priceOperator){
        ListingModel[] models = dbHub.getDbHandler().filterListing2(attributeMap,cState,priceOperator);
        String[] columnNames = {"ListingID", "Date Listed","Price","Description","BoardGameID","Username"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        String description, username;
        int lID,bID;
        Date dateListed;
        BigDecimal price;

        for (ListingModel model : models) {

            lID= model.getLid();
            bID = model.getBoardGameID();
            username = model.getUsername();
            dateListed = model.getDateListed();
            price = model.getPrice();

            if (model.getDescription() != null) {
                description = model.getDescription();
            } else {
                description = " ";
            }


            Object[] row = {lID, dateListed,price,description,bID,username};
            tableModel.addRow(row);

        }

        createTableWindow("Filtered Listings", tableModel);
    }


    private void showListings() {

        ListingModel[] models = dbHub.getDbHandler().getListingInfo();

        String[] columnNames = {"ListingID", "Date Listed","Price","Description","BoardGameID","Username"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        String description, username;
        int lID,bID;
        Date dateListed;
        BigDecimal price;

        for (ListingModel model : models) {

            lID= model.getLid();
            bID = model.getBoardGameID();
            username = model.getUsername();
            dateListed = model.getDateListed();
            price = model.getPrice();

            if (model.getDescription() != null) {
                description = model.getDescription();
            } else {
                description = " ";
            }


            Object[] row = {lID, dateListed,price,description,bID,username};
            tableModel.addRow(row);

        }
        createTableWindow("Listings", tableModel);
    }

    private void insertListing() {

        JTextField priceField = new JTextField(15);
        JTextField desField = new JTextField(15);
        JTextField bgIDField = new JTextField(15);
        JTextField usernameField = new JTextField(15);
        JTextField dateField = new JTextField(15);

        JPanel inputPanel = new JPanel(new GridLayout(10,1));

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Board Game ID:"));
        inputPanel.add(bgIDField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(desField);
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        inputPanel.add(dateField);

        int result = JOptionPane.showConfirmDialog(
                null,
                inputPanel,
                "Insert Listing",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            String description = desField.getText().trim();
            String listingDateStr = dateField.getText().trim();
            String username = usernameField.getText().trim();
            String priceStr = priceField.getText().trim();
            String bgIDStr = bgIDField.getText().trim();

            if (checkSemicolon(description) || checkSemicolon(username) ||
                    checkSemicolon(listingDateStr) || checkSemicolon(priceStr) || checkSemicolon(bgIDStr)) {
                return;
            }

            int bgID = fieldToInt(bgIDField);
            int listingID = dbHub.getDbHandler().countListingID() + 1;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setLenient(false);

            if(!dbHub.getDbHandler().isBoardGameID(bgID)){
                JOptionPane.showMessageDialog(null, "Board Game ID does not exist!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!dbHub.getDbHandler().isUserName(username)) {
                JOptionPane.showMessageDialog(null,
                        "Creator name " + username + " does not exist!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (listingDateStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Listing Date cannot be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Price cannot be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (bgIDStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Board Game ID cannot be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(null, "User Name cannot be set to null!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BigDecimal price = fieldToBD(priceField);

            Date listingDate = null;

            try {
                listingDate = new Date(simpleDateFormat.parse(listingDateStr).getTime());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid Date Format Detected",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ListingModel listingModel = new ListingModel(listingID, listingDate, price, description, bgID, username);
            dbHub.getDbHandler().insertListing(listingModel);
            confirmSuccess("Inserting Listing " + Integer.toString(listingID));
        }

    }
}
