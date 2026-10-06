package Maps;

import Level.Map;
import Scripts.Match2PuzzleScript;
import Tilesets.HotelTileset;

public class LibraryMap extends Map {

    public LibraryMap() {
        super("library_map.txt", new HotelTileset());
        this.playerStartPosition = getMapTile(10, 14).getLocation();
    }

    @Override
    public void loadScripts() {
        getMapTile(10, 7).setInteractScript(new Match2PuzzleScript());
    }
}