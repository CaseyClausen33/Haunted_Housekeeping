package Puzzles;

import Engine.Config;

import javax.swing.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class BedroomPuzzle extends JFrame {

    // Set to true when the player beats all three rounds.
    public static volatile boolean solved = false;

    // True while the puzzle window is open.
    private static volatile boolean open = false;

    public static boolean isOpen() {
        return open;
    }

    // Opens the puzzle window; does nothing if it is already open.
    public static synchronized void launch() {
        if (open) {
            return;
        }

        open = true;
        SwingUtilities.invokeLater(() -> new BedroomPuzzle());
    }

    private Runnable puzzleCompleteListener;

    private final Color backgroundColor = new Color(25, 20, 35);
    private final Color normalColor = new Color(75, 52, 90);
    private final Color activeColor = new Color(205, 155, 255);
    private final Color borderColor = new Color(155, 115, 180);

    private final String[] symbols = {
        "🛏️", "🧸", "⏰", "🪞"
    };

    private final JButton[] buttons = new JButton[4];
    private final Clip[] buttonSoundEffects = new Clip[buttons.length];

    private final ArrayList<Integer> sequence = new ArrayList<>();
    private final Random random = new Random();

    private JLabel instructionLabel;
    private JLabel roundLabel;
    private JLabel strikesLabel;

    private int round = 1;
    private int playerIndex = 0;
    private int playbackStep = 0;

    // 3 mistakes total means the puzzle restarts from Round 1.
    private static final int MAX_STRIKES = 3;
    private int strikes = 0;

    private boolean showingSequence = false;
    private Timer playbackTimer;

    public BedroomPuzzle() {

        setTitle("Bedroom Puzzle");

        // Slightly taller window so the buttons have plenty of room.
        setSize(400, 450);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {

                // Stop the sequence animation if the window closes.
                if (playbackTimer != null) {
                    playbackTimer.stop();
                }

                closeSoundEffects();
                open = false;
            }
        });

        loadSoundEffects();

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
        roundLabel.setFont(new Font("Serif", Font.BOLD, 14));

        strikesLabel = new JLabel(
            "Strikes: 0 / 3",
            SwingConstants.CENTER
        );

        strikesLabel.setForeground(Color.WHITE);
        strikesLabel.setFont(new Font("Serif", Font.BOLD, 14));

        instructionLabel = new JLabel(
            "<html><center>Look closely...<br>" +
            "the room remembers.</center></html>",
            SwingConstants.CENTER
        );

        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setFont(new Font("Serif", Font.PLAIN, 14));

        /*
         * Keep the top section organized.
         *
         * This used to be only 3 rows. The strike counter adds a fourth
         * row, so the layout explicitly accounts for it.
         */
        JPanel topPanel = new JPanel(
            new GridLayout(4, 1, 2, 2)
        );

        topPanel.setOpaque(false);

        topPanel.add(titleLabel);
        topPanel.add(roundLabel);
        topPanel.add(strikesLabel);
        topPanel.add(instructionLabel);

        /*
         * IMPORTANT:
         * Give the button panel a stable size.
         *
         * Without this, changing the instruction text after a mistake
         * can cause Swing's BorderLayout to squeeze the button panel,
         * making the four buttons tiny.
         */
        JPanel buttonPanel = new JPanel(
            new GridLayout(2, 2, 10, 10)
        );

        buttonPanel.setOpaque(false);

        buttonPanel.setPreferredSize(
            new Dimension(340, 270)
        );

        buttonPanel.setMinimumSize(
            new Dimension(340, 270)
        );

        for (int i = 0; i < buttons.length; i++) {

            final int symbolIndex = i;

            JButton button = new JButton(symbols[i]);

            /*
             * Give every button a stable minimum/preferred size so
             * they cannot collapse when the layout is recalculated.
             */
            button.setPreferredSize(
                new Dimension(160, 125)
            );

            button.setMinimumSize(
                new Dimension(140, 110)
            );

            button.setFont(
                new Font("Serif", Font.PLAIN, 46)
            );

            button.setForeground(Color.WHITE);
            button.setBackground(normalColor);
            button.setOpaque(true);
            button.setFocusPainted(false);

            button.setBorder(
                BorderFactory.createLineBorder(
                    borderColor,
                    2
                )
            );

            button.addActionListener(e -> {

                playSoundEffect(symbolIndex);

                // Make the clicked object glow.
                button.setBackground(activeColor);
                button.setForeground(Color.BLACK);

                Timer clickGlowTimer = new Timer(
                    250,
                    event -> {

                        button.setBackground(normalColor);
                        button.setForeground(Color.WHITE);

                        ((Timer) event.getSource()).stop();
                    }
                );

                clickGlowTimer.setRepeats(false);
                clickGlowTimer.start();

                handlePlayerInput(symbolIndex);
            });

            buttons[i] = button;

            buttonPanel.add(button);
        }

        mainPanel.add(
            topPanel,
            BorderLayout.NORTH
        );

        mainPanel.add(
            buttonPanel,
            BorderLayout.CENTER
        );

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

        roundLabel.setText(
            "Round " + round + " of 3"
        );

        instructionLabel.setText(
            "<html><center>Look closely...<br>" +
            "watch the objects glow!</center></html>"
        );

        updateStrikesLabel();

        /*
         * Make sure Swing recalculates the layout after changing
         * the instruction text.
         */
        getContentPane().revalidate();
        getContentPane().repaint();

        showSequence();
    }

    private void showSequence() {

        showingSequence = true;
        playbackStep = 0;

        setButtonsEnabled(false);
        resetButtonColors();

        /*
         * Stop an old playback timer before starting a new one.
         *
         * This is especially important when the sequence is being
         * replayed after a mistake.
         */
        if (playbackTimer != null) {
            playbackTimer.stop();
        }

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

                /*
                 * Force the layout to refresh without allowing
                 * the buttons to collapse.
                 */
                getContentPane().revalidate();
                getContentPane().repaint();

                return;
            }

            if (playbackStep % 2 == 0) {

                int symbolIndex =
                    sequence.get(playbackStep / 2);

                playSoundEffect(symbolIndex);

                buttons[symbolIndex].setBackground(
                    activeColor
                );

                buttons[symbolIndex].setForeground(
                    Color.BLACK
                );

            } else {

                resetButtonColors();
            }

            playbackStep++;
        });

        playbackTimer.setInitialDelay(800);
        playbackTimer.start();
    }

    private void loadSoundEffects() {

        for (int i = 0; i < buttonSoundEffects.length; i++) {

            String fileName =
                "bedroompuzzlesoundeffect" + (i + 1) + ".wav";

            Path soundFile =
                Paths.get(
                    Config.RESOURCES_PATH,
                    fileName
                );

            Clip clip = null;

            try (
                AudioInputStream input =
                    AudioSystem.getAudioInputStream(
                        soundFile.toFile()
                    )
            ) {

                clip = AudioSystem.getClip();

                clip.open(input);

                buttonSoundEffects[i] = clip;

            } catch (
                UnsupportedAudioFileException |
                IOException |
                LineUnavailableException e
            ) {

                if (clip != null) {
                    clip.close();
                }

                System.err.println(
                    "Unable to load bedroom puzzle sound effect "
                    + soundFile
                    + ": "
                    + e.getMessage()
                );
            }
        }
    }

    private void playSoundEffect(int buttonIndex) {

        Clip clip =
            buttonSoundEffects[buttonIndex];

        if (clip != null) {

            clip.stop();

            clip.setFramePosition(0);

            clip.start();
        }
    }

    private void closeSoundEffects() {

        for (Clip clip : buttonSoundEffects) {

            if (clip != null) {
                clip.close();
            }
        }
    }

    private void handlePlayerInput(int symbolIndex) {

        if (showingSequence) {
            return;
        }

        /*
         * WRONG ANSWER
         */
        if (symbolIndex != sequence.get(playerIndex)) {

            strikes++;

            playerIndex = 0;

            updateStrikesLabel();

            /*
             * Three mistakes = complete restart.
             */
            if (strikes >= MAX_STRIKES) {

                instructionLabel.setText(
                    "<html><center>Too many mistakes!<br>" +
                    "The curse takes over...</center></html>"
                );

                setButtonsEnabled(false);

                getContentPane().revalidate();
                getContentPane().repaint();

                /*
                 * Small delay before showing the restart popup so
                 * the player can see that they lost.
                 */
                Timer loseTimer = new Timer(
                    700,
                    e -> {

                        ((Timer) e.getSource()).stop();

                        showLoseScreen();
                    }
                );

                loseTimer.setRepeats(false);
                loseTimer.start();

                return;
            }

            /*
             * The player still has strikes remaining.
             */
            instructionLabel.setText(
                "<html><center>The ghost tricked you!<br>" +
                "Watch the sequence again.</center></html>"
            );

            setButtonsEnabled(false);

            getContentPane().revalidate();
            getContentPane().repaint();

            /*
             * Replay the SAME round after a mistake.
             */
            Timer retryTimer = new Timer(
                1000,
                e -> {

                    ((Timer) e.getSource()).stop();

                    showSequence();
                }
            );

            retryTimer.setRepeats(false);
            retryTimer.start();

            return;
        }

        /*
         * CORRECT ANSWER
         */
        playerIndex++;

        /*
         * Player completed the current sequence.
         */
        if (playerIndex == sequence.size()) {

            setButtonsEnabled(false);

            if (round == 3) {

                showWinScreen();

            } else {

                instructionLabel.setText(
                    "<html><center>Well done!<br>" +
                    "The ghost grows weaker...</center></html>"
                );

                getContentPane().revalidate();
                getContentPane().repaint();

                Timer nextRoundTimer = new Timer(
                    1000,
                    e -> {

                        ((Timer) e.getSource()).stop();

                        round++;

                        startRound();
                    }
                );

                nextRoundTimer.setRepeats(false);
                nextRoundTimer.start();
            }
        }
    }

    private void updateStrikesLabel() {

        strikesLabel.setText(
            "Strikes: " + strikes + " / " + MAX_STRIKES
        );

        /*
         * Make the counter increasingly noticeable as the player
         * gets closer to losing.
         */
        if (strikes == 2) {

            strikesLabel.setForeground(
                new Color(255, 180, 70)
            );

        } else if (strikes >= 3) {

            strikesLabel.setForeground(
                new Color(255, 80, 80)
            );

        } else {

            strikesLabel.setForeground(Color.WHITE);
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

    private void showLoseScreen() {

        if (playbackTimer != null) {
            playbackTimer.stop();
        }

        showingSequence = false;

        resetButtonColors();

        /*
         * Only give the player a restart option.
         */
        JOptionPane.showOptionDialog(
            this,

            "You made three mistakes!\n\n" +
            "The bedroom curse has reset the puzzle.\n" +
            "You'll have to start again from Round 1.",

            "Puzzle Failed",

            JOptionPane.DEFAULT_OPTION,

            JOptionPane.ERROR_MESSAGE,

            null,

            new Object[] { "RESTART" },

            "RESTART"
        );

        /*
         * Completely reset the puzzle.
         */
        round = 1;
        strikes = 0;
        playerIndex = 0;
        playbackStep = 0;

        updateStrikesLabel();

        setButtonsEnabled(false);

        startRound();
    }

    private void showWinScreen() {

        if (playbackTimer != null) {
            playbackTimer.stop();
        }

        BedroomPuzzle.solved = true;

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