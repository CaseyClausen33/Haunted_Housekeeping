package Maps;

import Level.*;
import Scripts.ChangeMapScript;
import Scripts.SimpleTextScript;
import Tilesets.CommonTileset;

// Represents a test map to be used in a level
public class NewMap extends Map {

    public NewMap() {
        super("new_map.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(0, 0).getLocation();
    }

    @Override
    public void loadScripts() {
        getMapTile(1, 1).setInteractScript(new ChangeMapScript(HotelLobbyMap::new));
        getMapTile(3, 4).setInteractScript(new SimpleTextScript("Welcome to the test map!"));
        getMapTile(4, 4).setInteractScript(new SimpleTextScript("If you're here, then you found something still in development!"));
    }
}

