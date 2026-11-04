package Scripts;

import Level.Script;
import Level.ScriptState;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import Puzzles.FinalRoomPuzzle1;
import Puzzles.FinalRoomPuzzle2;
import Puzzles.StoryProgress;

import java.util.ArrayList;

public class FinalRoomPuzzle2Script extends Script {

    private boolean solvedBefore = false;

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {

        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        // Open the word search if it has not already been solved.
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {

                solvedBefore = FinalRoomPuzzle2.solved;

                if (!solvedBefore) {
                    FinalRoomPuzzle2.launch();
                }

                return ScriptState.COMPLETED;
            }
        });

        // Keep the player locked while the puzzle is open.
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {

                return FinalRoomPuzzle2.isOpen()
                    ? ScriptState.RUNNING
                    : ScriptState.COMPLETED;
            }
        });

        scriptActions.add(new UnlockPlayerScriptAction());

        // Record completion.
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {

                if (FinalRoomPuzzle2.solved && !solvedBefore) {

                    StoryProgress.markBossPuzzleSolved(1);


                    FinalRoomPuzzle2Script.this.map.getFlagManager()
                        .setFlag("FinalRoomPuzzle2Solved");

                    FinalRoomCompletionScript.recordSolved(
                        FinalRoomPuzzle2Script.this.map,
                        "FinalRoomPuzzle2Solved",
                        FinalRoomPuzzle2Script.this.listeners
                    );
                }

                return ScriptState.COMPLETED;
            }
        });

        return scriptActions;
    }
}