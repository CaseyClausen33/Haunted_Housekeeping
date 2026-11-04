package Maps;

import Tilesets.CleanKitchenTileset;
import Tilesets.KitchenTileset;

import java.util.ArrayList;

import Level.Map;
import Level.NPC;
import Level.Player;
import NPCs.KitchenGhost;
import Puzzles.KitchenPuzzle;
import Puzzles.StoryProgress;
import Scripts.KitchenExitScript;
import Scripts.KitchenPuzzleScript;
import Scripts.SimpleTextScript;
import Scripts.TestMap.KitchenGhostScript;
import Scripts.TestMap.KitchenGhostScriptRestored;
import Scripts.RoomStory;

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

        getMapTile(12, 10).setInteractScript(new SimpleTextScript("You open to fridge to find that it is still \nstocked with foods of all kinds."));
        getMapTile(1, 5).setInteractScript(new SimpleTextScript("The drawer is stuffed with cookbooks from all \naround the world."));
        if (!KitchenPuzzle.solved) {
         getMapTile(16, 4).setInteractScript(new SimpleTextScript("The cauldron seems to be the only functioning thing \nin this kitchen, brewing a soup in a greenish color \nyou don't find too appetizing."));
        } else {
            getMapTile(16, 4).setInteractScript(new SimpleTextScript("The new soup smells amazing, and would probably \n taste even better if you had time to eat it."));
        }
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        KitchenGhost ghost = new KitchenGhost(3, getMapTile(6, 3).getLocation().subtractX(20));
        if(KitchenPuzzle.solved) {
            ghost.setInteractScript(new KitchenGhostScriptRestored());
        } else {
            ghost.setInteractScript(new KitchenGhostScript());
        }
        npcs.add(ghost);

        return npcs;
    }
}