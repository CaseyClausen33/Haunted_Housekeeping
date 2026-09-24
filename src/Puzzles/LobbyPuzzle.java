// LobbyPuzzle.java

package Puzzles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class LobbyPuzzle extends JFrame {
  
    public LobbyPuzzle() {
        setTitle("Lobby Puzzle");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,400);
        setLocationRelativeTo(null);
        setResizable(false);
 
        PuzzlePanel puzzlePanel = new PuzzlePanel();
        add(puzzlePanel);

        setVisible(true);
   }

   public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LobbyPuzzle());
   }
 }

 class PuzzlePanel extends JPanel {

    private static final int SIZE = 4;
    private static final int TILE_SIZE = 50;
    private static final int BOARD_X = 100;
    private static final int BOARD_Y = 105;

    private int[] tiles = new int[16];

    private int emptyPosition;

    private int moves = 0;

    private Timer animationTimer;

    private boolean animating = false;

    private int movingTile;
    private int movingFrom;
    private int movingTo;

    private double animationProgress = 0.0;

    private JButton resetButton;

    private Random random = new Random();


    public PuzzlePanel() {

        setLayout(null);
        setBackground(new Color(25, 22, 30));

        createButtons();
        initializePuzzle();
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                 if (animating) {
                    return;
                }
                int mouseX = e.getX();
                int mouseY = e.getY();

                handleClick(mouseX, mouseY);
            }
        });
    }

    private void createButtons() {

        resetButton = new JButton("RESET");
        resetButton.setBounds(125, 325, 150, 35);
        resetButton.setFont(new Font("Serif", Font.BOLD, 16));

        resetButton.setForeground(Color.WHITE);

        resetButton.setBackground(new Color(70, 55, 75));

        resetButton.setFocusPainted(false);
        resetButton.setBorder(BorderFactory.createLineBorder(new Color(150, 120, 150), 2));
        resetButton.addActionListener(e -> initializePuzzle());

        add(resetButton);
    }

    private void initializePuzzle() {

        // Create solved board:
        for (int i = 0; i < 15; i++) {
            tiles[i] = i + 1;
        }

        // 0 represents the empty space
        tiles[15] = 0;
        emptyPosition = 15;
        moves = 0;
        animating = false;

        /*
         * Shuffle by making legal moves.
         * This guarantees that the puzzle remains solvable.
         */
        for (int i = 0; i < 150; i++) {
            int[] possibleMoves = getPossibleMoves();
            int randomPosition = possibleMoves[random.nextInt(possibleMoves.length)];

            swapTiles(randomPosition, emptyPosition);

            emptyPosition = randomPosition;
        }
        repaint();
    }

    private void handleClick(int mouseX, int mouseY) {

        // Check if click is inside board
        if (mouseX < BOARD_X ||
                mouseX > BOARD_X + SIZE * TILE_SIZE ||
                mouseY < BOARD_Y ||
                mouseY > BOARD_Y + SIZE * TILE_SIZE) {

            return;
        }

        // Find row and column
        int column = (mouseX - BOARD_X) / TILE_SIZE;
        int row = (mouseY - BOARD_Y) / TILE_SIZE;
        int position = row * SIZE + column;

        // Empty tile was clicked
        if (tiles[position] == 0) {
            return;
        }

        // Make sure tile is next to empty space
        if (!isAdjacent(position, emptyPosition)) {
            return;
        }

        // Start animation
        startAnimation(position);
    }

    private boolean isAdjacent(int position1, int position2) {

        int row1 = position1 / SIZE;
        int column1 = position1 % SIZE;

        int row2 = position2 / SIZE;
        int column2 = position2 % SIZE;

        // Same row
        if (row1 == row2 && Math.abs(column1 - column2) == 1) {
            return true;
        }

        // Same column
        if (column1 == column2 && Math.abs(row1 - row2) == 1) {
            return true;
        }
        return false;
    }

    private void startAnimation(int position) {

        animating = true;
        
        movingTile = tiles[position];
        movingFrom = position;
        movingTo = emptyPosition;

        animationProgress = 0.0;

        animationTimer = new Timer(15, e -> {

            animationProgress += 0.08;

            if (animationProgress >= 1.0) {
                animationProgress = 1.0;
                animationTimer.stop();
                finishMove();
            }
            repaint();
        });

        animationTimer.start();
    }

    private void finishMove() {

        // Actually move the tile
        tiles[movingTo] = movingTile;
        tiles[movingFrom] = 0;

        // Update empty space
        emptyPosition = movingFrom;
        moves++;
        animating = false;

        repaint();

        // Check for win
        if (checkWin()) {
            Timer winTimer = new Timer(250, e -> {
                ((Timer) e.getSource()).stop();
                showWinScreen();
            });
            winTimer.setRepeats(false);
            winTimer.start();
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < 15; i++) {
            if (tiles[i] != i + 1) {
                return false;
            }
        }
        return tiles[15] == 0;
    }

    private void showWinScreen() {

        JOptionPane.showMessageDialog(
                this,

                "✨ Lobby Restored! ✨\n\n" +
                "You put the hotel numbers back in order.\n\n" +
                "Congratulations you have completed the lobby! ",

                "Puzzle Complete!",

                JOptionPane.INFORMATION_MESSAGE
        );

        Window window = SwingUtilities.getWindowAncestor(this);

        if (window != null) {
            window.dispose();
        }
    }

    private int[] getPossibleMoves() {

        int row = emptyPosition / SIZE;

        int column = emptyPosition % SIZE;

        int[] possible = new int[4];

        int count = 0;

        // Up
        if (row > 0) {
            possible[count] = emptyPosition - SIZE;
            count++;
        }

        // Down
        if (row < SIZE - 1) {
            possible[count] = emptyPosition + SIZE;
            count++;
        }

        // Left
        if (column > 0) {
            possible[count] = emptyPosition - 1;
            count++;
        }

        // Right
        if (column < SIZE - 1) {
            possible[count] = emptyPosition + 1;
            count++;
        }

        int[] result = new int[count];
        System.arraycopy(possible, 0, result, 0, count);
        return result;
    }

    private void swapTiles(int position1, int position2) {
        int temp = tiles[position1];
        tiles[position1] = tiles[position2];
        tiles[position2] = temp;
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Better graphics
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(220, 205, 220));

        g2.setFont(new Font("Serif", Font.BOLD, 24));

        String title = "🕯  Restore The Lobby  🕯";

        FontMetrics titleMetrics = g2.getFontMetrics();

        int titleX =(getWidth() - titleMetrics.stringWidth(title)) / 2;

        g2.drawString(title, titleX, 55);

        g2.setFont(new Font("Serif", Font.PLAIN,15));

        g2.setColor(new Color(180, 170, 185));

        String instructions = "Put the numbers back in order!";

        int instructionX = (getWidth() - g2.getFontMetrics().stringWidth(instructions)) / 2;

        g2.drawString(instructions, instructionX, 78);

        g2.setFont(new Font( "SansSerif", Font.BOLD, 14));

        g2.setColor(new Color(200, 190, 200));

        g2.drawString("Moves: " + moves, 20, 100);

        for (int i = 0; i < 16; i++) {

            // Don't draw the moving tile here
            if (animating && i == movingFrom) {
                continue;
            }

            // Don't draw empty space
            if (tiles[i] == 0) {
                drawEmptyTile(g2, i);
                continue;
            }

            int x = BOARD_X + (i % SIZE) * TILE_SIZE;

            int y = BOARD_Y + (i / SIZE) * TILE_SIZE;

            drawTile(g2, tiles[i], x, y);
        }

        if (animating) {
            drawMovingTile(g2);
        }
        g2.dispose();
    }

    private void drawTile(Graphics2D g2, int number, int x, int y) {

        // Tile background
        g2.setColor(new Color(85, 65, 90));

        g2.fillRoundRect(x + 5, y + 5, TILE_SIZE - 10, TILE_SIZE - 10, 18, 18);

        // Tile border
        g2.setColor( new Color(150, 125, 155));

        g2.setStroke(new BasicStroke(3));

        g2.drawRoundRect(x + 5, y + 5, TILE_SIZE - 10, TILE_SIZE - 10, 18, 18);

        // Number
        g2.setColor(Color.WHITE);

        g2.setFont(new Font("Serif", Font.BOLD, 28 ));

        String text = String.valueOf(number);

        FontMetrics metrics = g2.getFontMetrics();

        int textX = x + (TILE_SIZE - metrics.stringWidth(text)) / 2;

        int textY = y + (TILE_SIZE - metrics.getHeight()) / 2 + metrics.getAscent();

        g2.drawString(text,textX,textY);
    }

    private void drawEmptyTile(
            Graphics2D g2,
            int position) {

        int x = BOARD_X + (position % SIZE) * TILE_SIZE;

        int y = BOARD_Y + (position / SIZE) * TILE_SIZE;

        g2.setColor(new Color(35, 32, 40));

        g2.fillRoundRect(x + 5, y + 5, TILE_SIZE - 10, TILE_SIZE - 10, 18, 18);
    }

    private void drawMovingTile(
            Graphics2D g2) {

        // Starting position
        int startX = BOARD_X + (movingFrom % SIZE) * TILE_SIZE;
        int startY = BOARD_Y + (movingFrom / SIZE) * TILE_SIZE;

        // Ending position
        int endX = BOARD_X + (movingTo % SIZE) * TILE_SIZE;
        int endY = BOARD_Y + (movingTo / SIZE) * TILE_SIZE;

       // Smooth interpolation.
        int currentX = (int)(startX + (endX - startX) * animationProgress);

        int currentY = (int)(startY + (endY - startY) * animationProgress);

        drawTile(g2, movingTile, currentX, currentY);
    }
}