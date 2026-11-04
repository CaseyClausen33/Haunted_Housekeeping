package Maps;

import Level.Map;
import Level.Player;
import Puzzles.BathroomPuzzle;
import Puzzles.StoryProgress;
import Scripts.BathroomPuzzleScript;
import Scripts.ChangeMapScript;
import Scripts.RoomStory;
import Tilesets.BathroomTileset2;
import Tilesets.CleanBathroomTileset2;

public class BathroomMap2 extends Map {

    // true when this map was built with the clean tileset (the puzzle was already solved)
    private final boolean clean = BathroomPuzzle.solved;

    public BathroomMap2() {
        super("bathroom_map2.txt", BathroomPuzzle.solved ? new CleanBathroomTileset2() : new BathroomTileset2());
        // after the puzzle is solved the room reloads clean and the player appears in front of the puzzle; otherwise at the door
        if (StoryProgress.consumeReload(StoryProgress.Room.BATHROOM)) {
            this.playerStartPosition = getMapTile(5, 2).getLocation();
        } else {
            this.playerStartPosition = getMapTile(14, 2).getLocation();
        }
    }

    // plays the bathroom's before/after story scenes and swaps in the clean room once the puzzle is solved
    @Override
    public void update(Player player) {
        RoomStory.update(this, player, StoryProgress.Room.BATHROOM, () -> new ChangeMapScript(BathroomMap2::new), clean);
        super.update(player);
    }

    @Override
    public void loadScripts() {
        getMapTile(14, 1).setInteractScript(new ChangeMapScript(LobbyMap2::new));
        getMapTile(6, 2).setInteractScript(new BathroomPuzzleScript());
    }
}