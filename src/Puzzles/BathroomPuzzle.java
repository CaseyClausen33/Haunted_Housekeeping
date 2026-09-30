package Puzzles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class BathroomPuzzle extends JFrame {
    public BathroomPuzzle() {
        setTitle("Bathroom Pipe Puzzle");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        PipePuzzlePanel puzzlePanel = new PipePuzzlePanel();
        add(puzzlePanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BathroomPuzzle());
    }

    class PipePuzzlePanel extends JPanel {
        private static final int SIZE = 4;
        private static final int TILE_SIZE = 55;

        private static final int BOARD_X = 82;
        private static final int BOARD_Y = 75;

        private boolean[][][] connections = new boolean[SIZE][SIZE][4];
        private int moves = 0;
        private boolean solved = false;
        private Random random = new Random();
        private JButton resetButton;

        public PipePuzzlePanel() {
            setLayout(null);
            setBackground(new Color(25, 22, 30));

            createButtons();
            initializePuzzle();

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    handleClick(e.getX(), e.getY());
                }
            });
        }

        private void resetPuzzle() {
            initializePuzzle();
        }

        private void createButtons() {
            resetButton = new JButton("RESET");
            resetButton.setBounds(165, 320, 75, 30);
            resetButton.setFont(new Font("Serif", Font.BOLD, 12));

            resetButton.setForeground(Color.WHITE);
            resetButton.setBackground(new Color(70, 55, 75));
            resetButton.setFocusPainted(false);

            resetButton.setBorder(BorderFactory.createLineBorder(new Color(150, 120, 150), 2));
            resetButton.addActionListener(e -> resetPuzzle());
            add(resetButton);
        }
    
        private void initializePuzzle() {
            moves = 0;
            solved = false;

            createRandomSolvedPuzzle();

            // Randomly rotate the pipes to create the puzzle.
            for (int row = 0; row < SIZE; row++) {
                for (int column = 0; column < SIZE; column++) {
                    int rotations = random.nextInt(4);
                    for (int i = 0; i < rotations; i++) {

                        rotatePipe(row, column);
                    }
                }
         }
            // Ensure the puzzle is not already solved.
            if (checkWin()) {
                rotatePipe(1, 1);
            }

            repaint();
        }


        // Creates the solved state of the puzzle.
        private void createRandomSolvedPuzzle() {
            // Clear board
             for (int row = 0; row < SIZE; row++) {
                for (int column = 0; column < SIZE; column++) {
                    for (int direction = 0; direction < 4; direction++) {
                        connections[row][column][direction] = false;
                    }
             }
            }
            boolean[][] visited = new boolean[SIZE][SIZE];
            generateMaze(0, 0, visited);
        }

        private void generateMaze(int row, int column, boolean[][] visited) {
            visited[row][column] = true;
            int[] directions = {0, 1, 2, 3};

            // Shuffle directions
            for (int i = directions.length - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                int temp = directions[i];

                directions[i] = directions[j];
                directions[j] = temp;
            }

            for (int direction : directions) {
                int newRow = row;
                int newColumn = column;

                if (direction == 0) newRow--;       // UP
                if (direction == 1) newColumn++;   // RIGHT
                if (direction == 2) newRow++;      // DOWN
                if (direction == 3) newColumn--;   // LEFT

                if (newRow < 0 || newRow >= SIZE ||
                    newColumn < 0 || newColumn >= SIZE) {
                    continue;
                }

                if (visited[newRow][newColumn]) {
                    continue;
                }

                // Connect current tile to next tile
                connections[row][column][direction] = true;

                int opposite = (direction + 2) % 4;
                connections[newRow][newColumn][opposite] = true;

                // Continue generating
                generateMaze(newRow, newColumn, visited);
            }
        }

        // Handles a mouse click on the puzzle board.
        private void handleClick(int mouseX, int mouseY) {
            if (solved) {
                return;
            }
    
        // Check if the click is outside the board.
            if (mouseX < BOARD_X || mouseX >= BOARD_X + SIZE * TILE_SIZE || mouseY < BOARD_Y || mouseY >= BOARD_Y + SIZE * TILE_SIZE) {
                return;
            }

            // Figures out which tile was clicked.
            int column = (mouseX - BOARD_X) / TILE_SIZE;
            int row = (mouseY - BOARD_Y) / TILE_SIZE;
        
            // Rotate the pipe at the clicked tile.
            rotatePipe(row, column);
            moves++;
            repaint();

            // Check if the puzzle is solved.
            if (checkWin()) {
                solved = true;

                Timer winTimer = new Timer(
                        250,
                        e -> {
                            ((Timer) e.getSource()).stop();
                            showWinScreen();
                        }
                );
                winTimer.setRepeats(false);
                winTimer.start();
            }
        }

        //Rotates a pipe 90 degrees clockwise.
        private void rotatePipe(int row, int column) {
            boolean oldUp = connections[row][column][0];
            boolean oldRight = connections[row][column][1];
            boolean oldDown = connections[row][column][2];
            boolean oldLeft = connections[row][column][3];

            connections[row][column][0] = oldLeft;
            connections[row][column][1] = oldUp;
            connections[row][column][2] = oldRight;
            connections[row][column][3] = oldDown;
        }

        // Checks if the puzzle is solved by seeing if the water can reach the drain.
        private boolean checkWin() {
            boolean[][] visited = new boolean[SIZE][SIZE];
            return canReachDrain(0, 0, visited);
        }

        // Recursively checks if the water can reach the drain from the given tile.
        private boolean canReachDrain(int row, int column, boolean[][] visited) {
            if (row < 0 || row >= SIZE || column < 0 || column >= SIZE) {
                return false;
            }

            if (visited[row][column]) {
                return false;
            }

            visited[row][column] = true;

            // If we reached the drain tile, return true.
            if (row == SIZE - 1 && column == SIZE - 1) {
                return true;
            }   

            // Check UP.
            if (connections[row][column][0]) {
                if (row > 0 && connections[row - 1][column][2]) {
                    if (canReachDrain(row - 1, column, visited)) {
                        return true;
                    }
                }
            }

            // Check RIGHT.
            if (connections[row][column][1]) {
                if (column < SIZE - 1 && connections[row][column + 1][3]) {
                    if (canReachDrain(row, column + 1, visited)) {
                        return true;
                    }
                }
            }

            // Check DOWN.
            if (connections[row][column][2]) {
                if (row < SIZE - 1 && connections[row + 1][column][0]) {
                    if (canReachDrain(row + 1, column, visited)) {
                        return true;
                    }
                }
            }

        // Check LEFT.
            if (connections[row][column][3]) {
                if (column > 0 && connections[row][column - 1][1]) {
                    if (canReachDrain(row, column - 1, visited)) {
                        return true;
                    }
                }
            }

            return false;
        }

        // Shows the win screen when the puzzle is solved.
        private void showWinScreen() {
            JOptionPane.showMessageDialog(
                    this,
                    "✨ Bathroom Restored! ✨\n\n" +
                    "You connected the pipes!\n\n" +
                    "The water is flowing again!",
                    "Puzzle Complete!",

                    JOptionPane.INFORMATION_MESSAGE
            );

            Window window = SwingUtilities.getWindowAncestor(this);

            if (window != null) {
                window.dispose();
            }
        }  

        // Draws the puzzle board and all the tiles.
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 =(Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(220, 205, 220));
            g2.setFont(new Font("Serif", Font.BOLD, 27));

            String title ="Fix The Pipes";

            FontMetrics titleMetrics = g2.getFontMetrics();
            int titleX = (getWidth() - titleMetrics.stringWidth(title)) / 2;

            g2.drawString(title, titleX, 35);
            g2.setFont(new Font("Serif", Font.PLAIN, 14) );
            g2.setColor(new Color(180, 170, 185));

            String instructions = "Rotate the pipes to reach the drain!";
            int instructionX = (getWidth() - g2.getFontMetrics().stringWidth(instructions)) / 2;
            g2.drawString(instructions, instructionX, 55);

            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.setColor(new Color(200, 190, 200));
            g2.drawString("Moves: " + moves, 15, 70);

            for (int row = 0; row < SIZE; row++) {
                for (int column = 0; column < SIZE; column++) {
                    int x = BOARD_X + column * TILE_SIZE;
                    int y = BOARD_Y + row * TILE_SIZE;
                    drawTile(g2, row, column, x, y);
                }
            }
            g2.dispose();
    }

        // Draws a single tile at the specified row and column.
        private void drawTile(Graphics2D g2, int row, int column, int x,int y) {
            g2.setColor(new Color(55, 48, 55));
            g2.fillRoundRect(x + 3, y + 3, TILE_SIZE - 6, TILE_SIZE - 6, 12, 12);
            g2.setColor(new Color(130, 105, 135));
        
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x + 3, y + 3, TILE_SIZE - 6, TILE_SIZE - 6, 12, 12);
            drawPipe(g2, row, column, x, y);
        }

        // Draws the pipe connections for a tile at the specified row and column.
        private void drawPipe(Graphics2D g2, int row, int column, int x, int y) {
            int centerX = x + TILE_SIZE / 2;
            int centerY = y + TILE_SIZE / 2;

            g2.setColor(new Color(175, 95, 45));
            g2.setStroke(new BasicStroke(12, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            // UP
            if (connections[row][column][0]) {
                g2.drawLine(centerX, centerY, centerX, y + 5);
            }

            // RIGHT
            if (connections[row][column][1]) {
                g2.drawLine(centerX, centerY, x + TILE_SIZE - 5, centerY);
            }

            // DOWN
            if (connections[row][column][2]) {
                g2.drawLine(centerX, centerY, centerX, y + TILE_SIZE - 5);
            }
        
            // LEFT
            if (connections[row][column][3]) {
                g2.drawLine(centerX, centerY, x + 5, centerY);
            }   

            g2.setColor(new Color(225, 145, 75));
            g2.setStroke(new BasicStroke(3));

            g2.fillOval(centerX - 7, centerY - 7, 14, 14);

            if (row == 0 && column == 0) {
                g2.setColor(new Color(80, 170, 255));
                g2.fillOval(centerX - 8, centerY - 8, 16, 16);
          
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                g2.drawString("W", centerX - 4, centerY + 4);
            }

            if (row == SIZE - 1 && column == SIZE - 1) {
                g2.setColor(new Color(35, 35, 40));
                g2.fillOval(centerX - 12, centerY - 12, 24, 24);

                g2.setColor(new Color(120, 120, 130));
                g2.setStroke(new BasicStroke(2));

                g2.drawOval(centerX - 12, centerY - 12, 24, 24);

                g2.drawLine(centerX - 7, centerY - 5, centerX + 7, centerY + 5);
                g2.drawLine(centerX + 7, centerY - 5, centerX - 7, centerY + 5);
            }
        }
    }
}