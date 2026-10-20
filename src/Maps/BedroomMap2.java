package Maps;

import java.util.ArrayList;

import Level.Map;
import Level.NPC;
import NPCs.BedroomGhost;
import Tilesets.BedroomTileset;
import Tilesets.CleanBedroomTileset;
import Scripts.ChangeMapScript;
import Scripts.TestMap.BedroomGhostScriptRestored;
import Scripts.TestMap.BedroomGhostScript;
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
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        
        BedroomGhost bedroomGhost = new BedroomGhost(1, getMapTile(9, 3).getLocation().subtractX(30).subtractY(30));
        if(BedroomPuzzle.solved) {
            bedroomGhost.setInteractScript(new BedroomGhostScriptRestored());    
        } else {
            bedroomGhost.setInteractScript(new BedroomGhostScript());
        }
        npcs.add(bedroomGhost);
        return npcs;
    }

    @Override 
    public void loadScripts() {
        getMapTile(2, 1).setInteractScript(new ChangeMapScript(LobbyMap2::new));
        getMapTile(6, 2).setInteractScript(new BedroomPuzzleScript());
    }
}