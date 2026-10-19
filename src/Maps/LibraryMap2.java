package Maps;

import java.util.ArrayList;

import Level.Map;
import Level.NPC;
import NPCs.LibraryGhost;
import Scripts.ChangeMapScript;
import Scripts.Match2PuzzleScript;
import Scripts.TestMap.LibraryGhostScript;
import Tilesets.LibraryTileset;
import Tilesets.CleanLibraryTileset;
import Puzzles.Match2Puzzle;
 
public class LibraryMap2 extends Map {

    public LibraryMap2() {
        super("library_map2.txt", Match2Puzzle.solved ? new CleanLibraryTileset() : new LibraryTileset());
        if(Match2Puzzle.solved) {
            this.playerStartPosition = getMapTile(4, 11).getLocation();
        } else {
            this.playerStartPosition = getMapTile(12, 2).getLocation();
        }
    }

    @Override 
    public void loadScripts() {
        getMapTile(3, 11).setInteractScript(new Match2PuzzleScript());
        getMapTile(12, 1).setInteractScript(new ChangeMapScript(LobbyMap2::new));
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        
        LibraryGhost libraryGhost = new LibraryGhost(1, getMapTile(5, 11).getLocation().subtractX(30).subtractY(30));
        libraryGhost.setInteractScript(new LibraryGhostScript());
        npcs.add(libraryGhost);
        return npcs;
    }
    
}