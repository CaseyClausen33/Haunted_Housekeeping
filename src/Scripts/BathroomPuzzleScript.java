package Scripts;

import java.util.ArrayList;

import Level.GameListener;
import Level.Map;
import Level.Script;
import Level.ScriptState;
import Maps.BathroomMap2;
import Puzzles.BathroomPuzzle;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.UnlockPlayerScriptAction;

public class BathroomPuzzleScript extends Script {

    // Was the puzzle already solved when the player started this interaction?
    private boolean solvedBefore = false;

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {

        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        // Open the puzzle window (skipped if it's already been solved)
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
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
                if (BathroomPuzzle.solved && !solvedBefore) {
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