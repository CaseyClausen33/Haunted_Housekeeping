package Puzzles;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.*;

public class FinalRoomPuzzle1 extends JFrame {
    public static boolean solved = false;
    private static boolean isOpen = false;

    private Runnable puzzleCompleteListener;

    private final Color backgroundColor = new Color(25, 20, 35);
    private final Color panelColor = new Color(43, 32, 57);
    private final Color accentColor = new Color(190, 145, 225);
    private final Color slotColor = new Color(65, 48, 80);

    private final String[] ghosts = {
        "Misty", "Boo", "Phantom", "Wisp"
    };

    private final Color[] ghostColors = {
        new Color(240, 75, 85),
        new Color(65, 210, 130),
        new Color(75, 160, 255),
        new Color(190, 115, 255)
    };

     // Draws a small cartoon ghost instead of using text symbols or emoji.
    private static class GhostIcon implements Icon {

        private final Color ghostColor;

        GhostIcon(Color ghostColor) {
            this.ghostColor = ghostColor;
        }

        @Override
        public int getIconWidth() {
            return 48;
        }

        @Override
        public int getIconHeight() {
            return 48;
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {

            Graphics2D g2 = (Graphics2D) graphics.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            // Rounded ghost body with a wavy bottom.
            Path2D body = new Path2D.Double();
            body.moveTo(x + 8, y + 43);
            body.lineTo(x + 8, y + 22);
            body.curveTo(x + 8, y + 8, x + 17, y + 2, x + 24, y + 2);
            body.curveTo(x + 34, y + 2, x + 40, y + 10, x + 40, y + 22);
            body.lineTo(x + 40, y + 43);
            body.curveTo(x + 34, y + 37, x + 31, y + 48, x + 24, y + 42);
            body.curveTo(x + 17, y + 36, x + 14, y + 48, x + 8, y + 43);
            body.closePath();

            g2.setColor(ghostColor);
            g2.fill(body);

            g2.setColor(new Color(245, 235, 255));
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(body);

            // Eyes.
            g2.setColor(new Color(35, 25, 45));
            g2.fillOval(x + 17, y + 19, 4, 6);
            g2.fillOval(x + 29, y + 19, 4, 6);

            // Small friendly mouth.
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawArc(x + 21, y + 26, 7, 5, 200, 140);

            g2.dispose();
        }
    }

    private final JPanel[] slots = new JPanel[4];
    private final JLabel[] slotLabels = new JLabel[4];

    private final JLabel statusLabel = new JLabel(
        "Drag the ghosts into the correct order.",
        SwingConstants.CENTER
    );

    private final JLabel attemptsLabel = new JLabel(
        "Attempts remaining: 3",
        SwingConstants.CENTER
    );

    private String[] solution;
    private ArrayList<Clue> clues;

    private int attemptsRemaining = 3;

    // Represents one randomly generated clue.
    private static class Clue {

        String type;
        String firstGhost;
        String secondGhost;
        int position;

        Clue(String type, String firstGhost,
             String secondGhost, int position) {

            this.type = type;
            this.firstGhost = firstGhost;
            this.secondGhost = secondGhost;
            this.position = position;
        }

        String getDescription() {

            switch (type) {

                case "POSITION":
                    return firstGhost + " belongs in position "
                        + (position + 1) + ".";

                case "BEFORE":
                    return firstGhost + " must appear before "
                        + secondGhost + ".";

                case "NEXT":
                    return firstGhost + " must be immediately before "
                        + secondGhost + ".";

                case "NOT_FIRST":
                    return firstGhost + " cannot be first.";

                case "NOT_LAST":
                    return firstGhost + " cannot be last.";
            }

            return "";
        }

        boolean matches(String[] arrangement) {

            int first = indexOf(arrangement, firstGhost);
            int second = indexOf(arrangement, secondGhost);

            switch (type) {

                case "POSITION":
                    return first == position;

                case "BEFORE":
                    return first < second;

                case "NEXT":
                    return first + 1 == second;

                case "NOT_FIRST":
                    return first != 0;

                case "NOT_LAST":
                    return first != 3;
            }

            return false;
        }

        private static int indexOf(String[] arrangement, String ghost) {

            for (int i = 0; i < arrangement.length; i++) {
                if (arrangement[i].equals(ghost)) {
                    return i;
                }
            }

            return -1;
        }
    }

