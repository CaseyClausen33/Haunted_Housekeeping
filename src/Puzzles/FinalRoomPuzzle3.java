package Puzzles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.*;

public class FinalRoomPuzzle3 extends JFrame {
    public static boolean solved = false;
    private static boolean isOpen = false;

    private static final int GRID_SIZE = 8;
    private static final int CELL_SIZE = 52;
    private static final int GHOST_TYPES = 5;
    private static final int MOVE_LIMIT = 15;
    private static final int GHOST_GOAL = 50;

    private final Color[] ghostColors = {
        new Color(170, 100, 230), // Purple
        new Color(100, 210, 130), // Green
        new Color(90, 170, 240),  // Blue
        new Color(240, 100, 110), // Red
        new Color(245, 210, 90)   // Yellow
    };

    private int[][] board = new int[GRID_SIZE][GRID_SIZE];

    private int movesLeft = MOVE_LIMIT;
    private int ghostsCleared = 0;

    private Point selectedCell = null;

    private GhostGridPanel gridPanel;

    private JLabel scoreLabel;
    private JLabel movesLabel;
    private JLabel messageLabel;

    private Runnable puzzleCompleteListener;

    private boolean gameOver = false;

    private boolean[][] clearingCells = new boolean[GRID_SIZE][GRID_SIZE];

