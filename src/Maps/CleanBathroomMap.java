package Maps;

import Level.Map;
import Scripts.ChangeMapScript;
import Scripts.BathroomPuzzleScript;
import Tilesets.CleanBathroomTileset;

public class CleanBathroomMap extends Map {

    public CleanBathroomMap() {
        super("clean_bathroom_map.txt", new CleanBathroomTileset());
        this.playerStartPosition = getMapTile(5, 2).getLocation();
    }

    @Override
    public void loadScripts() {
        // Door back to lobby
        getMapTile(5, 1).setInteractScript(
            new ChangeMapScript(HotelLobbyMap::new)
        );

        // Bathtub
        getMapTile(1, 5).setInteractScript(
            new BathroomPuzzleScript()
        );

        getMapTile(2, 5).setInteractScript(
            new BathroomPuzzleScript()
        );
    }
}