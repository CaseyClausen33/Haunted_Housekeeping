package Maps;

import Level.Map;
import Level.Player;
import Puzzles.BedroomPuzzle;
import Puzzles.StoryProgress;
import Scripts.BedroomPuzzleScript;
import Scripts.ChangeMapScript;
import Scripts.RoomStory;
import Tilesets.BedroomTileset;
import Tilesets.CleanBedroomTileset;

public class BedroomMap2 extends Map {

    // true when this map was built with the clean tileset (the puzzle was already solved)
    private final boolean clean = BedroomPuzzle.solved;

    public BedroomMap2() {
        super("bedroom_map2.txt", BedroomPuzzle.solved ? new CleanBedroomTileset() : new BedroomTileset());
        // after the puzzle is solved the room reloads clean and the player appears beside the bed; otherwise at the door
        if (StoryProgress.consumeReload(StoryProgress.Room.BEDROOM)) {
            this.playerStartPosition = getMapTile(5, 5).getLocation();
        } else {
            this.playerStartPosition = getMapTile(10, 2).getLocation();
        }
    }

    // plays the bedroom's before/after story scenes and swaps in the clean room once the puzzle is solved
    @Override
    public void update(Player player) {
        RoomStory.update(this, player, StoryProgress.Room.BEDROOM, () -> new ChangeMapScript(BedroomMap2::new), clean);
        super.update(player);
    }

    @Override
    public void loadScripts() {
        getMapTile(10, 1).setInteractScript(new ChangeMapScript(LobbyMap2::new));
        getMapTile(5, 3).setInteractScript(new BedroomPuzzleScript());
        getMapTile(6, 3).setInteractScript(new BedroomPuzzleScript());
    }
}