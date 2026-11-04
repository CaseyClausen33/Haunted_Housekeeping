package Scripts;

import java.util.ArrayList;
import Level.Script;
import Level.ScriptState;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import Puzzles.BedroomPuzzle;

// Opens the bedroom's sequence puzzle and keeps the player locked until the puzzle window is closed
public class BedroomPuzzleScript extends Script {

    // True once the player has beaten the puzzle (the flag itself lives on BedroomPuzzle)
    public static boolean isSolved() {
        return BedroomPuzzle.solved;
    }

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {

        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        // Open the puzzle window (does nothing if it is already open)
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                BedroomPuzzle.launch();
                return ScriptState.COMPLETED;
            }
        });

        // Keep the player locked until the puzzle window is closed
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                return BedroomPuzzle.isOpen() ? ScriptState.RUNNING : ScriptState.COMPLETED;
            }
        });

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}