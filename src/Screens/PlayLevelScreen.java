package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.Keyboard;
import Engine.KeyLocker;
import Engine.Screen;
import Engine.ScreenManager;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.*;
import Maps.*;
import Players.Character;
import Scripts.IntroCutsceneScript;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

// This class is for when the RPG game is actually being played
public class PlayLevelScreen extends Screen implements GameListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected PlayLevelScreenState playLevelScreenState;
    protected WinScreen winScreen;
    protected FlagManager flagManager;
    private final KeyLocker keyLocker = new KeyLocker();
    private boolean checklistVisible;

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public void initialize() {
        checklistVisible = false;
        keyLocker.unlockKey(Key.C);

        // setup state
        flagManager = new FlagManager();
        flagManager.addFlag("BathroomPuzzleSolved", false);
        flagManager.addFlag("BedroomPuzzleSolved", false); 
        flagManager.addFlag("KitchenPuzzleSolved", false);
        flagManager.addFlag("LibraryPuzzleSolved", false);

        flagManager.addFlag("FinalRoomReady", false);

        flagManager.addFlag("FinalBathroomPuzzleSolved", false);
        flagManager.addFlag("FinalBedroomPuzzleSolved", false);
        flagManager.addFlag("FinalKitchenPuzzleSolved", false);
        flagManager.addFlag("FinalLibraryPuzzleSolved", false);
        flagManager.addFlag("FinalRoomWinTriggered", false);
        

        // define/setup map
        //map = new TestMap();
        // Load the outside map first so the intro can play there
        

// Set up the initial game state
playLevelScreenState = PlayLevelScreenState.RUNNING;

// Start the intro cutscene if it hasn't already played
        if (!Maps.OutsideMap.cutsceneSeen) {
            loadMap(new OutsideMap());
            map.setActiveScript(new IntroCutsceneScript());
        } else {
            // If the intro was already completed, go straight to the lobby
            loadMap(new LobbyMap2());
        }

        winScreen = new WinScreen(this);
    }

    private void loadMap(Map nextMap) {
        if (nextMap == null) {
            return;
        }

        this.map = nextMap;
        this.map.setFlagManager(flagManager);

        if (player == null) {
            player = new Character(this.map.getPlayerStartPosition().x, this.map.getPlayerStartPosition().y);
        } else {
            player.setX(this.map.getPlayerStartPosition().x);
            player.setY(this.map.getPlayerStartPosition().y);
        }

        player.setMap(this.map);
        this.map.setPlayer(player);

        // let pieces of map know which button to listen for as the "interact" button
        this.map.getTextbox().setInteractKey(player.getInteractKey());

        // add this screen as a "game listener" so other areas of the game that don't normally have direct access to it (such as scripts) can "signal" to have it do something
        // this is used in the "onWin" method -- a script signals to this class that the game has been won by calling its "onWin" method
        this.map.addListener(this);

        // preloads all scripts ahead of time rather than loading them dynamically
        // both are supported, however preloading is recommended
        this.map.preloadScripts();
    }

    public void update() {
        // based on screen state, perform specific actions
        switch (playLevelScreenState) {
            // if level is "running" update player and map to keep game logic for the platformer level going
            case RUNNING:
                updateChecklistState();
                player.update();
                map.update(player);
                break;
            // if level has been completed, bring up level cleared screen
            case LEVEL_COMPLETED:
                winScreen.update();
                break;
        }
    }

    @Override
    public void onWin() {
        // when this method is called within the game, it signals the game has been "won"
        playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
    }

    @Override
    public void onMapChange(Map nextMap) {
        loadMap(nextMap);
    }

    public void draw(GraphicsHandler graphicsHandler) {
        // based on screen state, draw appropriate graphics
        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(player, graphicsHandler);
                if (checklistVisible) {
                    drawChecklist(graphicsHandler);
                }
                break;
            case LEVEL_COMPLETED:
                winScreen.draw(graphicsHandler);
                break;
        }
    }

    private void updateChecklistState() {
        if (Keyboard.isKeyDown(Key.C) && !keyLocker.isKeyLocked(Key.C)) {
            checklistVisible = !checklistVisible;
            keyLocker.lockKey(Key.C);
        }

        if (Keyboard.isKeyUp(Key.C)) {
            keyLocker.unlockKey(Key.C);
        }
    }

    private void drawChecklist(GraphicsHandler graphicsHandler) {
        Graphics2D graphics = (Graphics2D) graphicsHandler.getGraphics().create();
        try {
            int screenWidth = ScreenManager.getScreenWidth();
            int screenHeight = ScreenManager.getScreenHeight();
            int panelWidth = 430;
            int panelHeight = 330;
            int panelX = (screenWidth - panelWidth) / 2;
            int panelY = (screenHeight - panelHeight) / 2;

            graphics.setColor(new Color(0, 0, 0, 150));
            graphics.fillRect(0, 0, screenWidth, screenHeight);

            graphics.setColor(new Color(35, 29, 42, 245));
            graphics.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 18, 18);
            graphics.setStroke(new BasicStroke(3));
            graphics.setColor(new Color(231, 210, 166));
            graphics.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 18, 18);

            graphics.setFont(new Font("Arial", Font.BOLD, 27));
            graphics.drawString("Checklist", panelX + 34, panelY + 45);

            boolean bedroomSolved = flagManager.isFlagSet("BedroomPuzzleSolved");
            boolean bathroomSolved = flagManager.isFlagSet("BathroomPuzzleSolved");
            boolean librarySolved = flagManager.isFlagSet("LibraryPuzzleSolved");
            boolean kitchenSolved = flagManager.isFlagSet("KitchenPuzzleSolved");
            int rowY = panelY + 92;
            int rowSpacing = 43;

            drawChecklistItem(graphics, panelX + 42, rowY, "Bedroom", bedroomSolved);
            drawChecklistItem(graphics, panelX + 42, rowY + rowSpacing, "Bathroom", bathroomSolved);
            drawChecklistItem(graphics, panelX + 42, rowY + rowSpacing * 2, "Library", librarySolved);
            drawChecklistItem(graphics, panelX + 42, rowY + rowSpacing * 3, "Kitchen", kitchenSolved);

            boolean allRoomsSolved = bedroomSolved && bathroomSolved && librarySolved && kitchenSolved;
            drawChecklistItem(
                    graphics,
                    panelX + 42,
                    rowY + rowSpacing * 4,
                    allRoomsSolved ? "Top Floor" : "?????",
                    false);

            graphics.setFont(new Font("Arial", Font.PLAIN, 14));
            graphics.setColor(new Color(210, 200, 190));
            graphics.drawString("Press C to close", panelX + 300, panelY + panelHeight - 22);
        } finally {
            graphics.dispose();
        }
    }

    private void drawChecklistItem(Graphics2D graphics, int x, int y, String label, boolean completed) {
        int boxSize = 22;
        graphics.setStroke(new BasicStroke(2));
        graphics.setColor(new Color(231, 210, 166));
        graphics.drawRect(x, y - boxSize + 2, boxSize, boxSize);

        if (completed) {
            graphics.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            graphics.setColor(new Color(129, 218, 147));
            graphics.drawLine(x + 4, y - 10, x + 9, y - 5);
            graphics.drawLine(x + 9, y - 5, x + 19, y - 18);
        }

        graphics.setFont(new Font("Arial", Font.PLAIN, 20));
        graphics.setColor(Color.WHITE);
        graphics.drawString(label, x + 38, y);
    }

    public PlayLevelScreenState getPlayLevelScreenState() {
        return playLevelScreenState;
    }

    public void resetLevel() {
        initialize();
    }

    public void goBackToMenu() {
        screenCoordinator.setGameState(GameState.MENU);
    }

    // This enum represents the different states this screen can be in
    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED
    }
}