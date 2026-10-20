package Maps;

import Level.Map;
import Level.MapTile;
import Level.NPC;
import NPCs.Ghost;
import Puzzles.*;
import Scripts.*;
import Tilesets.LobbyTileset2;
import java.util.ArrayList;

public class FinalRoomMap extends Map {

    public FinalRoomMap() {
        super("finalroom_map.txt", new LobbyTileset2());
        this.playerStartPosition = getMapTile(7,13).getLocation();
    }

    @Override 
    public void loadScripts() {
        getMapTile(2, 2).setInteractScript(new BathroomPuzzleScript());
        getMapTile(2, 13).setInteractScript(new Match2PuzzleScript());
        getMapTile(13, 2).setInteractScript(new KitchenPuzzleScript());
        getMapTile(13, 13).setInteractScript(new BedroomPuzzleScript());
    }
    
}
