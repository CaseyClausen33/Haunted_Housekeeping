package Maps;

import Tilesets.CleanKitchenTileset;
import Tilesets.CleanKitchenTileset;
import Tilesets.KitchenTileset;

import java.util.ArrayList;

import Level.Map;
import Level.NPC;
import NPCs.KitchenGhost;
import Puzzles.KitchenPuzzle;
import Scripts.KitchenExitScript;
import Scripts.KitchenPuzzleScript;
import Scripts.TestMap.GhostScript;

public class KitchenMap extends Map {
    public KitchenMap() {
        // Same layout either way; the clean tileset is used once the puzzle is solved
        super("kitchen_map.txt", KitchenPuzzle.solved ? new CleanKitchenTileset() : new KitchenTileset());
        if(KitchenPuzzle.solved) {
            this.playerStartPosition = getMapTile(1, 9).getLocation();
        } else {
            this.playerStartPosition = getMapTile(17, 2).getLocation();
        }
        
    }

    @Override
    public void loadScripts() {
        getMapTile(2, 9).setInteractScript(new KitchenPuzzleScript());
        getMapTile(17, 2).setInteractScript(new KitchenExitScript());
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        KitchenGhost ghost = new KitchenGhost(3, getMapTile(10, 6).getLocation().subtractX(20));
        ghost.setInteractScript(new GhostScript());
        npcs.add(ghost);

        return npcs;
    }
}