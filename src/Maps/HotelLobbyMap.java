package Maps;

import Level.Map;
import Tilesets.HotelTileset;

public class HotelLobbyMap extends Map {

    public HotelLobbyMap() {
        super("hotel_lobby_map.txt", new HotelTileset());
        this.playerStartPosition = getMapTile(10, 10).getLocation();
    }
    
}
