package Maps;

import Level.Map;
import Scripts.ChangeMapScript;
import Tilesets.CleanBathroomTileset;

public class CleanBathroomMap extends Map {

    public CleanBathroomMap() {
        super("clean_bathroom_map.txt", new CleanBathroomTileset());
        this.playerStartPosition = getMapTile(3, 5).getLocation();
    }

    @Override
    public void loadScripts() {

        // Door back to lobby
        getMapTile(5, 1).setInteractScript(
            new ChangeMapScript(HotelLobbyMap::new)
        );
    }
}