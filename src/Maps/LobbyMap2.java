package Maps;

import Level.Map;
import Level.NPC;
import NPCs.Ghost;
import Scripts.ChangeMapScript;
import Scripts.TestMap.GhostScript;
import Tilesets.HotelTileset;
import Tilesets.LobbyTileset2;

import java.util.ArrayList;

public class LobbyMap2 extends Map {

    public LobbyMap2() {
        super("lobby_map2.txt", new LobbyTileset2());
        this.playerStartPosition = getMapTile(11, 12).getLocation();
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        
        Ghost ghost = new Ghost(1, getMapTile(5, 12).getLocation().subtractX(30).subtractY(30));
        ghost.setInteractScript(new GhostScript());
        npcs.add(ghost);

        return npcs;
    }
    

    @Override 
    public void loadScripts() {
    getMapTile(17, 1).setInteractScript(new ChangeMapScript(KitchenMap::new));
    getMapTile(22, 1).setInteractScript(new ChangeMapScript(BathroomMap::new));
    getMapTile(2, 1).setInteractScript(new ChangeMapScript(LibraryMap2::new));
    getMapTile(4, 1).setInteractScript(new ChangeMapScript(BedroomMap2::new));
    }
    
}
