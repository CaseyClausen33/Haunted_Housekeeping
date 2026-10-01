// KitchenPuzzle.java

package Puzzles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class KitchenPuzzle extends JFrame {

    public KitchenPuzzle() {
        setTitle("Kitchen Puzzle");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        KitchenPanel kitchenPanel = new KitchenPanel();
        add(kitchenPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KitchenPuzzle());
    }
}

class KitchenPanel extends JPanel {

    private static final int SIZE = 4;
    private static final int TILE_SIZE = 50;
    private static final int BOARD_X = 100;
    private static final int BOARD_Y = 105;

    // true = burner is lit
    private boolean[] burners = new boolean[SIZE * SIZE];

    // 0.0 = fully off, 1.0 = fully glowing (used for smooth fading)
    private double[] glow = new double[SIZE * SIZE];

    private int moves = 0;

    private boolean won = false;

    private Timer glowTimer;

    private JButton resetButton;

    private Random random = new Random();


    public KitchenPanel() {

        setLayout(null);
        setBackground(new Color(25, 22, 30));

        createButtons();
        initializePuzzle();
        startGlowTimer();

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (won) {
                    return;
                }
                handleClick(e.getX(), e.getY());
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
        resetButton.addActionListener(e -> {
            if (!won) {
                initializePuzzle();
            }
        });

        add(resetButton);
    }

    private void initializePuzzle() {

        // Start with every burner off (solved)
        for (int i = 0; i < burners.length; i++) {
            burners[i] = false;
        }

        moves = 0;
        won = false;

        /*
         * Shuffle by pressing random burners.
         * Every press is reversible, so this guarantees the puzzle is solvable.
         * Repeat until the board is not already solved.
         */
        do {
            for (int i = 0; i < 20; i++) {
                pressBurner(random.nextInt(burners.length));
            }
        } while (checkWin());

        repaint();
    }

    private void startGlowTimer() {

        glowTimer = new Timer(20, e -> {

            boolean changed = false;

            for (int i = 0; i < glow.length; i++) {

                double target = burners[i] ? 1.0 : 0.0;

                if (glow[i] < target) {
                    glow[i] = Math.min(target, glow[i] + 0.12);
                    changed = true;
                } else if (glow[i] > target) {
                    glow[i] = Math.max(target, glow[i] - 0.12);
                    changed = true;
                }
            }

            if (changed) {
                repaint();
            }
        });

        glowTimer.start();
    }

    @Override
    public void removeNotify() {
        // Stop the timer when the window is closed
        if (glowTimer != null) {
            glowTimer.stop();
        }
        super.removeNotify();
    }

    private void handleClick(int mouseX, int mouseY) {

        // Check if click is inside board
        if (mouseX < BOARD_X ||
                mouseX >= BOARD_X + SIZE * TILE_SIZE ||
                mouseY < BOARD_Y ||
                mouseY >= BOARD_Y + SIZE * TILE_SIZE) {

            return;
        }

        // Find row and column
        int column = (mouseX - BOARD_X) / TILE_SIZE;
        int row = (mouseY - BOARD_Y) / TILE_SIZE;
        int position = row * SIZE + column;

        pressBurner(position);
        moves++;

        repaint();

        // Check for win
        if (checkWin()) {
            won = true;

            Timer winTimer = new Timer(500, e -> {
                ((Timer) e.getSource()).stop();
                showWinScreen();
            });
            winTimer.setRepeats(false);
            winTimer.start();
        }
    }

    // Toggles the burner at position and its up/down/left/right neighbors
    private void pressBurner(int position) {

        int row = position / SIZE;
        int column = position % SIZE;

        burners[position] = !burners[position];

        // Up
        if (row > 0) {
            burners[position - SIZE] = !burners[position - SIZE];
        }

        // Down
        if (row < SIZE - 1) {
            burners[position + SIZE] = !burners[position + SIZE];
        }

        // Left
        if (column > 0) {
            burners[position - 1] = !burners[position - 1];
        }

        // Right
        if (column < SIZE - 1) {
            burners[position + 1] = !burners[position + 1];
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < burners.length; i++) {
            if (burners[i]) {
                return false;
            }
        }
        return true;
    }

    private void showWinScreen() {

        JOptionPane.showMessageDialog(
                this,

                "✨ Kitchen Secured! ✨\n\n" +
                "You turned off every burner.\n\n" +
                "Congratulations you have completed the kitchen! ",

                "Puzzle Complete!",

                JOptionPane.INFORMATION_MESSAGE
        );

        Window window = SwingUtilities.getWindowAncestor(this);

        if (window != null) {
            window.dispose();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Better graphics
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(220, 205, 220));

        g2.setFont(new Font("Serif", Font.BOLD, 24));

        String title = "🔥  Cool Down The Kitchen  🔥";

        FontMetrics titleMetrics = g2.getFontMetrics();

        int titleX = (getWidth() - titleMetrics.stringWidth(title)) / 2;

        g2.drawString(title, titleX, 55);

        g2.setFont(new Font("Serif", Font.PLAIN, 15));

        g2.setColor(new Color(180, 170, 185));

        String instructions = "Turn off every burner! Each one affects its neighbors.";

        int instructionX = (getWidth() - g2.getFontMetrics().stringWidth(instructions)) / 2;

        g2.drawString(instructions, instructionX, 78);

        g2.setFont(new Font("SansSerif", Font.BOLD, 14));

        g2.setColor(new Color(200, 190, 200));

        g2.drawString("Moves: " + moves, 20, 100);

        // Stovetop background
        g2.setColor(new Color(40, 36, 46));
        g2.fillRoundRect(BOARD_X - 6, BOARD_Y - 6,
                SIZE * TILE_SIZE + 12, SIZE * TILE_SIZE + 12, 20, 20);

        for (int i = 0; i < burners.length; i++) {

            int x = BOARD_X + (i % SIZE) * TILE_SIZE;

            int y = BOARD_Y + (i / SIZE) * TILE_SIZE;

            drawBurner(g2, glow[i], x, y);
        }

        g2.dispose();
    }

    private void drawBurner(Graphics2D g2, double heat, int x, int y) {

        int cx = x + TILE_SIZE / 2;
        int cy = y + TILE_SIZE / 2;

        // Soft glow halo behind lit burners
        if (heat > 0.01) {
            g2.setColor(new Color(255, 120, 30, (int) (70 * heat)));
            g2.fillOval(cx - 24, cy - 24, 48, 48);
        }

        // Burner base color fades from cold gray to hot orange
        Color cold = new Color(60, 55, 65);
        Color hot = new Color(255, 130, 40);
        g2.setColor(blend(cold, hot, heat));
        g2.fillOval(cx - 19, cy - 19, 38, 38);

        // Outer ring
        Color coldRing = new Color(110, 95, 115);
        Color hotRing = new Color(255, 200, 100);
        g2.setColor(blend(coldRing, hotRing, heat));
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(cx - 19, cy - 19, 38, 38);

        // Inner ring
        g2.setStroke(new BasicStroke(2));
        g2.setColor(blend(new Color(45, 40, 50), new Color(255, 230, 150), heat));
        g2.drawOval(cx - 10, cy - 10, 20, 20);

        // Center dot
        g2.setColor(blend(new Color(30, 27, 35), new Color(255, 245, 200), heat));
        g2.fillOval(cx - 4, cy - 4, 8, 8);
    }

    private Color blend(Color a, Color b, double t) {
        int r = (int) (a.getRed() + (b.getRed() - a.getRed()) * t);
        int g = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int bl = (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t);
        return new Color(r, g, bl);
    }
}