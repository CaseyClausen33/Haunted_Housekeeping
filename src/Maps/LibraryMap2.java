package Maps;

import Level.Map;
import Scripts.ChangeMapScript;
import Scripts.Match2PuzzleScript;
import Tilesets.LibraryTileset;

public class LibraryMap2 extends Map {

    public LibraryMap2() {
        super("library_map2.txt", new LibraryTileset());
        this.playerStartPosition = getMapTile(12, 2).getLocation();
    }

    @Override 
    public void loadScripts() {
        getMapTile(3, 11).setInteractScript(new Match2PuzzleScript());
        getMapTile(12, 1).setInteractScript(new ChangeMapScript(LobbyMap2::new));
    }
}