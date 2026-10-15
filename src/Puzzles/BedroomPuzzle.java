package Puzzles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class BedroomPuzzle extends JFrame {
    private Runnable puzzleCompleteListener;

    private final Color backgroundColor = new Color(25, 20, 35);
    private final Color normalColor = new Color(75, 52, 90);
    private final Color activeColor = new Color(205, 155, 255);
    private final Color borderColor = new Color(155, 115, 180);

    private final String[] symbols = {
        "🛏️", "🧸", "⏰", "🪞"
    };

    private final JButton[] buttons = new JButton[4];
    private final ArrayList<Integer> sequence = new ArrayList<>();
    private final Random random = new Random();

    private JLabel instructionLabel;
    private JLabel roundLabel;

    private int round = 1;
    private int playerIndex = 0;
    private int playbackStep = 0;

    private boolean showingSequence = false;
    private Timer playbackTimer;

    public BedroomPuzzle() {
        setTitle("Bedroom Puzzle");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );

        JLabel titleLabel = new JLabel(
            "👻 Break the Bedroom Curse 👻",
            SwingConstants.CENTER
        );
        titleLabel.setForeground(new Color(235, 215, 250));
        titleLabel.setFont(new Font("Serif", Font.BOLD, 21));

        roundLabel = new JLabel(
            "Round 1 of 3",
            SwingConstants.CENTER
        );
        roundLabel.setForeground(new Color(195, 170, 215));

        instructionLabel = new JLabel(
            "<html><center>Look closely...<br>" +
            "the room remembers.</center></html>",
            SwingConstants.CENTER
        );
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setFont(new Font("Serif", Font.PLAIN, 14));

        JPanel topPanel = new JPanel(new GridLayout(3, 1, 2, 2));
        topPanel.setOpaque(false);
        topPanel.add(titleLabel);
        topPanel.add(roundLabel);
        topPanel.add(instructionLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setOpaque(false);

        for (int i = 0; i < buttons.length; i++) {
            final int symbolIndex = i;

            JButton button = new JButton(symbols[i]);
            button.setFont(new Font("Serif", Font.PLAIN, 46));
            button.setForeground(Color.WHITE);
            button.setBackground(normalColor);
            button.setOpaque(true);
            button.setFocusPainted(false);
            button.setBorder(
                BorderFactory.createLineBorder(borderColor, 2)
            );

            button.addActionListener(e -> {
            // Make the clicked object glow
            button.setBackground(activeColor);
            button.setForeground(Color.BLACK);

            Timer clickGlowTimer = new Timer(250, event -> {
                button.setBackground(normalColor);
                button.setForeground(Color.WHITE);
                ((Timer) event.getSource()).stop();
            });

            clickGlowTimer.setRepeats(false);
            clickGlowTimer.start();

            handlePlayerInput(symbolIndex);
        });

            buttons[i] = button;
            buttonPanel.add(button);
        }

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);

        startRound();
        setVisible(true);
    }

    public void setPuzzleCompleteListener(Runnable listener) {
        this.puzzleCompleteListener = listener;
    }

    private void startRound() {
        sequence.clear();
        playerIndex = 0;

        int sequenceLength = round + 2;

        for (int i = 0; i < sequenceLength; i++) {
            sequence.add(random.nextInt(4));
        }

        roundLabel.setText("Round " + round + " of 3");
        instructionLabel.setText(
            "<html><center>Look closely...<br>" +
            "watch the objects glow!</center></html>"
        );

        showSequence();
    }

    private void showSequence() {
        showingSequence = true;
        playbackStep = 0;

        setButtonsEnabled(false);
        resetButtonColors();

        playbackTimer = new Timer(600, e -> {
            if (playbackStep >= sequence.size() * 2) {
                playbackTimer.stop();

                resetButtonColors();
                showingSequence = false;
                setButtonsEnabled(true);

                instructionLabel.setText(
                    "<html><center>Your turn!<br>" +
                    "Repeat the sequence.</center></html>"
                );
                return;
            }

            if (playbackStep % 2 == 0) {
                int symbolIndex = sequence.get(playbackStep / 2);

                buttons[symbolIndex].setBackground(activeColor);
                buttons[symbolIndex].setForeground(Color.BLACK);
            } else {
                resetButtonColors();
            }

            playbackStep++;
        });

        playbackTimer.setInitialDelay(800);
        playbackTimer.start();
    }

    private void handlePlayerInput(int symbolIndex) {
        if (showingSequence) {
            return;
        }

        if (symbolIndex != sequence.get(playerIndex)) {
            playerIndex = 0;

            instructionLabel.setText(
                "<html><center>The ghost tricked you!<br>" +
                "Watch the sequence again.</center></html>"
            );

            setButtonsEnabled(false);

            Timer retryTimer = new Timer(1000, e -> {
                ((Timer) e.getSource()).stop();
                showSequence();
            });

            retryTimer.setRepeats(false);
            retryTimer.start();
            return;
        }

        playerIndex++;

        if (playerIndex == sequence.size()) {
            setButtonsEnabled(false);

            if (round == 3) {
                showWinScreen();
            } else {
                instructionLabel.setText(
                    "<html><center>Well done!<br>" +
                    "The ghost grows weaker...</center></html>"
                );

                Timer nextRoundTimer = new Timer(1000, e -> {
                    ((Timer) e.getSource()).stop();
                    round++;
                    startRound();
                });

                nextRoundTimer.setRepeats(false);
                nextRoundTimer.start();
            }
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        for (JButton button : buttons) {
            button.setEnabled(enabled);
        }
    }

    private void resetButtonColors() {
        for (JButton button : buttons) {
            button.setBackground(normalColor);
            button.setForeground(Color.WHITE);
        }
    }

    private void showWinScreen() {
        if (playbackTimer != null) {
            playbackTimer.stop();
        }

        JOptionPane.showMessageDialog(
            this,
            "Bedroom Restored!\n\n" +
            "You remembered every sequence!\n" +
            "The ghost's curse has been broken!",
            "Puzzle Complete!",
            JOptionPane.INFORMATION_MESSAGE
        );

        dispose();

        if (puzzleCompleteListener != null) {
            puzzleCompleteListener.run();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
            () -> new BedroomPuzzle()
        );
    }
}

