package Maps;

import java.util.ArrayList;

import Level.Map;
import Level.NPC;
import NPCs.BathroomGhost;
import Tilesets.BathroomTileset2;
import Tilesets.CleanBathroomTileset2;
import Scripts.ChangeMapScript;
import Scripts.TestMap.BathroomGhostScriptRestored;
import Scripts.TestMap.BathroomGhostScript;
import Scripts.BathroomPuzzleScript;
import Puzzles.BathroomPuzzle;

public class BathroomMap2 extends Map {

    public BathroomMap2() {
        super("bathroom_map2.txt", BathroomPuzzle.solved ? new CleanBathroomTileset2() : new BathroomTileset2());
        if(BathroomPuzzle.solved) {
            this.playerStartPosition = getMapTile(5, 2).getLocation();
        } else {
            this.playerStartPosition = getMapTile(1, 2).getLocation();
        }
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        
        BathroomGhost bathroomGhost = new BathroomGhost(1, getMapTile(9, 3).getLocation().subtractX(30).subtractY(30));
        if(BathroomPuzzle.solved) {
            bathroomGhost.setInteractScript(new BathroomGhostScriptRestored());    
        } else {
            bathroomGhost.setInteractScript(new BathroomGhostScript());
        }
        npcs.add(bathroomGhost);
        return npcs;
    }

    @Override 
    public void loadScripts() {
        getMapTile(2, 1).setInteractScript(new ChangeMapScript(LobbyMap2::new));
        getMapTile(6, 2).setInteractScript(new BathroomPuzzleScript());
    }
}