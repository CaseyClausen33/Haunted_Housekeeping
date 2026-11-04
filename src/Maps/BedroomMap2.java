package Maps;

import java.util.ArrayList;

import Level.Map;
import Level.NPC;
import Level.Player;
import NPCs.BedroomGhost;
import Puzzles.BedroomPuzzle;
import Puzzles.StoryProgress;
import Scripts.BedroomPuzzleScript;
import Scripts.ChangeMapScript;
import Scripts.RoomStory;
import Scripts.SimpleTextScript;
import Scripts.TestMap.BedroomGhostScript;
import Scripts.TestMap.BedroomGhostScriptRestored;
import Tilesets.BedroomTileset;
import Tilesets.CleanBedroomTileset;

public class BedroomMap2 extends Map {

    // true when this map was built with the clean tileset (the puzzle was already solved)
    private final boolean clean = BedroomPuzzle.solved;

    public BedroomMap2() {
        super("bedroom_map2.txt", BedroomPuzzle.solved ? new CleanBedroomTileset() : new BedroomTileset());
        // after the puzzle is solved the room reloads clean and the player appears beside the bed; otherwise at the door
        if (StoryProgress.consumeReload(StoryProgress.Room.BEDROOM)) {
            this.playerStartPosition = getMapTile(5, 4).getLocation();
        } else {
            this.playerStartPosition = getMapTile(1, 2).getLocation();
        }
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        
        BedroomGhost bedroomGhost = new BedroomGhost(1, getMapTile(5, 6).getLocation().subtractX(30).subtractY(30));
        if(BedroomPuzzle.solved) {
            bedroomGhost.setInteractScript(new BedroomGhostScriptRestored());    
        } else {
            bedroomGhost.setInteractScript(new BedroomGhostScript());
        }
        npcs.add(bedroomGhost);
        return npcs;
    }

    // plays the bedroom's before/after story scenes and swaps in the clean room once the puzzle is solved
    @Override
    public void update(Player player) {
        RoomStory.update(this, player, StoryProgress.Room.BEDROOM, () -> new ChangeMapScript(BedroomMap2::new), clean);
        super.update(player);
    }

    @Override
    public void loadScripts() {
        getMapTile(1, 1).setInteractScript(new ChangeMapScript(LobbyMap2::new));
        getMapTile(5, 4).setInteractScript(new BedroomPuzzleScript());

        getMapTile(1, 5).setInteractScript(new SimpleTextScript("A rideable plastic horse toy for a young child, \n some of the paint is scratched off."));
        getMapTile(6, 4).setInteractScript(new SimpleTextScript("A pair of comfy-looking fur slippers, \n they must belong to the ghost here, \n but you're not sure how he puts them on."));
         // change the text based on how clean the room is
        if (!BedroomPuzzle.solved) {
            getMapTile(5, 3).setInteractScript(new SimpleTextScript("The sheets are a mess, it looks like that ghost \n has been sleeping here for a long time."));
            getMapTile(7, 3).setInteractScript(new SimpleTextScript("The sheets are a mess, it looks like that ghost \n has been sleeping here for a long time."));
        }
        else {
            getMapTile(5, 3).setInteractScript(new SimpleTextScript("The sheets are neatly made, thanks to you."));
            getMapTile(7, 3).setInteractScript(new SimpleTextScript("The sheets are neatly made, thanks to you."));
        }
    }
}