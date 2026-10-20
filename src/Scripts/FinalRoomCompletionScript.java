package Scripts;

import Level.GameListener;
import Level.Map;
import Maps.FinalRoomMap;
import java.util.ArrayList;

final class FinalRoomCompletionScript {

    private FinalRoomCompletionScript() {
    }

    static void recordSolved(Map map, String puzzleFlag, ArrayList<GameListener> listeners) {
        if (!(map instanceof FinalRoomMap)) {
            return;
        }

        map.getFlagManager().setFlag(puzzleFlag);
        if (map.getFlagManager().isFlagSet("FinalBathroomPuzzleSolved")
                && map.getFlagManager().isFlagSet("FinalBedroomPuzzleSolved")
                && map.getFlagManager().isFlagSet("FinalKitchenPuzzleSolved")
                && map.getFlagManager().isFlagSet("FinalLibraryPuzzleSolved")
                && !map.getFlagManager().isFlagSet("FinalRoomWinTriggered")) {
            map.getFlagManager().setFlag("FinalRoomWinTriggered");
            for (GameListener listener : listeners) {
                listener.onWin();
            }
        }
    }
}