    public FinalRoomPuzzle3() {

        setTitle("Ghost Busters");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                isOpen = false;
            }
        });
        setLocationRelativeTo(null);
        setResizable(false);

        generateBoard();
        createInterface();

        setVisible(true);
    }

    public void setPuzzleCompleteListener(Runnable listener) {
        this.puzzleCompleteListener = listener;
    }

    // Generates a board without automatic matches.
    private void generateBoard() {

        Random random = new Random();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {

                int type;

                do {
                    type = random.nextInt(GHOST_TYPES);
                } while (wouldCreateMatch(row, col, type));

                board[row][col] = type;
            }
        }
    }

    // Prevents three ghosts from matching during generation.
    private boolean wouldCreateMatch(int row, int col, int type) {

        if (col >= 2 &&
            board[row][col - 1] == type &&
            board[row][col - 2] == type) {
            return true;
        }

        if (row >= 2 &&
            board[row - 1][col] == type &&
            board[row - 2][col] == type) {
            return true;
        }

        return false;
    }

    // Creates the game interface.
    private void createInterface() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBackground(new Color(35, 25, 45));

        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        );

        // Title.
        JLabel title = new JLabel(
            "GHOST BUSTERS",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(new Color(220, 190, 255));

        // Instructions.
        JLabel instructions = new JLabel(
            "Match 3 or more ghosts to clear them!",
            SwingConstants.CENTER
        );

        instructions.setFont(new Font("Arial", Font.PLAIN, 14));
        instructions.setForeground(Color.WHITE);

        JPanel headerPanel = new JPanel();

        headerPanel.setLayout(
            new BoxLayout(headerPanel, BoxLayout.Y_AXIS)
        );

        headerPanel.setBackground(new Color(35, 25, 45));

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(instructions);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Score and moves.
        JPanel statsPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 45, 8)
        );

        statsPanel.setBackground(new Color(35, 25, 45));

        scoreLabel = new JLabel();
        movesLabel = new JLabel();

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        movesLabel.setFont(new Font("Arial", Font.BOLD, 16));

        scoreLabel.setForeground(Color.WHITE);
        movesLabel.setForeground(Color.WHITE);

        statsPanel.add(scoreLabel);
        statsPanel.add(movesLabel);

        // Grid.
        gridPanel = new GhostGridPanel();

        JPanel gridContainer = new JPanel(
            new FlowLayout(FlowLayout.CENTER)
        );

        gridContainer.setBackground(new Color(35, 25, 45));
        gridContainer.add(gridPanel);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));

        centerPanel.setBackground(new Color(35, 25, 45));

        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(gridContainer, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom section.
        JPanel bottomPanel = new JPanel();

        bottomPanel.setLayout(
            new BoxLayout(bottomPanel, BoxLayout.Y_AXIS)
        );

        bottomPanel.setBackground(new Color(35, 25, 45));

        messageLabel = new JLabel(
            "Clear 30 ghosts to win!",
            SwingConstants.CENTER
        );

        messageLabel.setFont(new Font("Arial", Font.BOLD, 15));
        messageLabel.setForeground(new Color(220, 190, 255));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton resetButton = new JButton("Reset Puzzle");

        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        resetButton.addActionListener(e -> resetPuzzle());

        bottomPanel.add(messageLabel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(resetButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        updateStats();
    }

    // Updates score and move labels.
    private void updateStats() {

        scoreLabel.setText(
            "Ghosts Cleared: " + ghostsCleared + "/" + GHOST_GOAL
        );

        movesLabel.setText(
            "Moves Left: " + movesLeft
        );

        gridPanel.repaint();
    }

    // Handles selecting and swapping ghosts.
    private void handleCellClick(int row, int col) {

        if (gameOver) {
            return;
        }

        Point clickedCell = new Point(col, row);

        if (selectedCell == null) {

            selectedCell = clickedCell;
            gridPanel.repaint();

            return;
        }

        // Clicking the same ghost deselects it.
        if (selectedCell.equals(clickedCell)) {

            selectedCell = null;
            gridPanel.repaint();

            return;
        }

        int rowDifference =
            Math.abs(selectedCell.y - row);

        int colDifference =
            Math.abs(selectedCell.x - col);

        // Only adjacent ghosts can be swapped.
        if (rowDifference + colDifference != 1) {

            selectedCell = clickedCell;
            gridPanel.repaint();

            return;
        }

        int oldRow = selectedCell.y;
        int oldCol = selectedCell.x;

        selectedCell = null;

        swapGhosts(oldRow, oldCol, row, col);
    }

    // Swaps two ghosts and checks for matches.
    private void swapGhosts(
        int row1, int col1,
        int row2, int col2
    ) {

        swap(row1, col1, row2, col2);

        if (!hasMatches()) {

            // Reverse an invalid swap.
            swap(row1, col1, row2, col2);

            messageLabel.setText(
                "No match! Try another swap."
            );

            gridPanel.repaint();

            return;
        }

        movesLeft--;

        messageLabel.setText("Ghosts busted!");

        resolveMatches();
    }

    private void swap(
        int row1, int col1,
        int row2, int col2
    ) {

        int temp = board[row1][col1];

        board[row1][col1] = board[row2][col2];
        board[row2][col2] = temp;
    }

    // Finds all horizontal and vertical matches.
    private boolean[][] findMatches() {

        boolean[][] matched =
            new boolean[GRID_SIZE][GRID_SIZE];

        // Horizontal matches.
        for (int row = 0; row < GRID_SIZE; row++) {

            int start = 0;

            while (start < GRID_SIZE) {

                int type = board[row][start];

                int end = start + 1;

                while (end < GRID_SIZE &&
                       board[row][end] == type) {
                    end++;
                }

                if (end - start >= 3) {

                    for (int col = start; col < end; col++) {
                        matched[row][col] = true;
                    }
                }

                start = end;
            }
        }

        // Vertical matches.
        for (int col = 0; col < GRID_SIZE; col++) {

            int start = 0;

            while (start < GRID_SIZE) {

                int type = board[start][col];

                int end = start + 1;

                while (end < GRID_SIZE &&
                       board[end][col] == type) {
                    end++;
                }

                if (end - start >= 3) {

                    for (int row = start; row < end; row++) {
                        matched[row][col] = true;
                    }
                }

                start = end;
            }
        }

        return matched;
    }

        private boolean hasMatches() {

            boolean[][] matched = findMatches();

            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    if (matched[row][col]) {
                        return true;
                    }
                }
            }

            return false;
        }

        // Clears matches, applies gravity, and fills the grid.
        private void resolveMatches() {

        boolean[][] matched = findMatches();

        boolean hasMatch = false;

        // Check whether there are any matches.
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {

                if (matched[row][col]) {
                    hasMatch = true;
                }
            }
        }

        if (!hasMatch) {
            updateStats();
            checkGameStatus();
            return;
        }

        // Mark matching cells for the clearing animation.
        clearingCells = new boolean[GRID_SIZE][GRID_SIZE];

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {

                if (matched[row][col]) {
                    clearingCells[row][col] = true;
                }
            }
        }

        // Show the highlighted ghosts before removing them.
        gridPanel.repaint();

        javax.swing.Timer clearTimer = new javax.swing.Timer(550, e -> {

            ((javax.swing.Timer) e.getSource()).stop();

            // Remove the matched ghosts.
            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    if (matched[row][col]) {

                        board[row][col] = -1;
                        ghostsCleared++;
                    }
                }
            }

            // Move remaining ghosts downward.
            for (int col = 0; col < GRID_SIZE; col++) {

                int emptyRow = GRID_SIZE - 1;

                for (int row = GRID_SIZE - 1; row >= 0; row--) {

                    if (board[row][col] != -1) {
                        board[emptyRow][col] = board[row][col];
                        emptyRow--;
                    }
                }

                Random random = new Random();

                // Generate new ghosts at the top.
                for (int row = emptyRow; row >= 0; row--) {
                    board[row][col] = random.nextInt(GHOST_TYPES);
                }
            }

            clearingCells = new boolean[GRID_SIZE][GRID_SIZE];

            updateStats();
            gridPanel.repaint();

            // Check for additional matches caused by falling ghosts.
            resolveMatches();

        });

        clearTimer.setRepeats(false);
        clearTimer.start(); 
    }

    // Checks win/loss conditions.
    private void checkGameStatus() {

        if (ghostsCleared >= GHOST_GOAL) {

            gameOver = true;

            showWinScreen();
            solved = true;

        } else if (movesLeft <= 0) {

            gameOver = true;

            JOptionPane.showMessageDialog(
                this,
                "The ghosts are still haunting the room!\n\n"
                + "You cleared " + ghostsCleared
                + " ghosts.\n"
                + "Try again to restore the hotel!",
                "Out of Moves",
                JOptionPane.INFORMATION_MESSAGE
            );

            messageLabel.setText("Puzzle failed! Try again.");
        }
    }

    // Completion message.
    private void showWinScreen() {

        JOptionPane.showMessageDialog(
            this,

            "You cleared the haunted room!\n\n"
            + "The dark spirits disappear into the light...\n\n"
            + "Your grandfather's spirit is finally at peace.",

            "Puzzle Complete!",

            JOptionPane.INFORMATION_MESSAGE
        );

        solved = true;
        dispose();

        if (puzzleCompleteListener != null) {
            puzzleCompleteListener.run();
        }
    }

    // Resets the entire puzzle.
    private void resetPuzzle() {

        movesLeft = MOVE_LIMIT;
        ghostsCleared = 0;

        selectedCell = null;
        gameOver = false;

        generateBoard();

        messageLabel.setText(
            "Clear 30 ghosts to win!"
        );

        updateStats();
    }

    // Custom grid that draws the ghosts.
    private class GhostGridPanel extends JPanel {

        public GhostGridPanel() {

            setPreferredSize(
                new Dimension(
                    GRID_SIZE * CELL_SIZE,
                    GRID_SIZE * CELL_SIZE
                )
            );

            setBackground(new Color(35, 25, 45));

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    int col = e.getX() / CELL_SIZE;
                    int row = e.getY() / CELL_SIZE;

                    if (row >= 0 && row < GRID_SIZE &&
                        col >= 0 && col < GRID_SIZE) {

                        handleCellClick(row, col);
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {

                    int x = col * CELL_SIZE;
                    int y = row * CELL_SIZE;

                    // Cell background.
                    g2.setColor(new Color(65, 50, 80));

                    g2.fillRoundRect(
                        x + 2, y + 2,
                        CELL_SIZE - 4, CELL_SIZE - 4,
                        10, 10
                    );

                    // Cell border.
                    g2.setColor(new Color(100, 80, 120));

                    g2.drawRoundRect(
                        x + 2, y + 2,
                        CELL_SIZE - 4, CELL_SIZE - 4,
                        10, 10
                    );

                    // Selected ghost highlight.
                    if (selectedCell != null &&
                        selectedCell.x == col &&
                        selectedCell.y == row) {

                        g2.setColor(new Color(255, 255, 255));

                        g2.setStroke(new BasicStroke(3));

                        g2.drawRoundRect(
                            x + 3, y + 3,
                            CELL_SIZE - 6, CELL_SIZE - 6,
                            10, 10
                        );

                        g2.setStroke(new BasicStroke(1));
                    }

                    // Draw the ghost.
                    drawGhost(
                        g2,
                        x,
                        y,
                        ghostColors[board[row][col]]
                    );

                    // Highlight ghosts that are about to disappear.
                    if (clearingCells[row][col]) {

                        g2.setColor(new Color(255, 255, 255, 160));

                        g2.fillRoundRect(
                            x + 3,
                            y + 3,
                            CELL_SIZE - 6,
                            CELL_SIZE - 6,
                            12,
                            12
                        );

                        g2.setColor(Color.WHITE);
                        g2.setFont(new Font("Arial", Font.BOLD, 13));

                        g2.drawString(
                            "POP!",
                            x + 7,
                            y + CELL_SIZE - 5
                        );
                    }
                }
            }

            g2.dispose();
        }

        // Draws a small cartoon ghost.
        private void drawGhost(
            Graphics2D g2,
            int x,
            int y,
            Color color
        ) {

            int size = CELL_SIZE - 14;

            int ghostX = x + 7;
            int ghostY = y + 6;

            Path2D ghost = new Path2D.Double();

            ghost.moveTo(ghostX, ghostY + size);

            ghost.lineTo(
                ghostX, ghostY + size / 2
            );

            ghost.curveTo(
                ghostX,
                ghostY - 3,
                ghostX + size,
                ghostY - 3,
                ghostX + size,
                ghostY + size / 2
            );

            ghost.lineTo(
                ghostX + size,
                ghostY + size
            );

            ghost.lineTo(
                ghostX + size * 0.75,
                ghostY + size * 0.82
            );

            ghost.lineTo(
                ghostX + size * 0.5,
                ghostY + size
            );

            ghost.lineTo(
                ghostX + size * 0.25,
                ghostY + size * 0.82
            );

            ghost.closePath();

            g2.setColor(color);
            g2.fill(ghost);

            g2.setColor(color.darker());
            g2.draw(ghost);

            // Eyes.
            g2.setColor(Color.BLACK);

            g2.fillOval(
                ghostX + size / 4 - 2,
                ghostY + size / 3,
                5, 7
            );

            g2.fillOval(
                ghostX + size * 3 / 4 - 3,
                ghostY + size / 3,
                5, 7
            );
        }
    }

    public static void launch() {
        if (!isOpen && !solved) {
            isOpen = true;
            SwingUtilities.invokeLater(() -> new FinalRoomPuzzle3());
        }
    }

    public static boolean isOpen() {
        return isOpen;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
            FinalRoomPuzzle3::new
        );
    }
}