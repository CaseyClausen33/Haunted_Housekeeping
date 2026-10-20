package Scripts;

import Level.GameListener;
import Level.Map;
import Level.Script;
import Level.ScriptState;
import Maps.KitchenMap;
import Puzzles.KitchenPuzzle;
import ScriptActions.*;
import java.util.ArrayList;

public class KitchenPuzzleScript extends Script {

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
                isFinalRoom = KitchenPuzzleScript.this.map.getFlagManager()
                        .isFlagSet("FinalRoomReady");
                if (isFinalRoom) {
                    KitchenPuzzle.solved = false;
                }
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
                if (KitchenPuzzle.solved) {
                    KitchenPuzzleScript.this.map.getFlagManager().setFlag("KitchenPuzzleSolved");
                }

                if (KitchenPuzzle.solved && !solvedBefore && !isFinalRoom) {
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