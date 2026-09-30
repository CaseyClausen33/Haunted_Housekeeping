package Puzzles;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Match2Puzzle extends JFrame {

    private static final int COLUMNS = 5;
    private static final int ROWS = 4;
    private static final int PAIR_COUNT = COLUMNS * ROWS / 2;
    private static final Color[] BOOK_COLORS = {
            new Color(194, 61, 55), new Color(231, 126, 46),
            new Color(220, 185, 55), new Color(99, 154, 76),
            new Color(43, 145, 129), new Color(52, 111, 181),
            new Color(90, 77, 158), new Color(174, 82, 171),
            new Color(218, 112, 147), new Color(125, 83, 56)
    };
    private static final Color BACKGROUND = new Color(25, 22, 30);
    private static final Color CARD_BACK = new Color(70, 55, 75);
    private static final Color CARD_FACE = new Color(224, 211, 190);
    private static final Color CARD_MATCHED = new Color(76, 112, 83);

    private final BookCardButton[] cards = new BookCardButton[COLUMNS * ROWS];
    private final int[] cardPairs = new int[COLUMNS * ROWS];
    private final boolean[] matched = new boolean[COLUMNS * ROWS];
    private final JLabel statusLabel = new JLabel("Pairs: 0 / 10     Moves: 0", SwingConstants.CENTER);

    private int firstSelection = -1;
    private int secondSelection = -1;
    private int moves;
    private int matchedPairs;
    private boolean resolvingTurn;
    private Timer revealTimer;

    public Match2Puzzle() {
        setTitle("Lobby Memory Puzzle");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(680, 590);
        setLocationRelativeTo(null);
        setResizable(false);

        createInterface();
        initializeGame();
        setVisible(true);
    }

    private void createInterface() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        panel.setBackground(BACKGROUND);

        JLabel titleLabel = new JLabel("THE LOST AND FOUND", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 25));
        titleLabel.setForeground(new Color(236, 222, 195));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel(new GridLayout(ROWS, COLUMNS, 10, 10));
        cardPanel.setOpaque(false);
        for (int index = 0; index < cards.length; index++) {
            final int cardIndex = index;
            BookCardButton card = new BookCardButton();
            card.setText("?");
            card.setFont(new Font("Serif", Font.BOLD, 20));
            card.setForeground(Color.WHITE);
            card.setBackground(CARD_BACK);
            card.setFocusPainted(false);
            card.setBorder(BorderFactory.createLineBorder(new Color(150, 120, 150), 2));
            card.addActionListener(event -> revealCard(cardIndex));
            cards[index] = card;
            cardPanel.add(card);
        }
        panel.add(cardPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout(12, 0));
        footer.setOpaque(false);
        statusLabel.setFont(new Font("Serif", Font.BOLD, 16));
        statusLabel.setForeground(Color.WHITE);
        footer.add(statusLabel, BorderLayout.CENTER);

        JButton resetButton = new JButton("RESET");
        resetButton.setFont(new Font("Serif", Font.BOLD, 14));
        resetButton.setForeground(Color.WHITE);
        resetButton.setBackground(CARD_BACK);
        resetButton.setFocusPainted(false);
        resetButton.setBorder(BorderFactory.createLineBorder(new Color(150, 120, 150), 2));
        resetButton.addActionListener(event -> initializeGame());
        footer.add(resetButton, BorderLayout.EAST);
        panel.add(footer, BorderLayout.SOUTH);

        add(panel);
    }

    private void initializeGame() {
        if (revealTimer != null) {
            revealTimer.stop();
        }

        List<Integer> shuffledPairs = new ArrayList<>();
        for (int colorIndex = 0; colorIndex < BOOK_COLORS.length; colorIndex++) {
            shuffledPairs.add(colorIndex);
            shuffledPairs.add(colorIndex);
        }
        Collections.shuffle(shuffledPairs);

        for (int index = 0; index < cards.length; index++) {
            cardPairs[index] = shuffledPairs.get(index);
            matched[index] = false;
            showCardBack(index);
        }

        firstSelection = -1;
        secondSelection = -1;
        moves = 0;
        matchedPairs = 0;
        resolvingTurn = false;
        updateStatus();
    }

    private void revealCard(int index) {
        if (resolvingTurn || matched[index] || index == firstSelection) {
            return;
        }

        showCardFace(index);
        if (firstSelection == -1) {
            firstSelection = index;
            return;
        }

        secondSelection = index;
        moves++;
        resolvingTurn = true;
        updateStatus();

        revealTimer = new Timer(800, event -> {
            ((Timer) event.getSource()).stop();
            if (cardPairs[firstSelection] == cardPairs[secondSelection]) {
                matched[firstSelection] = true;
                matched[secondSelection] = true;
                cards[firstSelection].setBackground(CARD_MATCHED);
                cards[secondSelection].setBackground(CARD_MATCHED);
                matchedPairs++;
            } else {
                showCardBack(firstSelection);
                showCardBack(secondSelection);
            }

            firstSelection = -1;
            secondSelection = -1;
            resolvingTurn = false;
            updateStatus();

            if (matchedPairs == PAIR_COUNT) {
                showWinScreen();
            }
        });
        revealTimer.setRepeats(false);
        revealTimer.start();
    }

    private void showCardFace(int index) {
        cards[index].setText("");
        cards[index].setBackground(CARD_FACE);
        cards[index].showBook(BOOK_COLORS[cardPairs[index]]);
    }

    private void showCardBack(int index) {
        cards[index].setText("?");
        cards[index].setForeground(Color.WHITE);
        cards[index].setBackground(CARD_BACK);
        cards[index].hideBook();
    }

    private static BufferedImage createBookImage(Color coverColor) {
        BufferedImage image = new BufferedImage(20, 24, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();

        graphics.setColor(coverColor.darker().darker());
        graphics.fillRect(6, 1, 11, 1);
        graphics.fillRect(4, 2, 14, 1);
        graphics.fillRect(3, 3, 16, 18);
        graphics.fillRect(4, 21, 14, 1);
        graphics.fillRect(6, 22, 11, 1);

        graphics.setColor(coverColor);
        graphics.fillRect(6, 2, 10, 1);
        graphics.fillRect(4, 3, 13, 18);
        graphics.fillRect(6, 21, 10, 1);

        graphics.setColor(coverColor.darker());
        graphics.fillRect(5, 4, 3, 16);
        graphics.setColor(new Color(246, 235, 207));
        graphics.fillRect(15, 4, 1, 16);
        graphics.setColor(new Color(238, 207, 132));
        graphics.fillRect(5, 6, 2, 1);
        graphics.fillRect(5, 17, 2, 1);
        graphics.fillRect(10, 9, 3, 1);
        graphics.fillRect(9, 10, 5, 1);
        graphics.fillRect(10, 11, 3, 1);

        graphics.dispose();
        return image;
    }

    private void updateStatus() {
        statusLabel.setText("Pairs: " + matchedPairs + " / " + PAIR_COUNT + "     Moves: " + moves);
    }

    private void showWinScreen() {
        JOptionPane.showMessageDialog(
                this,
                "You found all 10 pairs in " + moves + " moves!",
                "Puzzle Complete!",
                JOptionPane.INFORMATION_MESSAGE
        );
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Match2Puzzle::new);
    }

    private static class BookCardButton extends JButton {
        private BufferedImage bookImage;
        private boolean faceUp;

        private void showBook(Color color) {
            bookImage = createBookImage(color);
            faceUp = true;
            repaint();
        }

        private void hideBook() {
            faceUp = false;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if (!faceUp) {
                return;
            }

                Graphics2D graphics2D = (Graphics2D) graphics.create();
                graphics2D.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
                );

                int bookHeight = Math.min(72, getHeight() - 20);
                int bookWidth = bookHeight * 20 / 24;
            int bookX = (getWidth() - bookWidth) / 2;
            int bookY = (getHeight() - bookHeight) / 2;
                graphics2D.drawImage(bookImage, bookX, bookY, bookWidth, bookHeight, null);
            graphics2D.dispose();
        }
    }
}