package Maps;

import Level.Map;
import Level.NPC;
import NPCs.Ghost;
import Scripts.ChangeMapScript;
import Scripts.TestMap.GhostScript;
import Tilesets.HotelTileset;
import java.util.ArrayList;

public class BedroomMap extends Map {

    public BedroomMap() {
        super("bedroom_map.txt", new HotelTileset());
        this.playerStartPosition = getMapTile(1, 6).getLocation();
    }    

     @Override 
    public void loadScripts() {
    getMapTile(1, 7).setInteractScript(new ChangeMapScript(HotelLobbyMap::new));
    }
}

