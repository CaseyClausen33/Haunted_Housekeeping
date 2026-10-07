package Maps;

import Level.Map;
import Scripts.ChangeMapScript;
import Scripts.KitchenPuzzleScript;
import Tilesets.BathroomTileset;
import Tilesets.BathroomTileset2;
import Scripts.BathroomPuzzleScript;

public class BathroomMap2 extends Map {

    public BathroomMap2() {
        super("bathroom_map2.txt", new BathroomTileset2());
        this.playerStartPosition = getMapTile(14, 2).getLocation();
    }

    @Override 
    public void loadScripts() {
        getMapTile(14, 1).setInteractScript(new ChangeMapScript(LobbyMap2::new));
        getMapTile(5, 1).setInteractScript(new BathroomPuzzleScript());
    }
}