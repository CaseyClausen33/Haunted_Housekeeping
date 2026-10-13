package Scripts;

import java.util.ArrayList;

import Level.GameListener;
import Level.Map;
import Level.Script;
import Level.ScriptState;
import Puzzles.Match2Puzzle;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import Maps.LibraryMap2;

public class Match2PuzzleScript extends Script {

    // Was the puzzle already solved when the player started this interaction?
    private boolean solvedBefore = false;

    // True once the player has beaten the puzzle.
    public static boolean isSolved() {
        return Match2Puzzle.solved;
    }

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {

        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        // Open the puzzle window (skipped if it has already been solved)
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                solvedBefore = Match2Puzzle.solved;

                if (!solvedBefore) {
                    Match2Puzzle.launch();
                }

                return ScriptState.COMPLETED;
            }
        });

        // Keep the player locked until the puzzle window is closed
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                return Match2Puzzle.isOpen()
                    ? ScriptState.RUNNING
                    : ScriptState.COMPLETED;
            }
        });

        scriptActions.add(new UnlockPlayerScriptAction());

        // If the puzzle was just solved, reload the appropriate map
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                if (Match2Puzzle.solved && !solvedBefore) {

                    Map nextMap = new LibraryMap2();

                    for (GameListener listener : Match2PuzzleScript.this.listeners) {
                        listener.onMapChange(nextMap);
                    }
                }

                return ScriptState.COMPLETED;
            }
        });

        return scriptActions;
    }
}