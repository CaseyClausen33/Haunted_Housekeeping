package Scripts;

import Level.Script;
import Level.ScriptState;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import Puzzles.FinalRoomPuzzle1;
import Puzzles.StoryProgress;

import java.util.ArrayList;

public class FinalRoomPuzzle1Script extends Script {

    private boolean solvedBefore = false;

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {

        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        // Lock the player.
        scriptActions.add(new LockPlayerScriptAction());

        // Open the puzzle if it has not already been solved.
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {

                solvedBefore = FinalRoomPuzzle1.solved;

                if (!solvedBefore) {
                    FinalRoomPuzzle1.launch();
                }

                return ScriptState.COMPLETED;
            }
        });

        // Keep the player locked until the puzzle window closes.
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {

                return FinalRoomPuzzle1.isOpen()
                    ? ScriptState.RUNNING
                    : ScriptState.COMPLETED;
            }
        });

        // Unlock the player.
        scriptActions.add(new UnlockPlayerScriptAction());

        // Record completion if the puzzle was just solved.
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {

                if (FinalRoomPuzzle1.solved && !solvedBefore) {

                    StoryProgress.markBossPuzzleSolved(0);

                    FinalRoomPuzzle1Script.this.map.getFlagManager()
                        .setFlag("FinalRoomPuzzle1Solved");

                    FinalRoomCompletionScript.recordSolved(
                        FinalRoomPuzzle1Script.this.map,
                        "FinalRoomPuzzle1Solved",
                        FinalRoomPuzzle1Script.this.listeners
                    );
                }

                return ScriptState.COMPLETED;
            }
        });

        return scriptActions;
    }
}