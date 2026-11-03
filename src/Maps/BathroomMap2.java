package Maps;

import Level.Map;
import Level.NPC;
import NPCs.BathroomGhost;
import Puzzles.BathroomPuzzle;
import Scripts.BathroomPuzzleScript;
import Scripts.ChangeMapScript;
import Scripts.SimpleTextScript;
import Scripts.TestMap.BathroomGhostScript;
import Scripts.TestMap.BathroomGhostScriptRestored;
import Tilesets.BathroomTileset2;
import Tilesets.CleanBathroomTileset2;
import java.util.ArrayList;

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
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        
        BathroomGhost bathroomGhost = new BathroomGhost(1, getMapTile(6, 4).getLocation().subtractX(30).subtractY(30));
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
        getMapTile(14, 1).setInteractScript(new ChangeMapScript(LobbyMap2::new));
        getMapTile(6, 2).setInteractScript(new BathroomPuzzleScript());
        // change text based on how clean the room is
        if (!BathroomPuzzle.solved) {
        getMapTile(4, 2).setInteractScript(new SimpleTextScript("The water runs an odd brown, the sink looks \n as if it has been rusting for years."));
        getMapTile(12, 4).setInteractScript(new SimpleTextScript("There's a wet floor sign here, but there \n doesn't seem to be any water here anymore..."));
        getMapTile(3, 10).setInteractScript(new SimpleTextScript("You open the lid and immediately close it to \n ward off the smell. You don't want to know how long \n it's been sitting there."));
        }
        else {
            getMapTile(4, 2).setInteractScript(new SimpleTextScript("The water is clear, but you'd rather not \n think about how long it's been sitting here."));
            getMapTile(12, 4).setInteractScript(new SimpleTextScript("The wet floor sign is still here, but the \n floor is dry and clean now."));
            getMapTile(6, 2).setInteractScript(new SimpleTextScript("The toilet is clean and sparkling,  best \n not to think about how long it took to get it this way."));
        }
    }
}