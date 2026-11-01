package Game;

import Engine.GameWindow;
import Engine.Config;
import Engine.MusicPlayer;
import Engine.ScreenManager;

import java.nio.file.Paths;

/*
 * The game starts here
 * This class just starts up a GameWindow and attaches the ScreenCoordinator to the ScreenManager instance in the GameWindow
 * From this point on the ScreenCoordinator class will dictate what the game does
 */
public class Game {
    private final MusicPlayer musicPlayer =
            new MusicPlayer(Paths.get(Config.RESOURCES_PATH, "gamemusic.mp3"));

    public static void main(String[] args) {
        new Game();
    }

    public Game() {
        GameWindow gameWindow = new GameWindow();
        ScreenManager screenManager = gameWindow.getScreenManager();
        screenManager.setCurrentScreen(new ScreenCoordinator());
        Runtime.getRuntime().addShutdownHook(new Thread(musicPlayer::stop, "stop-game-music"));
        musicPlayer.startLooping();
        gameWindow.startGame();
    }
}
