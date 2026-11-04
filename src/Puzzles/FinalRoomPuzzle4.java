package Puzzles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class FinalRoomPuzzle4 extends JFrame {
    public static boolean solved = false;
    private static boolean isOpen = false;

    private static final int GRID_SIZE = 4;
    private static final int CELL_SIZE = 110;
    private static final int BOARD_X = 65;
    private static final int BOARD_Y = 160;

    private BufferedImage originalImage;
    private BufferedImage[][] pieces =
        new BufferedImage[GRID_SIZE][GRID_SIZE];

    // Each number represents the original position of a piece.
    private int[] board = new int[GRID_SIZE * GRID_SIZE];

    private int selectedPiece = -1;
    private int moves = 0;
    private boolean completed = false;

    private Runnable puzzleCompleteListener;

    private final Color backgroundColor = new Color(25, 20, 35);
    private final Color accentColor = new Color(220, 190, 255);
    private PuzzlePanel puzzlePanel;

    public FinalRoomPuzzle4() {
        setTitle("The Broken Stained Glass");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                isOpen = false;
            }
        });
        setSize(590, 690);
        setLocationRelativeTo(null);
        setResizable(false);

        loadImage();
        divideImage();
        initializePuzzle();

        puzzlePanel = new PuzzlePanel();
        add(puzzlePanel);

        setVisible(true);
    }

    public void setPuzzleCompleteListener(Runnable listener) {
        this.puzzleCompleteListener = listener;
    }

    private void loadImage() {
        try {
            File imageFile = new File("Resources/hauntedhotelstainedglass.png");
            if (!imageFile.exists()) {
                throw new IOException("Image not found at: " + imageFile.getAbsolutePath());
            }

            originalImage = ImageIO.read(imageFile);

            if (originalImage == null) {
                throw new IOException("Unable to read the stained-glass image.");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading stained-glass image:\n" + e.getMessage(), "Image Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    // Divides the original image into 16 equal pieces.
    private void divideImage() {
        int pieceWidth = originalImage.getWidth() / GRID_SIZE;
        int pieceHeight = originalImage.getHeight() / GRID_SIZE;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                pieces[row][col] =
                    originalImage.getSubimage(
                        col * pieceWidth,
                        row * pieceHeight,
                        pieceWidth,
                        pieceHeight
                    );
            }
        }
    }

    // Creates and shuffles the puzzle.
    private void initializePuzzle() {
        for (int i = 0; i < board.length; i++) {
            board[i] = i;
        }

        Random random = new Random();

        // Shuffle the pieces.
        for (int i = board.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = board[i];
            board[i] = board[j];
            board[j] = temp;
        }

        // Make sure the puzzle is not already solved.
        if (isSolved()) {
            int temp = board[0];
            board[0] = board[1];
            board[1] = temp;
        }

        moves = 0;
        selectedPiece = -1;
        completed = false;
    }

    // Checks whether every piece is in its original position.
    private boolean isSolved() {
        for (int i = 0; i < board.length; i++) {
            if (board[i] != i) {
                return false;
            }
        }
        return true;
    }

    // Handles clicking and swapping two pieces.
    private void handleClick(int mouseX, int mouseY) {
        int boardWidth = GRID_SIZE * CELL_SIZE;
        int boardHeight = GRID_SIZE * CELL_SIZE;

        if (mouseX < BOARD_X || mouseX >= BOARD_X + boardWidth || mouseY < BOARD_Y || mouseY >= BOARD_Y + boardHeight) {
            return;
        }

        int col = (mouseX - BOARD_X) / CELL_SIZE;
        int row = (mouseY - BOARD_Y) / CELL_SIZE;

        int position = row * GRID_SIZE + col;

        // First click selects a piece.
        if (selectedPiece == -1) {
            selectedPiece = position;
        } else {
            // Second click swaps the two selected pieces.
            if (selectedPiece != position) {
                int temp = board[selectedPiece];
                board[selectedPiece] = board[position];
                board[position] = temp;
                moves++;
            }
            selectedPiece = -1;

            // Check whether the puzzle is completed.
            if (isSolved()) {
                showWinScreen();
                solved = true;
            }
        }

        puzzlePanel.repaint();
    }

    // Displays the completion message.
    private void showWinScreen() {
        if (completed) {
            return;
        }

        completed = true;
        solved = true;
        puzzlePanel.repaint();

        JOptionPane.showMessageDialog(
            this,
            "The final piece falls into place!\n\n"
                + "Light pours through the restored window.\n"
                + "The hotel looks just as it did before "
                + "the haunting began.\n\n"
                + "Your grandfather's final secret "
                + "is finally within reach!",
            "Stained Glass Restored!",
            JOptionPane.INFORMATION_MESSAGE
        );

        dispose();

        if (puzzleCompleteListener != null) {
            puzzleCompleteListener.run();
        }
    }

    // Resets the puzzle.
    private void resetPuzzle() {
        initializePuzzle();
        puzzlePanel.repaint();
    }

    // Main puzzle drawing panel.
    private class PuzzlePanel extends JPanel {
        public PuzzlePanel() {
            setBackground(backgroundColor);
            setLayout(null);

            JButton resetButton = new JButton("RESET");

            resetButton.setBounds(235, 620, 120, 35);

            resetButton.setFont(new Font("Serif", Font.BOLD, 15));

            resetButton.setForeground(Color.WHITE);
            resetButton.setBackground(new Color(75, 55, 90));

            resetButton.setFocusPainted(false);
            resetButton.setBorder(BorderFactory.createLineBorder(new Color(170, 130, 200), 2)
            );

            resetButton.addActionListener(e -> resetPuzzle());
            add(resetButton);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (!completed) {
                        handleClick(e.getX(), e.getY());
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Title.
            g2.setColor(accentColor);

            g2.setFont(new Font("Serif", Font.BOLD, 27));

            String title = "THE BROKEN STAINED GLASS";
            FontMetrics titleMetrics = g2.getFontMetrics();

            int titleX = (getWidth() - titleMetrics.stringWidth(title)) / 2;

            g2.drawString(title, titleX, 35);

            // Instructions.
            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(new Font("Arial", Font.PLAIN, 13));

            String instructions = "Swap any two pieces to complete the image and restore the hotel window!";

            FontMetrics instructionMetrics = g2.getFontMetrics();

            int instructionX = (getWidth() - instructionMetrics.stringWidth(instructions)) / 2;

            g2.drawString(instructions, instructionX, 62);

            // Moves counter.
            g2.setColor(new Color(220, 205, 235));
            g2.setFont(new Font("Arial", Font.BOLD, 15));
            g2.drawString("Moves: " + moves, BOARD_X, 105);

            // Draw completed-image preview.
            int previewSize = 65;
            int previewX = (getWidth() - previewSize) / 2;
            int previewY = 75;

            g2.drawImage(
                originalImage,
                previewX,
                previewY,
                previewSize,
                previewSize,
                null
            );

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            g2.drawString(
                "COMPLETED IMAGE",
                previewX - 12,
                previewY + previewSize + 12
            );

            // Draw all 16 puzzle pieces.
            for (int i = 0; i < board.length; i++) {
                int row = i / GRID_SIZE;
                int col = i % GRID_SIZE;
                int x = BOARD_X + col * CELL_SIZE;
                int y = BOARD_Y + row * CELL_SIZE;

                int pieceNumber = board[i];
                int originalRow = pieceNumber / GRID_SIZE;
                int originalCol = pieceNumber % GRID_SIZE;

                BufferedImage piece = pieces[originalRow][originalCol];

                g2.drawImage(
                    piece,
                    x,
                    y,
                    CELL_SIZE,
                    CELL_SIZE,
                    null
                );

                // Piece border.
                g2.setColor(new Color(20, 15, 30));
                g2.setStroke(new BasicStroke(3));

                g2.drawRect(
                    x,
                    y,
                    CELL_SIZE,
                    CELL_SIZE
                );

                // Highlight selected piece.
                if (selectedPiece == i) {
                    g2.setColor(new Color(255, 220, 100));
                    g2.setStroke(new BasicStroke(5));

                    g2.drawRect(
                        x + 2,
                        y + 2,
                        CELL_SIZE - 4,
                        CELL_SIZE - 4
                    );
                }
            }

            // Board border.
            g2.setColor(new Color(190, 145, 225));
            g2.setStroke(new BasicStroke(4));

            g2.drawRect(
                BOARD_X,
                BOARD_Y,
                GRID_SIZE * CELL_SIZE,
                GRID_SIZE * CELL_SIZE
            );

            // Bottom instructions.
            g2.setColor(Color.LIGHT_GRAY);

            g2.setFont(new Font("Arial", Font.PLAIN, 12));

            String bottomText = "Select one piece, then select another to swap them.";

            FontMetrics bottomMetrics = g2.getFontMetrics();

            int bottomX =
                (getWidth() - bottomMetrics.stringWidth(
                    bottomText
                )) / 2;

            g2.drawString(
                bottomText,
                bottomX,
                600
            );

            g2.dispose();
        }
    }

     public static void launch() {
        if (!isOpen && !solved) {
            isOpen = true;
            SwingUtilities.invokeLater(() -> new FinalRoomPuzzle4());
        }
    }

    public static boolean isOpen() {
        return isOpen;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
            () -> new FinalRoomPuzzle4()
        );
    }
}