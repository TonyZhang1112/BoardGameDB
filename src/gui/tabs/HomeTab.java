package ca.ubc.cs304.gui.tabs;

import ca.ubc.cs304.gui.UserSigninWindow;
import ca.ubc.cs304.gui.tabs.Tab;

import javax.swing.*;
import java.awt.*;

public class HomeTab extends Tab {

    private JLabel greeting;
    private JLabel space;
    private static final String displayMsg = "Welcome to Board Game DB!";
    private static final String blank = "     ";

    public HomeTab(UserSigninWindow controller) {
        super(controller);
        setLayout(new GridBagLayout());

        placeGreeting();
        placeImage();
    }

    private void placeGreeting() {
        greeting = new JLabel(displayMsg, JLabel.CENTER);
        greeting.setSize(WIDTH, HEIGHT );
        greeting.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
        this.add(greeting);

        space = new JLabel(blank, JLabel.CENTER);
        space.setSize(WIDTH, HEIGHT );
        space.setFont(new Font("Verdana",1,20));
        this.add(space);
    }

    private void placeImage() {
        ImageIcon imageIcon = new ImageIcon("./data/img/title.jpg");
        Image image = imageIcon.getImage().getScaledInstance(640, 360, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setAlignmentX(Component.BOTTOM_ALIGNMENT);
        this.add(imageLabel);
    }
}
