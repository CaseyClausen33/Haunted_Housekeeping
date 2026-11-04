package Scripts;

import java.util.function.BooleanSupplier;

import Level.Map;
import Level.Player;
import Puzzles.StoryProgress;

// Starts the boss room's scenes: Evelyn's confrontation when the player first walks in, and the reunion once ALL
// FOUR boss room puzzles are solved.
//
// Option A (no changes to the puzzles): tell it how to check each puzzle, from the boss room map's update():
//   @Override
//   public void update(Player player) {
//       BossStory.update(this, player,
//               () -> FirstPuzzle.solved, () -> SecondPuzzle.solved, () -> ThirdPuzzle.solved, () -> FourthPuzzle.solved);
//       super.update(player);
//   }
//
// Option B: call BossStory.update(this, player) with no puzzles, and call StoryProgress.markBossPuzzleSolved(n)
// (n = 0, 1, 2 or 3) from each puzzle when it is beaten.
public class BossStory {

    public static void update(Map map, Player player, BooleanSupplier... puzzlesSolved) {
        if (map.getActiveScript() != null) {
            return;   // wait for the puzzle scripts / any other script to finish
        }

        if (!StoryProgress.bossIntroSeen) {
            StoryProgress.bossIntroSeen = true;
            RoomStory.start(map, player, BossScenes.before());
        } else if (!StoryProgress.reunionSeen && allSolved(puzzlesSolved)) {
            StoryProgress.reunionSeen = true;
            RoomStory.start(map, player, BossScenes.after());
        }
    }

    private static boolean allSolved(BooleanSupplier[] puzzlesSolved) {
        if (StoryProgress.bossSolved) {
            return true;
        }
        if (puzzlesSolved != null && puzzlesSolved.length > 0) {
            for (BooleanSupplier solved : puzzlesSolved) {
                if (!solved.getAsBoolean()) {
                    return false;
                }
            }
            return true;
        }
        return StoryProgress.allBossPuzzlesMarked();   // option B
    }
}