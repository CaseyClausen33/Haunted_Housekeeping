package Scripts;

import Level.Map;
import Level.Player;
import Maps.LobbyMap2;
import Puzzles.StoryProgress;

// Starts the lobby's story scenes. Call LobbyStory.update(this, player) from LobbyMap2.update(),
// after your intro cutscene code:
//   - after the four rooms are solved: the scene that points the player at the stairs
//   - at the very end: the final shot of the busy hotel, then the title screen
public class LobbyStory {

    public static void update(Map map, Player player) {
        if (map.getActiveScript() != null) {
            return;
        }

        if (StoryProgress.epilogueActive && !StoryProgress.epilogueSeen) {
            // StoryProgress.epilogueSeen = true;
            // RoomStory.start(map, player, new EpilogueScript());
        } else if (LobbyMap2.lobbyIntroSeen && !StoryProgress.allRoomsSceneSeen && StoryProgress.allSolved()) {
            StoryProgress.allRoomsSceneSeen = true;
            RoomStory.start(map, player, new LobbyAllRoomsScript());
        }
    }
}