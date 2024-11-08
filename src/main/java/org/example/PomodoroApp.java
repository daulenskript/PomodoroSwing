package org.example;


import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class PomodoroApp extends JFrame {
    private Timer timer;
    private int timeLeft = 25 * 60; // 25 minutes in seconds
    private JLabel timeLabel, sessionCountLabel, statsLabel;
    private JButton startButton, resetButton;
    private JComboBox<String> moodSelector;
    private ArrayList<PomodoroSession> sessions;
    private boolean isRunning = false;

    public PomodoroApp() {
        sessions = new ArrayList<>();
        setupUI();
        setupTimer();
    }

    private void setupUI() {
        setTitle("Pomodoro Timer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Timer panel
        JPanel timerPanel = new JPanel();
        timeLabel = new JLabel("25:00");
        timeLabel.setFont(new Font("Roboto", Font.BOLD, 48));
        timerPanel.add(timeLabel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        startButton = new JButton("Start");
        resetButton = new JButton("Reset");

        // Style buttons
        styleButton(startButton);
        styleButton(resetButton);

        startButton.addActionListener(e -> toggleTimer());
        resetButton.addActionListener(e -> resetTimer());

        buttonPanel.add(startButton);
        buttonPanel.add(resetButton);

        // Mood selector panel
        JPanel moodPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        String[] moods = {"😊 Productive", "😐 Neutral", "😓 Distracted"};
        moodSelector = new JComboBox<>(moods);
        moodSelector.setPreferredSize(new Dimension(150, 25));
        moodPanel.add(new JLabel("Current Mood: "));
        moodPanel.add(moodSelector);

        // Stats panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        sessionCountLabel = new JLabel("Total Sessions: 0");
        statsLabel = new JLabel("Last Session: N/A");
        sessionCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        statsPanel.add(sessionCountLabel);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        statsPanel.add(statsLabel);

        // Add all panels to main panel
        mainPanel.add(timerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(moodPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(statsPanel);

        add(mainPanel);
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(100, 30));
        button.setFocusPainted(false);
        button.setFont(new Font("Roboto", Font.PLAIN, 14));
    }

    private void setupTimer() {
        timer = new Timer(1000, e -> {
            timeLeft--;
            updateTimeLabel();

            if (timeLeft <= 0) {
                timerComplete();
            }
        });
    }

    private void toggleTimer() {
        if (isRunning) {
            timer.stop();
            startButton.setText("Resume");
        } else {
            timer.start();
            startButton.setText("Pause");
        }
        isRunning = !isRunning;
    }

    private void resetTimer() {
        timer.stop();
        timeLeft = 25 * 60;
        updateTimeLabel();
        startButton.setText("Start");
        isRunning = false;
    }

    private void updateTimeLabel() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void updateStats() {
        sessionCountLabel.setText("Total Sessions: " + sessions.size());
        if (!sessions.isEmpty()) {
            PomodoroSession lastSession = sessions.get(sessions.size() - 1);
            String lastSessionTime = lastSession.getDateTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm"));
            statsLabel.setText("Last Session: " + lastSessionTime + " - " + lastSession.getMood());
        }
    }

    private void timerComplete() {
        timer.stop();
        isRunning = false;
        String selectedMood = (String) moodSelector.getSelectedItem();
        sessions.add(new PomodoroSession(
                LocalDateTime.now(),
                25 - (timeLeft / 60),
                selectedMood
        ));

        updateStats();

        // Show completion dialog
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Pomodoro complete! Start another session?",
                "Session Complete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            resetTimer();
            startButton.setText("Start");
        }
    }


}