    public FinalRoomPuzzle1() {

        generatePuzzle();

        setTitle("The Haunted Spirits");
        setSize(500, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
                public void windowClosed(WindowEvent e) {
                isOpen = false;
            }
        });
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // Title
        JLabel titleLabel = new JLabel(
            "THE HAUNTED SPIRITS",
            SwingConstants.CENTER
        );

        titleLabel.setForeground(new Color(230, 200, 255));
        titleLabel.setFont(new Font("Serif", Font.BOLD, 23));

        JLabel subtitleLabel = new JLabel(
            "Arrange the four ghosts to break the curse.",
            SwingConstants.CENTER
        );

        subtitleLabel.setForeground(Color.LIGHT_GRAY);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 3, 3));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Center content
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(
            new BoxLayout(centerPanel, BoxLayout.Y_AXIS)
        );
        centerPanel.setBackground(backgroundColor);

        centerPanel.add(createLabel("PLACE THE GHOSTS"));
        centerPanel.add(Box.createVerticalStrut(10));

        // Drop slots
        JPanel slotPanel = new JPanel(new GridLayout(1, 4, 8, 8));
        slotPanel.setOpaque(false);

        for (int i = 0; i < 4; i++) {

            final int slotIndex = i;

            JPanel slot = new JPanel(new BorderLayout());
            slot.setBackground(slotColor);

            slot.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    BorderFactory.createEmptyBorder(5, 3, 5, 3)
                )
            );

            JLabel slotLabel = new JLabel(
                "Slot " + (i + 1),
                SwingConstants.CENTER
            );

            slotLabel.setForeground(Color.WHITE);
            slotLabel.setFont(new Font("Serif", Font.BOLD, 12));

            slot.add(slotLabel, BorderLayout.CENTER);

            slots[i] = slot;
            slotLabels[i] = slotLabel;

            TransferHandler dropHandler = new TransferHandler() {

                @Override
                public boolean canImport(TransferSupport support) {

                    return support.isDataFlavorSupported(
                        DataFlavor.stringFlavor
                    );
                }

                @Override
                public boolean importData(TransferSupport support) {

                    if (!canImport(support)) {
                        return false;
                    }

                    try {

                        String ghost = (String) support.getTransferable()
                            .getTransferData(DataFlavor.stringFlavor);

                        if (!Arrays.asList(ghosts).contains(ghost)) {
                            return false;
                        }

                        placeGhost(ghost, slotIndex);

                        return true;

                    } catch (Exception ex) {
                        return false;
                    }
                }
            };

            slot.setTransferHandler(dropHandler);
            slotLabel.setTransferHandler(dropHandler);

            slotPanel.add(slot);
        }

        centerPanel.add(slotPanel);
        centerPanel.add(Box.createVerticalStrut(20));

        centerPanel.add(createLabel("YOUR GHOSTS"));
        centerPanel.add(Box.createVerticalStrut(10));

        // Randomize the starting ghost order.
        JPanel ghostPanel = new JPanel(new GridLayout(1, 4, 8, 8));
        ghostPanel.setOpaque(false);

        ArrayList<String> shuffledGhosts =
            new ArrayList<>(Arrays.asList(ghosts));

        Collections.shuffle(shuffledGhosts);

        for (String ghost : shuffledGhosts) {

            int index = Arrays.asList(ghosts).indexOf(ghost);

            JLabel ghostLabel = new JLabel(
                ghost,
                new GhostIcon(ghostColors[index]),
                SwingConstants.CENTER
            );

            ghostLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            ghostLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
            ghostLabel.setIconTextGap(4);

            ghostLabel.setForeground(ghostColors[index]);
            ghostLabel.setFont(new Font("Serif", Font.BOLD, 12));
            ghostLabel.setOpaque(true);
            ghostLabel.setBackground(panelColor);

            ghostLabel.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ghostColors[index], 2),
                    BorderFactory.createEmptyBorder(8, 3, 8, 3)
                )
            );

            ghostLabel.setTransferHandler(new TransferHandler("text") {

                @Override
                protected Transferable createTransferable(
                    JComponent component
                ) {
                    return new StringSelection(ghost);
                }

                @Override
                public int getSourceActions(JComponent component) {
                    return COPY;
                }
            });

            ghostLabel.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    JLabel source = (JLabel) e.getSource();

                    source.getTransferHandler().exportAsDrag(
                        source,
                        e,
                        TransferHandler.COPY
                    );
                }
            });

            ghostPanel.add(ghostLabel);
        }

        centerPanel.add(ghostPanel);
        centerPanel.add(Box.createVerticalStrut(15));

        // Randomized clues with scrolling.
        JPanel cluePanel = new JPanel();
        cluePanel.setLayout(new BoxLayout(cluePanel, BoxLayout.Y_AXIS));
        cluePanel.setBackground(panelColor);

        cluePanel.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(accentColor),
                "Ghostly Clues",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Serif", Font.BOLD, 14),
                Color.WHITE
            )
        );

        for (Clue clue : clues) {

            JLabel label = new JLabel(
                "<html><div style='width:410px;'>"
                    + clue.getDescription()
                    + "</div></html>"
            );

            label.setForeground(Color.WHITE);
            label.setFont(new Font("Serif", Font.PLAIN, 12));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);

            cluePanel.add(label);
            cluePanel.add(Box.createVerticalStrut(3));
        }

        JScrollPane clueScrollPane = new JScrollPane(cluePanel);
        clueScrollPane.setPreferredSize(new Dimension(450, 115));
        clueScrollPane.setMinimumSize(new Dimension(450, 90));
        clueScrollPane.setMaximumSize(new Dimension(450, 115));
        clueScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        clueScrollPane.setBorder(
            BorderFactory.createLineBorder(accentColor)
        );

        clueScrollPane.getViewport().setBackground(panelColor);

        clueScrollPane.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        clueScrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        centerPanel.add(clueScrollPane);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom section
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        bottomPanel.setOpaque(false);

        statusLabel.setForeground(Color.WHITE);
        attemptsLabel.setForeground(accentColor);

        JButton checkButton = new JButton("CHECK SOLUTION");

        // Force the custom purple button appearance.
        checkButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        checkButton.setFont(new Font("Serif", Font.BOLD, 14));
        checkButton.setBackground(new Color(100, 65, 125));
        checkButton.setForeground(Color.WHITE);
        checkButton.setOpaque(true);
        checkButton.setContentAreaFilled(true);
        checkButton.setBorderPainted(true);
        checkButton.setFocusPainted(false);

        checkButton.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            )
        );

        checkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        checkButton.addActionListener(e -> checkSolution());

        bottomPanel.add(statusLabel);
        bottomPanel.add(attemptsLabel);
        bottomPanel.add(checkButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setVisible(true);
    }

    // Generate a random solution and clues that have exactly one solution.
    private void generatePuzzle() {

        ArrayList<String> shuffled = new ArrayList<>(
            Arrays.asList(ghosts)
        );

        Collections.shuffle(shuffled);

        solution = shuffled.toArray(new String[0]);

        ArrayList<Clue> possibleClues = new ArrayList<>();

        // Position clues
        for (int i = 0; i < 4; i++) {

            possibleClues.add(
                new Clue("POSITION", solution[i], null, i)
            );
        }

        // Relationship clues
        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {

                if (i == j) {
                    continue;
                }

                if (i < j) {

                    possibleClues.add(
                        new Clue(
                            "BEFORE",
                            solution[i],
                            solution[j],
                            -1
                        )
                    );
                }

                if (i + 1 == j) {

                    possibleClues.add(
                        new Clue(
                            "NEXT",
                            solution[i],
                            solution[j],
                            -1
                        )
                    );
                }
            }
        }

        // Position restrictions
        for (int i = 0; i < 4; i++) {

            if (i != 0) {
                possibleClues.add(
                    new Clue("NOT_FIRST", solution[i], null, -1)
                );
            }

            if (i != 3) {
                possibleClues.add(
                    new Clue("NOT_LAST", solution[i], null, -1)
                );
            }
        }

        // Randomize the possible clues.
        Collections.shuffle(possibleClues);

        clues = new ArrayList<>();

        // Keep adding clues until only one arrangement is possible.
        for (Clue clue : possibleClues) {

            clues.add(clue);

            if (countSolutions(clues) == 1) {
                break;
            }
        }

        // Randomize the order in which clues appear.
        Collections.shuffle(clues);
    }

    // Count how many arrangements satisfy the selected clues.
    private int countSolutions(ArrayList<Clue> selectedClues) {

        return countSolutionsRecursive(
            new String[4],
            new boolean[4],
            0,
            selectedClues
        );
    }

    private int countSolutionsRecursive(
        String[] arrangement,
        boolean[] used,
        int index,
        ArrayList<Clue> selectedClues
    ) {

        if (index == 4) {

            for (Clue clue : selectedClues) {

                if (!clue.matches(arrangement)) {
                    return 0;
                }
            }

            return 1;
        }

        int count = 0;

        for (int i = 0; i < ghosts.length; i++) {

            if (!used[i]) {

                used[i] = true;
                arrangement[index] = ghosts[i];

                count += countSolutionsRecursive(
                    arrangement,
                    used,
                    index + 1,
                    selectedClues
                );

                used[i] = false;
            }
        }

        return count;
    }

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text, SwingConstants.CENTER);

        label.setForeground(accentColor);
        label.setFont(new Font("Serif", Font.BOLD, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        return label;
    }

    private void placeGhost(String ghost, int slotIndex) {

        // Remove this ghost from any previous slot.
        for (int i = 0; i < slots.length; i++) {

            if (ghost.equals(slots[i].getClientProperty("ghost"))) {

                slots[i].putClientProperty("ghost", null);

                slotLabels[i].setText("Slot " + (i + 1));
            }
        }

        slots[slotIndex].putClientProperty("ghost", ghost);
        int ghostIndex = Arrays.asList(ghosts).indexOf(ghost);

        slotLabels[slotIndex].setText(ghost);
        slotLabels[slotIndex].setIcon(
            new GhostIcon(ghostColors[ghostIndex])
        );
        slotLabels[slotIndex].setForeground(ghostColors[ghostIndex]);

        slotLabels[slotIndex].setHorizontalTextPosition(
            SwingConstants.CENTER
        );

        slotLabels[slotIndex].setVerticalTextPosition(
            SwingConstants.BOTTOM
        );

        statusLabel.setText("Ghost placed! Keep arranging.");
    }

    private void checkSolution() {

        for (JPanel slot : slots) {

            if (slot.getClientProperty("ghost") == null) {

                statusLabel.setText(
                    "Place all four ghosts first!"
                );

                return;
            }
        }

        boolean correct = true;

        for (int i = 0; i < 4; i++) {

            String placedGhost = (String)
                slots[i].getClientProperty("ghost");

            if (!solution[i].equals(placedGhost)) {

                correct = false;
                break;
            }
        }

        if (correct) {

            showWinScreen();
            solved = true;

        } else {

            attemptsRemaining--;

            attemptsLabel.setText(
                "Attempts remaining: " + attemptsRemaining
            );

            if (attemptsRemaining <= 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "The curse has rejected your arrangement!\n"
                        + "The ghosts have been returned to the spirit realm.",
                    "The Curse Remains",
                    JOptionPane.WARNING_MESSAGE
                );

                resetPuzzle();

            } else {

                statusLabel.setText(
                    "Incorrect arrangement! Try again."
                );
            }
        }
    }

    private void resetPuzzle() {

        for (int i = 0; i < slots.length; i++) {

            slots[i].putClientProperty("ghost", null);

            slotLabels[i].setText("Slot " + (i + 1));
        }

        attemptsRemaining = 3;

        attemptsLabel.setText("Attempts remaining: 3");

        statusLabel.setText(
            "The ghosts have been returned to the spirit realm."
        );
    }

    public void setPuzzleCompleteListener(Runnable listener) {

        puzzleCompleteListener = listener;
    }

    private void showWinScreen() {

        JOptionPane.showMessageDialog(
            this,
            "The four ghosts begin to glow!\n\n"
                + "The curse has weakened!\n"
                + "You solved the Haunted Spirits puzzle!",
            "Puzzle Complete!",
            JOptionPane.INFORMATION_MESSAGE
        );

        solved = true;
        dispose();

        if (puzzleCompleteListener != null) {
            puzzleCompleteListener.run();
        }
    }

    public static void launch() {
        if (!isOpen && !solved) {
            isOpen = true;
            SwingUtilities.invokeLater(() -> new FinalRoomPuzzle1());
        }
    }

    public static boolean isOpen() {
        return isOpen;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
            FinalRoomPuzzle1::new
        );
    }
}