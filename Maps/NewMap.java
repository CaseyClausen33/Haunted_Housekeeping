package Maps;

import EnhancedMapTiles.PushableRock;
import Level.*;
import NPCs.Bug;
import NPCs.Dinosaur;
import NPCs.Walrus;
import Scripts.ChangeMapScript;
import Scripts.SimpleTextScript;
import Scripts.TestMap.*;
import Tilesets.CommonTileset;
import Maps.TitleScreenMap;

import java.util.ArrayList;

// Represents a test map to be used in a level
public class NewMap extends Map {

    public NewMap() {
        super("new_map.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(0, 0).getLocation();
    }

    @Override
    public void loadScripts() {
        getMapTile(1, 1).setInteractScript(new ChangeMapScript(TestMap::new));

    }
}

