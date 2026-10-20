package Scripts;

import Level.GameListener;
import Level.Map;
import Level.Script;
import Level.ScriptState;
import Maps.BathroomMap2;
import Maps.FinalRoomMap;
import Puzzles.BathroomPuzzle;
import ScriptActions.*;
import java.util.ArrayList;

public class BathroomPuzzleScript extends Script {

    // Was the puzzle already solved when the player started this interaction?
    private boolean solvedBefore = false;
    private boolean isFinalRoom = false;

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {

        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        // Open the puzzle window (skipped if it's already been solved)
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                isFinalRoom = BathroomPuzzleScript.this.map instanceof FinalRoomMap;
                if (isFinalRoom) {
                    BathroomPuzzle.solved = false;
                }
                solvedBefore = BathroomPuzzle.solved;
                if (!solvedBefore) {
                    BathroomPuzzle.launch();
                }
                return ScriptState.COMPLETED;
            }
        });

        // Keep the player locked until the puzzle window is closed
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                return BathroomPuzzle.isOpen() ? ScriptState.RUNNING : ScriptState.COMPLETED;
            }
        });

        scriptActions.add(new UnlockPlayerScriptAction());

        // If the puzzle was just solved, reload the kitchen (KitchenMap now uses the clean tileset)
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                if (BathroomPuzzle.solved) {
                    BathroomPuzzleScript.this.map.getFlagManager().setFlag("BathroomPuzzleSolved");
                    FinalRoomCompletionScript.recordSolved(
                            BathroomPuzzleScript.this.map,
                            "FinalBathroomPuzzleSolved",
                            BathroomPuzzleScript.this.listeners);
                }

                if (BathroomPuzzle.solved && !solvedBefore && !isFinalRoom) {
                    Map nextMap = new BathroomMap2();
                    for (GameListener listener : BathroomPuzzleScript.this.listeners) {
                        listener.onMapChange(nextMap);
                    }
                }
                return ScriptState.COMPLETED;
            }
        });

        return scriptActions;
    }
}