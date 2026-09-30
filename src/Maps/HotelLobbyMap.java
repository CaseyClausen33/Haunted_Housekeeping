package Maps;

import Level.Map;
import Scripts.ChangeMapScript;
import Tilesets.HotelTileset;

public class HotelLobbyMap extends Map {

    public HotelLobbyMap() {
        super("hotel_lobby_map.txt", new HotelTileset());
        this.playerStartPosition = getMapTile(3, 12).getLocation();
    }
    @Override 
    public void loadScripts() {
    getMapTile(2, 15).setInteractScript(new ChangeMapScript(NewMap::new));
    }
    
}
