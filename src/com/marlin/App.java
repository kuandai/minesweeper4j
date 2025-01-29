/**
 * @author Kuan Dai
 * 
 * Swing Application
 */

package com.marlin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Map;
import java.util.HashMap;

public class App {
    final static int gridSize = 15;

    public static void init() {
        // Load images
        Map<String, ImageIcon> images = new HashMap<>();
        images.put("mine", new ImageIcon("assets/mine.png"));
        images.put("flag", new ImageIcon("assets/flag.png"));
        images.put("normal", new ImageIcon("assets/normal.png"));
        images.put("dead", new ImageIcon("assets/dead.png"));
        images.put("0", new ImageIcon("assets/0.png"));
        images.put("1", new ImageIcon("assets/1.png"));
        images.put("2", new ImageIcon("assets/2.png"));
        images.put("3", new ImageIcon("assets/3.png"));
        images.put("4", new ImageIcon("assets/4.png"));
        images.put("5", new ImageIcon("assets/5.png"));
        images.put("6", new ImageIcon("assets/6.png"));
        images.put("7", new ImageIcon("assets/7.png"));
        images.put("8", new ImageIcon("assets/8.png"));
    }

    public static void main(String[] args) {
        // Initialize the application
        init();

        JFrame frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(16 * gridSize, 16 * gridSize);
        frame.setResizable(false);
        frame.setVisible(true);

        // Create the grid panel
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(gridSize, gridSize, 0, 0));

        // Add buttons to the grid
        JButton[][] buttons = new JButton[gridSize][gridSize];
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                button = new JButton();
                button.setIcon(new ImageIcon("assets/normal.png"));
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton button = (JButton) e.getSource();
                        button.setIcon(new ImageIcon("assets/mine.png"));
                    }
                });
                gridPanel.add(button);
            }
        }

        // Add the grid to the frame
        frame.add(gridPanel, BorderLayout.CENTER);

        // Make the frame visible
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }
}