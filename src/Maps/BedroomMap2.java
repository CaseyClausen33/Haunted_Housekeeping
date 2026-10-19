package Maps;

import Level.Map;
import Tilesets.BedroomTileset;
import Tilesets.CleanBedroomTileset;
import Scripts.ChangeMapScript;
import Scripts.BedroomPuzzleScript;
import Puzzles.BedroomPuzzle;
public class BedroomMap2 extends Map {

    public BedroomMap2() {
        super("bedroom_map2.txt", BedroomPuzzle.solved ? new CleanBedroomTileset() : new BedroomTileset());
        if(BedroomPuzzle.solved) {
            this.playerStartPosition = getMapTile(8, 3).getLocation();
        } else {
            this.playerStartPosition = getMapTile(1, 2).getLocation();
        }
    }

    @Override 
    public void loadScripts() {
        getMapTile(2, 1).setInteractScript(new ChangeMapScript(LobbyMap2::new));
        getMapTile(8, 2).setInteractScript(new BedroomPuzzleScript());
    }
}