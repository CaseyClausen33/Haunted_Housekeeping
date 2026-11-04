package Maps;

import java.util.ArrayList;

import Level.Map;
import Level.NPC;
import Level.Player;
import NPCs.KitchenGhost;
import Puzzles.KitchenPuzzle;
import Puzzles.StoryProgress;
import Scripts.KitchenExitScript;
import Scripts.KitchenPuzzleScript;
import Scripts.RoomStory;
import Scripts.TestMap.GhostScript;
import Tilesets.CleanKitchenTileset;
import Tilesets.KitchenTileset;

// Your KitchenMap with the story hook added (the update method)
public class KitchenMap extends Map {
    public KitchenMap() {
        // Same layout either way; the clean tileset is used once the puzzle is solved
        super("kitchen_map.txt", KitchenPuzzle.solved ? new CleanKitchenTileset() : new KitchenTileset());
        if (KitchenPuzzle.solved) {
            this.playerStartPosition = getMapTile(3, 3).getLocation();
        } else {
            this.playerStartPosition = getMapTile(17, 2).getLocation();
        }
    }

    // plays the kitchen's before/after story scenes
    @Override
    public void update(Player player) {
        // no reload script: the kitchen already swaps to its clean version on its own
        RoomStory.update(this, player, StoryProgress.Room.KITCHEN, null, true);
        super.update(player);
    }

    @Override
    public void loadScripts() {
        getMapTile(3, 2).setInteractScript(new KitchenPuzzleScript());
        getMapTile(17, 1).setInteractScript(new KitchenExitScript());
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