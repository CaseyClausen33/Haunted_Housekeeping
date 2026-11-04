package Scripts;

import java.util.function.Supplier;

import Level.Map;
import Level.Player;
import Level.Script;
import Puzzles.StoryProgress;
import Puzzles.StoryProgress.Room;

// Plays a room's "before" scene the first time the player walks in. When the puzzle has been solved and its window has
// closed, it reloads the room (so the clean tileset appears) and plays the "after" scene in the restored room.
//
// Call it from the room map's update():
//   RoomStory.update(this, player, StoryProgress.Room.BEDROOM, () -> new ChangeMapScript(BedroomMap2::new), clean);
// where clean is true when this map was built with the clean tileset. Pass null for the reload script if the room
// already reloads itself some other way (the kitchen does).
public class RoomStory {

    public static void update(Map map, Player player, Room room, Supplier<Script> reloadScript, boolean loadedClean) {
        if (map.getActiveScript() != null) {
            return;   // some other script (a puzzle script, a door...) is running; wait until it is finished
        }

        if (!StoryProgress.beforeSeen(room)) {
            StoryProgress.setBeforeSeen(room);
            start(map, player, RoomScenes.before(room));
            return;
        }

        if (StoryProgress.solved(room) && !StoryProgress.puzzleOpen(room)) {
            if (reloadScript != null && !loadedClean && !StoryProgress.reloadAttempted(room)) {
                // the puzzle was just solved in the spooky room: reload it so the clean tileset shows
                StoryProgress.markReloadStarted(room);
                player.unlock();
                start(map, player, reloadScript.get());
            } else if (!StoryProgress.afterSeen(room)) {
                StoryProgress.setAfterSeen(room);
                start(map, player, RoomScenes.after(room));
            }
        }
    }

    // Starts a script on a map from code (the same steps the engine does for a trigger)
    public static void start(Map map, Player player, Script script) {
        script.setMap(map);
        script.setPlayer(player);
        script.setListeners(map.getListeners());
        script.initialize();
        map.setActiveScript(script);
    }
}