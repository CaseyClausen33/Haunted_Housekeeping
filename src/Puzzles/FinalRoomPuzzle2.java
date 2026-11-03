package Puzzles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class FinalRoomPuzzle2 extends JFrame {
    public static boolean solved = false;
    private static boolean isOpen = false;

    private static final int GRID_SIZE = 10;
    private static final int CELL_SIZE = 36;

    private final String[] words = {
        "GHOST", "HAUNT", "CURSE", "SPIRIT", "SHADOW"
    };

    private char[][] grid = new char[GRID_SIZE][GRID_SIZE];

    private Set<String> foundWords = new HashSet<>();
    private Set<Point> foundCells = new HashSet<>();

    private WordGridPanel gridPanel;
    private JPanel wordListPanel;

    private Point selectionStart = null;
    private Point selectionEnd = null;

    private Runnable puzzleCompleteListener;

    public FinalRoomPuzzle2() {
        setTitle("The Final Room: Word Search");
        setSize(600, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                isOpen = false;
            }
        });
        setLocationRelativeTo(null);
        setResizable(false);

        generateGrid();
        createInterface();

        setVisible(true);
    }

    public void setPuzzleCompleteListener(Runnable listener) {
        this.puzzleCompleteListener = listener;
    }

    // Generates the word search.
    private void generateGrid() {
        grid = new char[GRID_SIZE][GRID_SIZE];
        Random random = new Random();

        for (int row = 0; row < GRID_SIZE; row++) {
            Arrays.fill(grid[row], ' ');
        }

        for (String word : words) {
            placeWord(word, random);
        }

        // Fill remaining spaces with random letters.
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] == ' ') {
                    grid[row][col] =
                        (char) ('A' + random.nextInt(26));
                }
            }
        }
    }

    // Places words randomly in the grid.
    private void placeWord(String word, Random random) {

        int[][] directions = {
            {0, 1}, {0, -1},
            {1, 0}, {-1, 0},
            {1, 1}, {1, -1},
            {-1, 1}, {-1, -1}
        };

        boolean placed = false;

        while (!placed) {

            int row = random.nextInt(GRID_SIZE);
            int col = random.nextInt(GRID_SIZE);

            int[] direction =
                directions[random.nextInt(directions.length)];

            int endRow =
                row + direction[0] * (word.length() - 1);

            int endCol =
                col + direction[1] * (word.length() - 1);

            if (endRow < 0 || endRow >= GRID_SIZE ||
                endCol < 0 || endCol >= GRID_SIZE) {
                continue;
            }

            boolean canPlace = true;

            for (int i = 0; i < word.length(); i++) {

                int r = row + direction[0] * i;
                int c = col + direction[1] * i;

                if (grid[r][c] != ' ' &&
                    grid[r][c] != word.charAt(i)) {

                    canPlace = false;
                    break;
                }
            }

            if (canPlace) {

                for (int i = 0; i < word.length(); i++) {

                    int r = row + direction[0] * i;
                    int c = col + direction[1] * i;

                    grid[r][c] = word.charAt(i);
                }

                placed = true;
            }
        }
    }

    // Creates the puzzle interface.
    private void createInterface() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBackground(new Color(35, 25, 45));

        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(10, 15, 5, 15)
        );

        // Title.
        JLabel title = new JLabel(
            "THE FINAL ROOM",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(new Color(220, 190, 255));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Instructions.
        JLabel instructions = new JLabel(
            "<html><center>"
            + "Find all five hidden words to restore the hotel."
            + "<br>Click and drag across letters to select a word."
            + "</center></html>",
            SwingConstants.CENTER
        );

        instructions.setFont(new Font("Arial", Font.PLAIN, 15));
        instructions.setForeground(Color.WHITE);
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel headerPanel = new JPanel();

        headerPanel.setLayout(
            new BoxLayout(headerPanel, BoxLayout.Y_AXIS)
        );

        headerPanel.setBackground(new Color(35, 25, 45));

        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(instructions);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center section
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(
            new BoxLayout(centerPanel, BoxLayout.Y_AXIS)
        );
        centerPanel.setBackground(new Color(35, 25, 45));

        // Word search grid
        gridPanel = new WordGridPanel();

        JPanel gridContainer = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 0, 0)
        );

        gridContainer.setBackground(new Color(35, 25, 45));
        gridContainer.add(gridPanel);

        // Prevent the grid container from stretching vertically
        gridContainer.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 360)
        );

            centerPanel.add(Box.createVerticalStrut(10));
            centerPanel.add(gridContainer);
            centerPanel.add(Box.createVerticalStrut(15));

        // Hidden words heading
        JLabel wordListTitle = new JLabel(
            "HIDDEN WORDS",
            SwingConstants.CENTER
        );

        wordListTitle.setFont(new Font("Arial", Font.BOLD, 18));
        wordListTitle.setForeground(new Color(220, 190, 255));
        wordListTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(wordListTitle);
        centerPanel.add(Box.createVerticalStrut(5));

        // Horizontal word list
        wordListPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 12, 5)
        );

        wordListPanel.setBackground(new Color(35, 25, 45));

        updateWordList();

        JPanel wordListContainer = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 0, 0)
        );

        wordListContainer.setBackground(new Color(35, 25, 45));
        wordListContainer.add(wordListPanel);

        wordListContainer.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 40)
        );

        centerPanel.add(wordListContainer);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Reset button.
        JButton resetButton = new JButton("Reset Puzzle");

        resetButton.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        resetButton.addActionListener(e -> resetPuzzle());

        JPanel bottomPanel = new JPanel();

        bottomPanel.setBackground(new Color(35, 25, 45));
        bottomPanel.add(resetButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Updates the horizontal hidden word list.
    private void updateWordList() {

        wordListPanel.removeAll();

        for (String word : words) {

            JLabel wordLabel = new JLabel(
                foundWords.contains(word)
                    ? "✓ " + word
                    : word
            );

            wordLabel.setFont(
                new Font("Arial", Font.BOLD, 14)
            );

            wordLabel.setForeground(
                foundWords.contains(word)
                    ? new Color(150, 255, 170)
                    : Color.WHITE
            );

            wordListPanel.add(wordLabel);
        }

        wordListPanel.revalidate();
        wordListPanel.repaint();
    }

    // Resets the puzzle.
    private void resetPuzzle() {

        foundWords.clear();
        foundCells.clear();

        selectionStart = null;
        selectionEnd = null;

        generateGrid();

        updateWordList();
        gridPanel.repaint();
    }

    // Checks whether the selected letters form a hidden word.
    private void checkSelection() {

        if (selectionStart == null || selectionEnd == null) {
            return;
        }

        int rowDifference =
            selectionEnd.y - selectionStart.y;

        int colDifference =
            selectionEnd.x - selectionStart.x;

        // Only allow straight horizontal, vertical, or diagonal lines.
        if (rowDifference != 0 && colDifference != 0 &&
            Math.abs(rowDifference) != Math.abs(colDifference)) {
            return;
        }

        int rowStep = Integer.compare(rowDifference, 0);
        int colStep = Integer.compare(colDifference, 0);

        StringBuilder selectedWord = new StringBuilder();

        List<Point> selectedCells = new ArrayList<>();

        int row = selectionStart.y;
        int col = selectionStart.x;

        while (true) {

            selectedWord.append(grid[row][col]);

            selectedCells.add(new Point(col, row));

            if (row == selectionEnd.y &&
                col == selectionEnd.x) {
                break;
            }

            row += rowStep;
            col += colStep;
        }

        String word = selectedWord.toString();
        String reversedWord =
            selectedWord.reverse().toString();

        for (String target : words) {

            if (!foundWords.contains(target) &&
                (word.equals(target) ||
                 reversedWord.equals(target))) {

                foundWords.add(target);
                foundCells.addAll(selectedCells);

                updateWordList();
                gridPanel.repaint();

                if (foundWords.size() == words.length) {
                    showWinScreen();
                }

                return;
            }
        }
    }

    // Displays the completion message.
    private void showWinScreen() {

        JOptionPane.showMessageDialog(
            this,

            "You found every hidden word!\n\n"
            + "The letters begin to glow...\n\n"
            + "\"YOU ARE ONE STEP CLOSER TO RESTORING THE HOTEL!\"\n\n"
            + "Your grandfather's spirit seems to be guiding you.\n"
            + "The hotel is slowly coming back to life...",

            "Puzzle Complete!",

            JOptionPane.INFORMATION_MESSAGE
        );

        solved = true;
        dispose();

        if (puzzleCompleteListener != null) {
            puzzleCompleteListener.run();
        }
    }

    // Word search grid panel.
    private class WordGridPanel extends JPanel {

        public WordGridPanel() {

            setPreferredSize(
                new Dimension(
                    GRID_SIZE * CELL_SIZE,
                    GRID_SIZE * CELL_SIZE
                )
            );

            setBackground(new Color(35, 25, 45));

            MouseAdapter mouseHandler = new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    int col = e.getX() / CELL_SIZE;
                    int row = e.getY() / CELL_SIZE;

                    if (isValidCell(row, col)) {

                        selectionStart = new Point(col, row);
                        selectionEnd = new Point(col, row);

                        repaint();
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {

                    int col = e.getX() / CELL_SIZE;
                    int row = e.getY() / CELL_SIZE;

                    if (isValidCell(row, col)) {

                        selectionEnd = new Point(col, row);

                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    int col = e.getX() / CELL_SIZE;
                    int row = e.getY() / CELL_SIZE;

                    if (isValidCell(row, col)) {

                        selectionEnd = new Point(col, row);

                        checkSelection();
                    }

                    selectionStart = null;
                    selectionEnd = null;

                    repaint();
                }
            };

            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
        }

        private boolean isValidCell(int row, int col) {

            return row >= 0 && row < GRID_SIZE &&
                   col >= 0 && col < GRID_SIZE;
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            for (int row = 0; row < GRID_SIZE; row++) {

                for (int col = 0; col < GRID_SIZE; col++) {

                    int x = col * CELL_SIZE;
                    int y = row * CELL_SIZE;

                    Point cell = new Point(col, row);

                    // Cell colors.
                    if (foundCells.contains(cell)) {

                        g2.setColor(new Color(130, 80, 180));

                    } else if (isSelectedCell(row, col)) {

                        g2.setColor(new Color(100, 75, 130));

                    } else {

                        g2.setColor(new Color(65, 50, 80));
                    }

                    g2.fillRoundRect(
                        x + 2,
                        y + 2,
                        CELL_SIZE - 4,
                        CELL_SIZE - 4,
                        8,
                        8
                    );

                    // Cell borders.
                    g2.setColor(new Color(180, 150, 210));

                    g2.drawRoundRect(
                        x + 2,
                        y + 2,
                        CELL_SIZE - 4,
                        CELL_SIZE - 4,
                        8,
                        8
                    );

                    // Letters.
                    g2.setColor(Color.WHITE);

                    g2.setFont(
                        new Font("Arial", Font.BOLD, 16)
                    );

                    FontMetrics fm = g2.getFontMetrics();

                    String letter =
                        String.valueOf(grid[row][col]);

                    int textX =
                        x + (CELL_SIZE - fm.stringWidth(letter)) / 2;

                    int textY =
                        y + (CELL_SIZE - fm.getHeight()) / 2
                        + fm.getAscent();

                    g2.drawString(letter, textX, textY);
                }
            }
        }

        // Checks whether a cell is currently selected.
        private boolean isSelectedCell(int row, int col) {

            if (selectionStart == null ||
                selectionEnd == null) {
                return false;
            }

            int rowDifference =
                selectionEnd.y - selectionStart.y;

            int colDifference =
                selectionEnd.x - selectionStart.x;

            if (rowDifference != 0 && colDifference != 0 &&
                Math.abs(rowDifference) != Math.abs(colDifference)) {
                return false;
            }

            int rowStep = Integer.compare(rowDifference, 0);
            int colStep = Integer.compare(colDifference, 0);

            int currentRow = selectionStart.y;
            int currentCol = selectionStart.x;

            while (true) {

                if (currentRow == row && currentCol == col) {
                    return true;
                }

                if (currentRow == selectionEnd.y &&
                    currentCol == selectionEnd.x) {
                    break;
                }

                currentRow += rowStep;
                currentCol += colStep;
            }

            return false;
        }
    }

    public static void launch() {
        if (!isOpen && !solved) {
            isOpen = true;
            SwingUtilities.invokeLater(() -> new FinalRoomPuzzle2());
        }
    }

    public static boolean isOpen() {
        return isOpen;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
            FinalRoomPuzzle2::new
        );
    }
}