package ca.ubc.cs304.gui.tabs;

import ca.ubc.cs304.gui.*;
import javax.swing.*;
import java.awt.*;
import ca.ubc.cs304.gui.UserSigninWindow;

public class LoginTab extends Tab{

    private static final String displayInt = "Login Screen";
    private JLabel msg;
    public LoginTab(UserSigninWindow window) {
            super(window);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            placeTitle();
        }

    private void placeTitle() {

    }

}
