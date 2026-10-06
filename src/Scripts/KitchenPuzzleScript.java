package Scripts;

import java.util.ArrayList;

import Level.GameListener;
import Level.Map;
import Level.Script;
import Level.ScriptState;
import Maps.KitchenMap;
import Puzzles.KitchenPuzzle;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.UnlockPlayerScriptAction;

public class KitchenPuzzleScript extends Script {

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
                solvedBefore = KitchenPuzzle.solved;
                if (!solvedBefore) {
                    KitchenPuzzle.launch();
                }
                return ScriptState.COMPLETED;
            }
        });

        // Keep the player locked until the puzzle window is closed
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                return KitchenPuzzle.isOpen() ? ScriptState.RUNNING : ScriptState.COMPLETED;
            }
        });

        scriptActions.add(new UnlockPlayerScriptAction());

        // If the puzzle was just solved, reload the kitchen (KitchenMap now uses the clean tileset)
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                if (KitchenPuzzle.solved && !solvedBefore) {
                    Map nextMap = new KitchenMap();
                    for (GameListener listener : KitchenPuzzleScript.this.listeners) {
                        listener.onMapChange(nextMap);
                    }
                }
                return ScriptState.COMPLETED;
            }
        });

        return scriptActions;
    }
}