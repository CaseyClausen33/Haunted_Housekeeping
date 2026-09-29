package Maps;

import Level.*;
import Scripts.ChangeMapScript;
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

    }
}

