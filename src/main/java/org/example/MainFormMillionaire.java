package org.example;

import java.awt.event.*;
import javax.swing.*;

public class MainFormMillionaire {

    private JButton showMessageButton;
    private JPanel mainPanel;


    public MainFormMillionaire() {
        showMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "You clicked it");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Who Wants to be a Millionaire");

        MainFormMillionaire form = new MainFormMillionaire();
        frame.setContentPane(form.mainPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
