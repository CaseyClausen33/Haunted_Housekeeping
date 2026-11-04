package Puzzles;

import Maps.LobbyMap2;
import Maps.OutsideMap;

// Keeps track of the story: which scenes have played and which puzzles are solved.
// Everything is static because a new map object is created every time the player enters a room.
public class StoryProgress {

    public enum Room { BATHROOM, KITCHEN, LIBRARY, BEDROOM, LOBBY }

    private static final boolean[] beforeSeen = new boolean[Room.values().length];
    private static final boolean[] afterSeen = new boolean[Room.values().length];
    private static final boolean[] reloadAttempted = new boolean[Room.values().length];
    private static final boolean[] reloadPending = new boolean[Room.values().length];

    // --- the stairs, the boss room and the ending ---
    public static volatile boolean allRoomsSceneSeen = false;   // lobby scene after the four rooms are done
    public static volatile boolean bossIntroSeen = false;       // Evelyn's confrontation when the player enters the boss room
    public static volatile boolean bossSolved = false;          // SET THIS TO TRUE when the boss room puzzle is beaten
    public static volatile boolean reunionSeen = false;         // Evelyn and Arthur's reunion
    public static volatile boolean epilogueActive = false;      // the lobby is full of guests (the final shot)
    public static volatile boolean epilogueSeen = false;

    public static boolean beforeSeen(Room room) { return beforeSeen[room.ordinal()]; }
    public static void setBeforeSeen(Room room) { beforeSeen[room.ordinal()] = true; }
    public static boolean afterSeen(Room room) { return afterSeen[room.ordinal()]; }
    public static void setAfterSeen(Room room) { afterSeen[room.ordinal()] = true; }

    // Reloading a room after its puzzle is solved is only tried once, so a failed reload can never loop
    public static boolean reloadAttempted(Room room) { return reloadAttempted[room.ordinal()]; }
    public static void markReloadStarted(Room room) {
        reloadAttempted[room.ordinal()] = true;
        reloadPending[room.ordinal()] = true;
    }

    // true once, for the map that is built by the reload (so it can put the player next to the puzzle)
    public static boolean consumeReload(Room room) {
        boolean pending = reloadPending[room.ordinal()];
        reloadPending[room.ordinal()] = false;
        return pending;
    }

    // Which puzzle belongs to which room. Change these lines if a room uses a different puzzle.
    public static boolean solved(Room room) {
        switch (room) {
            case BATHROOM: return BathroomPuzzle.solved;
            case KITCHEN:  return KitchenPuzzle.solved;
            case LIBRARY:  return Match2Puzzle.solved;
            case LOBBY: return LobbyPuzzle.solved;
            default:       return BedroomPuzzle.solved;
        }
    }

    public static boolean puzzleOpen(Room room) {
        switch (room) {
            case BATHROOM: return BathroomPuzzle.isOpen();
            case KITCHEN:  return KitchenPuzzle.isOpen();
            case LIBRARY:  return Match2Puzzle.isOpen();
            case LOBBY: return LobbyPuzzle.isOpen();
            default:       return BedroomPuzzle.isOpen();
        }
    }

    public static int solvedCount() {
        int count = 0;
        for (Room room : Room.values()) {
            if (solved(room)) {
                count++;
            }
        }
        return count;
    }

    public static boolean allSolved() {
        return solvedCount() == Room.values().length;
    }

    // Puts the whole story back to the start. Called when the game returns to the title screen,
    // so a new game starts fresh.
    public static void resetForNewGame() {
        for (int i = 0; i < beforeSeen.length; i++) {
            beforeSeen[i] = false;
            afterSeen[i] = false;
            reloadAttempted[i] = false;
            reloadPending[i] = false;
        }
        allRoomsSceneSeen = false;
        bossIntroSeen = false;
        bossSolved = false;
        reunionSeen = false;
        epilogueActive = false;
        epilogueSeen = false;

        BathroomPuzzle.solved = false;
        KitchenPuzzle.solved = false;
        Match2Puzzle.solved = false;
        BedroomPuzzle.solved = false;
        LobbyPuzzle.solved = false;

        LobbyMap2.lobbyIntroSeen = false;
        OutsideMap.cutsceneSeen = false;
    }
}