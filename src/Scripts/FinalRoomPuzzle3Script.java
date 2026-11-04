package Scripts;

import Level.Script;
import Level.ScriptState;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import Puzzles.FinalRoomPuzzle1;
import Puzzles.FinalRoomPuzzle3;
import Puzzles.StoryProgress;

import java.util.ArrayList;

public class FinalRoomPuzzle3Script extends Script {

    private boolean solvedBefore = false;

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {

        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        // Lock the player.
        scriptActions.add(new LockPlayerScriptAction());

        // Open Ghost Busters if it has not already been solved.
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {

                solvedBefore = FinalRoomPuzzle3.solved;

                if (!solvedBefore) {
                    FinalRoomPuzzle3.launch();
                }

                return ScriptState.COMPLETED;
            }
        });

        // Keep the player locked while the puzzle is open.
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {

                return FinalRoomPuzzle3.isOpen()
                    ? ScriptState.RUNNING
                    : ScriptState.COMPLETED;
            }
        });

        // Unlock the player.
        scriptActions.add(new UnlockPlayerScriptAction());

        // Record completion.
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {

                if (FinalRoomPuzzle3.solved && !solvedBefore) {

                     FinalRoomPuzzle3.solved = true;
                    StoryProgress.markBossPuzzleSolved(2);

                    FinalRoomPuzzle3Script.this.map.getFlagManager()
                        .setFlag("FinalRoomPuzzle3Solved");

                    FinalRoomCompletionScript.recordSolved(
                        FinalRoomPuzzle3Script.this.map,
                        "FinalRoomPuzzle3Solved",
                        FinalRoomPuzzle3Script.this.listeners
                    );
                }

                return ScriptState.COMPLETED;
            }
        });

        return scriptActions;
    }
}