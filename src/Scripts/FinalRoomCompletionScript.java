package Scripts;

import Level.GameListener;
import Level.Map;
import Maps.FinalRoomMap;
import Puzzles.StoryProgress;

import java.util.ArrayList;

final class FinalRoomCompletionScript {

    private FinalRoomCompletionScript() {}

    static void recordSolved(
            Map map,
            String puzzleFlag,
            ArrayList<GameListener> listeners) {

        if (!(map instanceof FinalRoomMap)) {
            return;
        }

        map.getFlagManager().setFlag(puzzleFlag);

        if (map.getFlagManager().isFlagSet("FinalRoomPuzzle1Solved")
                && map.getFlagManager().isFlagSet("FinalRoomPuzzle2Solved")
                && map.getFlagManager().isFlagSet("FinalRoomPuzzle3Solved")
                && map.getFlagManager().isFlagSet("FinalRoomPuzzle4Solved")
                && !map.getFlagManager().isFlagSet("FinalRoomWinTriggered")) {

            StoryProgress.bossSolved = true;

            map.getFlagManager().setFlag("FinalRoomWinTriggered");

            // DO NOT call onWin() here.
        }
    }
}