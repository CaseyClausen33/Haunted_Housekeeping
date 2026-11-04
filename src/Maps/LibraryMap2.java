package Maps;

import java.util.ArrayList;

import Level.Map;
import Level.NPC;
import Level.Player;
import NPCs.LibraryGhost;
import Puzzles.Match2Puzzle;
import Puzzles.StoryProgress;
import Scripts.ChangeMapScript;
import Scripts.Match2PuzzleScript;
import Scripts.RoomStory;
import Scripts.TestMap.LibraryGhostScript;
import Scripts.TestMap.LibraryGhostScriptRestored;
import Tilesets.CleanLibraryTileset;
import Tilesets.LibraryTileset;

public class LibraryMap2 extends Map {

    // true when this map was built with the clean tileset (the puzzle was already solved)
    private final boolean clean = Match2Puzzle.solved;

    public LibraryMap2() {
        super("library_map2.txt", Match2Puzzle.solved ? new CleanLibraryTileset() : new LibraryTileset());
        // after the puzzle is solved the room reloads clean and the player appears beside the puzzle; otherwise at the door
        if (StoryProgress.consumeReload(StoryProgress.Room.LIBRARY)) {
            this.playerStartPosition = getMapTile(13, 5).getLocation();
        } else {
            this.playerStartPosition = getMapTile(12, 2).getLocation();
        }
    }

    // plays the library's before/after story scenes and swaps in the clean room once the puzzle is solved
    @Override
    public void update(Player player) {
        RoomStory.update(this, player, StoryProgress.Room.LIBRARY, () -> new ChangeMapScript(LibraryMap2::new), clean);
        super.update(player);
    }

    @Override
    public void loadScripts() {
        getMapTile(12, 1).setInteractScript(new ChangeMapScript(LobbyMap2::new));
        // the puzzle: the table with the open book
        getMapTile(13, 4).setInteractScript(new Match2PuzzleScript());
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        LibraryGhost ghost = new LibraryGhost(3, getMapTile(6, 5).getLocation().subtractX(20));
        if(Match2Puzzle.solved) {
            ghost.setInteractScript(new LibraryGhostScriptRestored());
        } else {
            ghost.setInteractScript(new LibraryGhostScript());
        }
        npcs.add(ghost);

        return npcs;
    }
}