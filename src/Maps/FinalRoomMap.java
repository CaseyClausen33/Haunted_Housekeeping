package Maps;

import Level.Map;
import Level.MapTile;
import Level.NPC;
import NPCs.Ghost;
import NPCs.StoryNPCs;
import Puzzles.*;
import Scripts.*;
import Tilesets.LobbyTileset2;
import java.util.ArrayList;
import Level.Player;

public class FinalRoomMap extends Map {

    public FinalRoomMap() {
        super("finalroom_map.txt", new LobbyTileset2());
        this.playerStartPosition = getMapTile(7,13).getLocation();

    }

    public ArrayList<NPC> loadNPCs() {
        StoryNPCs.addStoryGhosts(npcs, getMapTile(7, 8).getLocation());

        return npcs;

    }

    public void update(Player player) {
        BossStory.update(this, player);
    }

    @Override 
    public void loadScripts() {
        getMapTile(2, 2).setInteractScript(new FinalRoomPuzzle1Script());
        getMapTile(2, 13).setInteractScript(new FinalRoomPuzzle2Script());
        getMapTile(13, 2).setInteractScript(new FinalRoomPuzzle3Script());
        getMapTile(13, 13).setInteractScript(new FinalRoomPuzzle4Script());
    }
    
}
