package Maps;

import Level.Map;
import Level.MapTile;
import Level.NPC;
import NPCs.LobbyGhost;
import Puzzles.LobbyPuzzle;
import Scripts.ChangeMapScript;
import Scripts.LobbyPuzzleScript;
import Scripts.SimpleTextScript;
import Scripts.TestMap.LobbyGhostScript;
import Tilesets.LobbyTileset2;
import java.util.ArrayList;

public class LobbyMap2 extends Map {

    public LobbyMap2() {
        super("lobby_map2.txt", new LobbyTileset2());
        this.playerStartPosition = getMapTile(11, 11).getLocation();
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        
        LobbyGhost ghost = new LobbyGhost(1, getMapTile(5, 2).getLocation().subtractX(30).subtractY(30));
        ghost.setInteractScript(new LobbyGhostScript());
        npcs.add(ghost);
        return npcs;
    }
    

    @Override 
    public void loadScripts() {
        getMapTile(17, 1).setInteractScript(new ChangeMapScript(KitchenMap::new));
        getMapTile(22, 1).setInteractScript(new ChangeMapScript(BathroomMap2::new));
        getMapTile(2, 1).setInteractScript(new ChangeMapScript(LibraryMap2::new));
        getMapTile(4, 1).setInteractScript(new ChangeMapScript(BedroomMap2::new));
        getMapTile(11,1).setInteractScript(new ChangeMapScript(FinalRoomMap::new));

        getMapTile(3, 4).setInteractScript(new SimpleTextScript("The shelves are filled with papers detailing bills \n going all the way back to over a century ago, \n they seem to be collecting dust."));
        getMapTile(4, 4).setInteractScript(new SimpleTextScript("The documents and items on the desk are kept tidily, \n you notice a small gold emblem with your \n Grandfather's initials on it."));
        getMapTile(5, 4).setInteractScript(new SimpleTextScript("The documents and items on the desk are kept tidily, \n you notice a small gold emblem with your \n Grandfather's initials on it."));
        getMapTile(6, 4).setInteractScript(new SimpleTextScript("The candle's wax has long run out, \n and the small ashtray placed aside is \n strangely empty."));

    // Show the puzzle if it hasn't been solved yet, otherwise show the open archway tile
    // there will be a third case added soon if not all puzzles have been solved yet
    if (LobbyPuzzle.solved) {
        MapTile oldTile = getMapTile(11, 5);
        setMapTile(11, 5, getTileset().getTile(33).build(oldTile.getX(), oldTile.getY()));
    } else {
        getMapTile(11, 5).setInteractScript(new LobbyPuzzleScript());
    }
    }
    
}
