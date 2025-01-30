/**
 * @author Kuan Dai
 * 
 * Swing Application
 */

package com.marlin;

import com.marlin.MineButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;

class ImageExtractor {
    public Image getImage(String filename) {
        try {
            return ImageIO.read(getClass().getResourceAsStream(
                    "/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public ImageExtractor() {};
}

public class App {

    final static int gridSize = 15;

    public static boolean isDead = false;
    public static MineButton[][] buttons;
    public static Map<String, ImageIcon> images;

    public static void init() {
        // Load images
        ImageExtractor extractor = new ImageExtractor();
        images = new HashMap<>();
        images.put("mine", new ImageIcon(extractor.getImage("assets/mine.png")));
        images.put("flag", new ImageIcon(extractor.getImage("assets/flag.png")));
        images.put("normal", new ImageIcon(extractor.getImage("assets/normal.png")));
        images.put("dead", new ImageIcon(extractor.getImage("assets/dead.png")));
        images.put("0", new ImageIcon(extractor.getImage("assets/0.png")));
        images.put("1", new ImageIcon(extractor.getImage("assets/1.png")));
        images.put("2", new ImageIcon(extractor.getImage("assets/2.png")));
        images.put("3", new ImageIcon(extractor.getImage("assets/3.png")));
        images.put("4", new ImageIcon(extractor.getImage("assets/4.png")));
        images.put("5", new ImageIcon(extractor.getImage("assets/5.png")));
        images.put("6", new ImageIcon(extractor.getImage("assets/6.png")));
        images.put("7", new ImageIcon(extractor.getImage("assets/7.png")));
        images.put("8", new ImageIcon(extractor.getImage("assets/8.png")));
    }

    public static void main(String[] args) {
        // Initialize the application
        init();

        JFrame frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(16 * gridSize, 16 * gridSize);
        frame.setResizable(false);

        // Create the grid panel
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(gridSize, gridSize, 0, 0));

        // Add buttons to the grid
        buttons = new MineButton[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                buttons[i][j] = new MineButton(i, j);
                buttons[i][j].setIcon(images.get("normal"));
                buttons[i][j].addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        MineButton button = (MineButton) e.getSource();
                        if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                            // Handle right click
                            if (!button.isRevealed()) {
                                button.setFlagged(!button.isFlagged());
                                button.setIcon(button.isFlagged() ? images.get("flag") : images.get("normal"));
                            }
                            return;
                        }
                        if (button.isFlagged()) { return; }
                        if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                            if (isDead) { return; }
                            if (button.isMine()) {
                                isDead = true;
                                // Show every mine
                                for (int i = 0; i < gridSize; i++) {
                                    for (int j = 0; j < gridSize; j++) {
                                        if (buttons[i][j].isMine()) {
                                            buttons[i][j].setIcon(images.get("mine"));
                                        }
                                    }
                                }
                                button.setIcon(images.get("dead"));
                                return;
                            }
                            
                            // BFS to reveal the grid
                            Queue<MineButton> queue = new LinkedList<>();
                            queue.add(button);
                            while (!queue.isEmpty()) {
                                MineButton current = queue.poll();
                                int neighbourMines = 0;
                                // Calculate number of neighbouring tiles with mines
                                for (int i = Math.max(0, current.getTileX() - 1); i <= Math.min(gridSize - 1, current.getTileX() + 1); i++) {
                                    for (int j = Math.max(0, current.getTileY() - 1); j <= Math.min(gridSize - 1, current.getTileY() + 1); j++) {
                                        if (buttons[i][j].isMine()) {
                                            neighbourMines++;
                                        }
                                    }
                                }
                                current.setRevealed(true);
                                current.setIcon(images.get(neighbourMines + ""));
                                if (neighbourMines == 0) {
                                    for (int i = -1; i <= 1; i++) {
                                        for (int j = -1; j <= 1; j++) {
                                            if (i == 0 && j == 0) {
                                                continue;
                                            }
                                            int x = current.getTileX() + i;
                                            int y = current.getTileY() + j;
                                            if (x >= 0 && x < gridSize && y >= 0 && y < gridSize) {
                                                MineButton neighbour = buttons[x][y];
                                                if (!neighbour.isRevealed()) {
                                                    neighbour.setRevealed(true);
                                                    queue.add(neighbour);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            // Check if all tiles except mines are revealed
                            int sum = 0;
                            for (int i = 0; i < gridSize; i++) {
                                for (int j = 0; j < gridSize; j++) {
                                    if (!buttons[i][j].isMine() && buttons[i][j].isRevealed()) {
                                        sum++;
                                    }
                                }
                            }
                            if (sum == gridSize*gridSize - (gridSize*gridSize >> 3)) {
                                isDead = true;
                                // Play win sound
                                try {
                                    URL soundURL = App.class.getClassLoader().getResource("assets/tada.wav");
                                    if (soundURL == null) {
                                        throw new RuntimeException("Sound file not found!");
                                    }

                                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
                                    Clip clip = AudioSystem.getClip();
                                    clip.open(audioStream);
                                    clip.start();
                                } catch (Exception ignore) {}
                            }
                        }
                    }
                });
                gridPanel.add(buttons[i][j]);
            }
        }

        // Initialization complete, display the frame
        frame.setVisible(true);

        // Place the mines randomly
        for (int i = 0; i < (int)(gridSize*gridSize >> 3); i++) {
            int x = (int) (Math.random() * gridSize);
            int y = (int) (Math.random() * gridSize);
            buttons[x][y].setMine(true);
        }

        // Add the grid to the frame
        frame.add(gridPanel, BorderLayout.CENTER);

        // Make the frame visible
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }
}