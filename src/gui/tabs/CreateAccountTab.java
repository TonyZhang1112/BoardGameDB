package ca.ubc.cs304.gui.tabs;

import ca.ubc.cs304.controller.DBHub;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.gui.*;
import ca.ubc.cs304.model.UserModel;
import ca.ubc.cs304.util.TextChecker;

import javax.swing.*;
import java.awt.*;


public class CreateAccountTab extends Tab{
    private static final String menuTitle = "Create Account";
    private DBHub dbHub;
    private JLabel createAccount;
    private DatabaseConnectionHandler dbHandler = null;

    private JTextField usernameTextBox;
    private JTextField passwordTextBox;
    private JTextField emailTextBox;
    private JTextField wishlistIdTextBox;
    private JTextField addressTextBox;
    private JTextField postalCodeTextBox;
    private JTextField countryTextBox;
    private JLabel statusText;

    public CreateAccountTab(UserSigninWindow window) {
        super(window);
        dbHub = window.getDbHub();
        dbHandler = dbHub.getDbHandler();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initTitle();
        initOptions();
        initButton();
        initStatusText();
    }

    private void initTitle() {
        createAccount = new JLabel(menuTitle, JLabel.CENTER);
        createAccount.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(createAccount);
    }

    private void initOptions() {
        JPanel warning = new JPanel(new FlowLayout(FlowLayout.LEFT));
        warning.add(new JLabel("Note: All fields are required."));
        this.add(warning);

        JPanel usernameRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernameTextBox = new JTextField(20);
        usernameRow.add(new JLabel("Username (Must not already be used):"));
        usernameRow.add(usernameTextBox);
        this.add(usernameRow);

        JPanel passwordRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordTextBox = new JTextField(20);
        passwordRow.add(new JLabel("Password"));
        passwordRow.add(passwordTextBox);
        this.add(passwordRow);

        JPanel emailRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailTextBox = new JTextField(20);
        emailRow.add(new JLabel("Email (Must not already be used):"));
        emailRow.add(emailTextBox);
        this.add(emailRow);

        JPanel wishlistIdRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wishlistIdTextBox = new JTextField(20);
        wishlistIdRow.add(new JLabel("Wishlist ID (Must not already be used):"));
        wishlistIdRow.add(wishlistIdTextBox);
        this.add(wishlistIdRow);

        JPanel addressRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addressTextBox = new JTextField(20);
        addressRow.add(new JLabel("Address:"));
        addressRow.add(addressTextBox);
        this.add(addressRow);

        JPanel postalCodeRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        postalCodeTextBox = new JTextField(20);
        postalCodeRow.add(new JLabel("Postal Code:"));
        postalCodeRow.add(postalCodeTextBox);
        this.add(postalCodeRow);

        JPanel countryRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        countryTextBox = new JTextField(20);
        countryRow.add(new JLabel("Country:"));
        countryRow.add(countryTextBox);
        this.add(countryRow);
    }

    private void initButton() {
        JButton createAccountButton = new JButton("Create Account!");
        createAccountButton.setBackground(Color.GREEN);
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));

        buttonRow.add(createAccountButton);
        this.add(buttonRow);

        createAccountButton.addActionListener(e -> insertUser());
    }

    private void initStatusText() {
        JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusText = new JLabel("");
        statusRow.add(statusText);
        this.add(statusRow);
    }

    private void insertUser() {
        String username = usernameTextBox.getText().trim();
        String password = passwordTextBox.getText().trim();
        String email = emailTextBox.getText().trim();
        String wishlistId = wishlistIdTextBox.getText().trim();
        String address = addressTextBox.getText().trim();
        String postalCode = postalCodeTextBox.getText().trim();
        String country = countryTextBox.getText().trim();

        if (TextChecker.isEmptyAny(new String[]{username, password, email, wishlistId, address, postalCode, country})) {
            statusText.setText("All fields must be filled!");
            return;
        }
        if (TextChecker.hasSemicolonAny(new String[]{username, password, email, wishlistId, address, postalCode,
                country})) {
            statusText.setText("At least one input contains a semicolon, which we do not allow.");
            return;
        }
        if (!TextChecker.isInteger(wishlistId)) {
            statusText.setText("The Wishlist ID must be an integer!");
            return;
        }

        UserModel user = new UserModel(username, password, email, Integer.parseInt(wishlistId), address,
                postalCode, country);

        int result = dbHandler.insertUser(user);

        if (result == 0) {
            statusText.setText("Your chosen Username, Email, or Wishlist ID is already in use.");
        } else {
            usernameTextBox.setText("");
            passwordTextBox.setText("");
            emailTextBox.setText("");
            wishlistIdTextBox.setText("");
            addressTextBox.setText("");
            postalCodeTextBox.setText("");
            countryTextBox.setText("");
            if (result == 1) {
                statusText.setText("Account " + username + " has been successfully created! WOOOO!");
            } else {
                statusText.setText("Account " + username + " has been successfully created! Your country may have been " +
                        "corrected based off your address and postal code.");
            }
        }
    }
}
