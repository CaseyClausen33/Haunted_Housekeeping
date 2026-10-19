package Maps;

import Level.Map;
import Scripts.ChangeMapScript;
import Tilesets.BathroomTileset2;
import Scripts.BathroomPuzzleScript;
import Tilesets.CleanBathroomTileset2;
import Puzzles.BathroomPuzzle;

public class BathroomMap2 extends Map {

    public BathroomMap2() {

        super("bathroom_map2.txt", BathroomPuzzle.solved ? new CleanBathroomTileset2() : new BathroomTileset2());
        if(BathroomPuzzle.solved) {
            this.playerStartPosition = getMapTile(5, 2).getLocation();
        } else {
            this.playerStartPosition = getMapTile(14, 2).getLocation();
        }
    }

    @Override 
    public void loadScripts() {
        getMapTile(14, 1).setInteractScript(new ChangeMapScript(LobbyMap2::new));
        getMapTile(5, 1).setInteractScript(new BathroomPuzzleScript());
    }
}