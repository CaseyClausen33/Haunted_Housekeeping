package Maps;

import Level.Map;
import Level.NPC;
import NPCs.Ghost;
import Scripts.ChangeMapScript;
import Scripts.TestMap.GhostScript;
import Tilesets.HotelTileset;
import java.util.ArrayList;

public class HotelLobbyMap extends Map {

    public HotelLobbyMap() {
        super("hotel_lobby_map.txt", new HotelTileset());
        this.playerStartPosition = getMapTile(6, 15).getLocation();
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
    getMapTile(1, 0).setInteractScript(new ChangeMapScript(BedroomMap::new));
    getMapTile(1, 5).setInteractScript(new ChangeMapScript(NewMap::new));
    getMapTile(1, 15).setInteractScript(new ChangeMapScript(NewMap::new));
    getMapTile(11, 10).setInteractScript(new ChangeMapScript(NewMap::new));
    }
    
}
