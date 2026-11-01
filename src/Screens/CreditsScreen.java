package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;
import java.awt.*;

// This class is for the credits screen
public class CreditsScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected SpriteFont creditsLabel;
    protected SpriteFont createdByLabel;
    protected SpriteFont returnInstructionsLabel;
    protected SpriteFont moveInstructionsLabel;
    protected SpriteFont interactInstructionsLabel;
    protected SpriteFont puzzleInstructionsLabel;
    // I only need one color for this screen
    protected Color purpleText = new Color(54, 49, 83);

    public CreditsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        creditsLabel = new SpriteFont("Credits", 15, 7, "Arial", 30, purpleText);
        creditsLabel.setOutlineColor(Color.white);
        creditsLabel.setOutlineThickness(2);

        createdByLabel = new SpriteFont("Created by The GhostBusters", 130, 121, "Arial", 24, purpleText);
        createdByLabel.setOutlineColor(Color.white);
        createdByLabel.setOutlineThickness(2);

        returnInstructionsLabel = new SpriteFont("Press SPACE to return to main menu", 20, 500, "Arial", 24, purpleText);
        returnInstructionsLabel.setOutlineColor(Color.white);
        returnInstructionsLabel.setOutlineThickness(2);
        moveInstructionsLabel = new SpriteFont("Use the Arrow Keys to move around", 50, 350, "Arial", 24, purpleText);
        moveInstructionsLabel.setOutlineColor(Color.white);
        moveInstructionsLabel.setOutlineThickness(2);

        interactInstructionsLabel = new SpriteFont("Use SPACE to interact with ghosts and objects", 50, 380, "Arial", 24, purpleText);
        interactInstructionsLabel.setOutlineColor(Color.white);
        interactInstructionsLabel.setOutlineThickness(2);

        puzzleInstructionsLabel = new SpriteFont("When a puzzle window opens, use the mouse to solve it", 50, 410, "Arial", 24, purpleText);
        puzzleInstructionsLabel.setOutlineColor(Color.white);
        puzzleInstructionsLabel.setOutlineThickness(2);

        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        background.update(null);

        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        // if space is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            // wait until space is released to go back to main menu
            while (Keyboard.isKeyDown(Key.SPACE)) {
                /*  What are the odds I can put a silly easter egg in here and no one will know?
                 Here's some ASCII art of a ghost, have fun with the game!
                    .-"""-.
                   / O   O \
                  |         |
                  |  /\/\/\ |
                  '-'     '-'
                 */
            }
            screenCoordinator.setGameState(GameState.MENU);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        creditsLabel.draw(graphicsHandler);
        createdByLabel.draw(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);
        moveInstructionsLabel.draw(graphicsHandler);
        interactInstructionsLabel.draw(graphicsHandler);
        puzzleInstructionsLabel.draw(graphicsHandler);
    }
}
