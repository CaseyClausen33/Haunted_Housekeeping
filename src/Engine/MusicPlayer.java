package Engine;

import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class MusicPlayer {
    private final Path musicFile;
    private volatile boolean running;
    private volatile Player activePlayer;

    public MusicPlayer(Path musicFile) {
        this.musicFile = musicFile;
    }

    public synchronized void startLooping() {
        if (running) {
            return;
        }

        running = true;
        Thread playbackThread = new Thread(this::playLoop, "game-music");
        playbackThread.setDaemon(true);
        playbackThread.start();
    }

    public void stop() {
        running = false;
        Player player = activePlayer;
        if (player != null) {
            player.close();
        }
    }

    private void playLoop() {
        while (running) {
            Player player = null;
            try (InputStream input = new BufferedInputStream(Files.newInputStream(musicFile))) {
                player = new Player(input);
                activePlayer = player;
                if (running) {
                    player.play();
                }
            } catch (IOException | javazoom.jl.decoder.JavaLayerException e) {
                if (running) {
                    System.err.println("Unable to play game music from " + musicFile + ": " + e.getMessage());
                    running = false;
                }
            } finally {
                if (player != null) {
                    player.close();
                    if (activePlayer == player) {
                        activePlayer = null;
                    }
                }
            }
        }
    }
}
