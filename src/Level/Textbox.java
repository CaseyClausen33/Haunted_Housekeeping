package Level;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.ScreenManager;
import SpriteFont.SpriteFont;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

// Represents the game's textbox
// Will display the text it is given to its textQueue
// Each String in the textQueue will be displayed in the textbox, and hitting the interact key will cycle between additional Strings in the queue
// Long lines are wrapped automatically, and the box grows (and optionally shrinks) to fit its text.
// A newline character in a String still forces a line break.
// Also supports adding options for a player to select from
public class Textbox {

    // ---------------------------------------------------------------------------------------------
    // Look and feel: change these to restyle every textbox in the game
    // ---------------------------------------------------------------------------------------------
    // Font: either a font installed on the machine (a name like "Serif", "Georgia", "Palatino Linotype") or a
    // .ttf file shipped with the game. If FONT_FILE is not null and loads, it wins; otherwise FONT_NAME is used.
    private static final String FONT_NAME = "Serif";
    private static final String FONT_FILE = null;              // e.g. "Resources/Fonts/MyFont.ttf"
    private static final int FONT_STYLE = Font.BOLD;
    private static final int FONT_SIZE = 24;
    private static final int LINE_GAP = 8;                      // extra pixels between lines

    private static final Color TEXT_COLOR = new Color(236, 222, 195);
    private static final Color BOX_FILL = new Color(25, 22, 30);
    private static final Color BOX_BORDER = new Color(150, 120, 150);
    private static final int BORDER_THICKNESS = 3;

    // Box sizing
    private static final int PADDING = 16;                      // space between the border and the text
    private static final int MIN_WIDTH = 260;
    private static final int MAX_WIDTH = 750;
    private static final int MIN_HEIGHT = 70;
    private static final boolean SHRINK_WIDTH_TO_TEXT = true;   // false = always MAX_WIDTH wide, like the original box

    // Placement (the box sits at the bottom of the screen, or the top when the camera is at the bottom of the map)
    private static final int BOTTOM_MARGIN = 40;
    private static final int TOP_MARGIN = 22;

    // Options box (the choices a player can pick between)
    private static final int OPTION_MIN_WIDTH = 92;
    private static final int OPTION_SPACING = FONT_SIZE + 10;
    private static final int OPTION_POINTER_SIZE = 10;
    private static final int OPTION_GAP_FROM_TEXTBOX = 10;

    private static final Font FONT = loadFont();
    private static final FontRenderContext FRC = new FontRenderContext(null, true, true);

    // whether textbox is shown or not
    protected boolean isActive;

    // core vars that make textbox work
    private Queue<TextboxItem> textQueue;
    private TextboxItem currentTextItem;
    protected int selectedOptionIndex = 0;
    private SpriteFont text = null;
    private ArrayList<SpriteFont> options = null;
    private KeyLocker keyLocker = new KeyLocker();
    private Key interactKey = Key.SPACE;

    // layout of the current text item (recalculated every time a new item is shown)
    private int boxX, boxY, boxWidth, boxHeight;
    private boolean drawnAtBottom = true;
    private int optionX, optionY, optionWidth, optionHeight;

    private Map map;

    public Textbox(Map map) {
        this.map = map;
        this.textQueue = new LinkedList<>();
    }

    private static Font loadFont() {
        if (FONT_FILE != null) {
            try {
                return Font.createFont(Font.TRUETYPE_FONT, new File(FONT_FILE)).deriveFont(FONT_STYLE, (float) FONT_SIZE);
            } catch (Exception e) {
                System.out.println("Textbox: could not load " + FONT_FILE + ", using " + FONT_NAME + " instead");
            }
        }
        return new Font(FONT_NAME, FONT_STYLE, FONT_SIZE);
    }

    public void addText(String text) {
        if (textQueue.isEmpty()) {
            keyLocker.lockKey(interactKey);
        }
        textQueue.add(new TextboxItem(text));
    }

    public void addText(String[] text) {
        if (textQueue.isEmpty()) {
            keyLocker.lockKey(interactKey);
        }
        for (String textItem : text) {
            textQueue.add(new TextboxItem(textItem));
        }
    }

    public void addText(TextboxItem text) {
        if (textQueue.isEmpty()) {
            keyLocker.lockKey(interactKey);
        }
        textQueue.add(text);
    }

    public void addText(TextboxItem[] text) {
        if (textQueue.isEmpty()) {
            keyLocker.lockKey(interactKey);
        }
        for (TextboxItem textItem : text) {
            textQueue.add(textItem);
        }
    }

    // returns whether the textQueue is out of items to display or not
    // useful for scripts to know when to complete
    public boolean isTextQueueEmpty() {
        return textQueue.isEmpty();
    }

    public void update() {
        // if textQueue has more text to display and the interact key button was pressed previously, display new text
        if (!textQueue.isEmpty() && keyLocker.isKeyLocked(interactKey)) {
            currentTextItem = textQueue.peek();
            options = null;

            layoutText(currentTextItem.getText());

            // if there are options associated with this text item, prepare the option text and box
            if (currentTextItem.getOptions() != null) {
                layoutOptions(currentTextItem.getOptions());
                selectedOptionIndex = 0;
            }
        }
        // if interact key is pressed, remove the current text from the queue to prepare for the next text item to be displayed
        if (Keyboard.isKeyDown(interactKey) && !keyLocker.isKeyLocked(interactKey)) {
            keyLocker.lockKey(interactKey);
            textQueue.poll();

            // if an option was selected, set output manager flag to the index of the selected option
            // a script can then look at output manager later to see which option was selected and do with that information what it wants
            if (options != null) {
                map.getActiveScript().getScriptActionOutputManager().addFlag("TEXTBOX_OPTION_SELECTION", selectedOptionIndex);
            }
        }
        else if (Keyboard.isKeyUp(interactKey)) {
            keyLocker.unlockKey(interactKey);
        }

        if (options != null) {
            if (Keyboard.isKeyDown(Key.DOWN) && !keyLocker.isKeyLocked(Key.DOWN)) {
                keyLocker.lockKey(Key.DOWN);
                if (selectedOptionIndex < options.size() - 1) {
                    selectedOptionIndex++;
                }
            }
            if (Keyboard.isKeyDown(Key.UP) && !keyLocker.isKeyLocked(Key.UP)) {
                keyLocker.lockKey(Key.UP);
                if (selectedOptionIndex > 0) {
                    selectedOptionIndex--;
                }
            }
            if (Keyboard.isKeyUp(Key.DOWN)) {
                keyLocker.unlockKey(Key.DOWN);
            }
            if (Keyboard.isKeyUp(Key.UP)) {
                keyLocker.unlockKey(Key.UP);
            }
        }
    }

