package Maps;

import Level.Map;
import Scripts.BathroomPuzzleScript;
import Scripts.ChangeMapScript;
import Tilesets.BathroomTileset;

public class BathroomMap extends Map {

    public BathroomMap() {
        super("bathroom_map.txt", new BathroomTileset());
        this.playerStartPosition = getMapTile(5, 2).getLocation();
    }

    @Override
    public void loadScripts() {
        getMapTile(5, 1).setInteractScript(new ChangeMapScript(HotelLobbyMap::new));
        getMapTile(1, 5).setInteractScript(new BathroomPuzzleScript());
         getMapTile(2, 5).setInteractScript(new BathroomPuzzleScript());
    }
}