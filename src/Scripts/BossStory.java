package Scripts;

import Level.Map;
import Level.Player;
import Puzzles.StoryProgress;

// Starts the boss room's scenes. Call BossStory.update(this, player) from the boss room map's update():
//   @Override
//   public void update(Player player) {
//       BossStory.update(this, player);
//       super.update(player);
//   }
// and set StoryProgress.bossSolved = true when the boss room's puzzle is beaten.
public class BossStory {

    public static void update(Map map, Player player) {
        if (map.getActiveScript() != null) {
            return;   // wait for the puzzle script / any other script to finish
        }

        if (!StoryProgress.bossIntroSeen) {
            StoryProgress.bossIntroSeen = true;
            RoomStory.start(map, player, BossScenes.before());
        } else if (StoryProgress.bossSolved && !StoryProgress.reunionSeen) {
            StoryProgress.reunionSeen = true;
            RoomStory.start(map, player, BossScenes.after());
        }
    }
}