    // wraps the text to the box width, works out how big the box needs to be, and creates the text to draw
    private void layoutText(String rawText) {
        int maxTextWidth = MAX_WIDTH - 2 * PADDING - 6;   // small safety margin: wrapping is measured, drawing may differ by a pixel or two
        ArrayList<String> lines = wrap(rawText, maxTextWidth);

        double widest = 0;
        for (String line : lines) {
            widest = Math.max(widest, FONT.getStringBounds(line, FRC).getWidth());
        }

        int contentWidth = (int) Math.ceil(widest) + 2 * PADDING + 6;
        boxWidth = SHRINK_WIDTH_TO_TEXT ? Math.max(MIN_WIDTH, Math.min(MAX_WIDTH, contentWidth)) : MAX_WIDTH;

        int lineAdvance = FONT_SIZE + LINE_GAP;
        int textHeight = lines.size() * lineAdvance - LINE_GAP + 6;   // +6 leaves room for letters that hang below the line (g, y, p)
        boxHeight = Math.max(MIN_HEIGHT, textHeight + 2 * PADDING);

        boxX = (ScreenManager.getScreenWidth() - boxWidth) / 2;

        // if camera is at bottom of map, textbox is drawn at top of screen instead of the bottom like usual
        // to prevent it from covering the player
        drawnAtBottom = !map.getCamera().isAtBottomOfMap();
        boxY = drawnAtBottom ? ScreenManager.getScreenHeight() - BOTTOM_MARGIN - boxHeight : TOP_MARGIN;

        int textY = boxY + (boxHeight - textHeight) / 2;
        text = new SpriteFont(String.join("\n", lines), boxX + PADDING, textY, FONT, TEXT_COLOR);
    }

    private void layoutOptions(ArrayList<String> optionTexts) {
        double widest = 0;
        for (String option : optionTexts) {
            widest = Math.max(widest, FONT.getStringBounds(option, FRC).getWidth());
        }
        int pointerSpace = OPTION_POINTER_SIZE + 14;
        optionWidth = Math.max(OPTION_MIN_WIDTH, (int) Math.ceil(widest) + 2 * PADDING + pointerSpace);
        optionHeight = optionTexts.size() * OPTION_SPACING + 2 * PADDING - 10;

        // the options box sits just above the textbox (or just below it when the textbox is at the top), lined up with its right edge
        optionX = boxX + boxWidth - optionWidth;
        optionY = drawnAtBottom ? boxY - optionHeight - OPTION_GAP_FROM_TEXTBOX : boxY + boxHeight + OPTION_GAP_FROM_TEXTBOX;

        options = new ArrayList<>();
        for (int i = 0; i < optionTexts.size(); i++) {
            options.add(new SpriteFont(optionTexts.get(i), optionX + PADDING + pointerSpace, optionY + PADDING - 4 + i * OPTION_SPACING, FONT, TEXT_COLOR));
        }
    }

    // splits text into lines no wider than maxWidth pixels, keeping any line breaks that were already in the text
    private ArrayList<String> wrap(String rawText, int maxWidth) {
        ArrayList<String> lines = new ArrayList<>();
        for (String paragraph : rawText.split("\n", -1)) {
            String current = "";
            for (String word : paragraph.split(" ")) {
                String attempt = current.isEmpty() ? word : current + " " + word;
                if (current.isEmpty() || FONT.getStringBounds(attempt, FRC).getWidth() <= maxWidth) {
                    current = attempt;
                } else {
                    lines.add(current);
                    current = word;
                }
            }
            lines.add(current);
        }
        return lines;
    }

    public void draw(GraphicsHandler graphicsHandler) {
        if (text == null) {
            return;
        }

        // draw textbox
        graphicsHandler.drawFilledRectangleWithBorder(boxX, boxY, boxWidth, boxHeight, BOX_FILL, BOX_BORDER, BORDER_THICKNESS);

        // draw text in textbox
        text.drawWithParsedNewLines(graphicsHandler, LINE_GAP);

        if (options != null) {
            // draw options textbox
            graphicsHandler.drawFilledRectangleWithBorder(optionX, optionY, optionWidth, optionHeight, BOX_FILL, BOX_BORDER, BORDER_THICKNESS);

            // draw each option text
            for (SpriteFont option : options) {
                option.draw(graphicsHandler);
            }

            // draw option selection indicator (small square)
            int pointerY = optionY + PADDING + 4 + selectedOptionIndex * OPTION_SPACING;
            graphicsHandler.drawFilledRectangle(optionX + PADDING, pointerY, OPTION_POINTER_SIZE, OPTION_POINTER_SIZE, TEXT_COLOR);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setInteractKey(Key interactKey) {
        this.interactKey = interactKey;
    }

}