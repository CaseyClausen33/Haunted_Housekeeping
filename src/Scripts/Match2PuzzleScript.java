package Scripts;

import Level.GameListener;
import Level.Map;
import Level.Script;
import Level.ScriptState;
import Maps.FinalRoomMap;
import Maps.LibraryMap2;
import Puzzles.Match2Puzzle;
import ScriptActions.*;
import java.util.ArrayList;

public class Match2PuzzleScript extends Script {

    // Was the puzzle already solved when the player started this interaction?
    private boolean solvedBefore = false;
    private boolean isFinalRoom = false;

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
                isFinalRoom = Match2PuzzleScript.this.map instanceof FinalRoomMap;
                if (isFinalRoom) {
                    Match2Puzzle.solved = false;
                }
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
                if (Match2Puzzle.solved) {
                    Match2PuzzleScript.this.map.getFlagManager().setFlag("LibraryPuzzleSolved");
                    FinalRoomCompletionScript.recordSolved(
                            Match2PuzzleScript.this.map,
                            "FinalLibraryPuzzleSolved",
                            Match2PuzzleScript.this.listeners);
                }

                if (Match2Puzzle.solved && !solvedBefore && !isFinalRoom) {

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