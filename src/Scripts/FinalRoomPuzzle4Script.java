package Scripts;

import Level.Script;
import Level.ScriptState;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import Puzzles.FinalRoomPuzzle1;
import Puzzles.FinalRoomPuzzle4;
import Puzzles.StoryProgress;

import java.util.ArrayList;

public class FinalRoomPuzzle4Script extends Script {

    private boolean solvedBefore = false;

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {

        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        // Lock the player.
        scriptActions.add(new LockPlayerScriptAction());

        // Open the stained-glass puzzle if it is not solved.
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {

                solvedBefore = FinalRoomPuzzle4.solved;

                if (!solvedBefore) {
                    FinalRoomPuzzle4.launch();
                }

                return ScriptState.COMPLETED;
            }
        });

        // Keep the player locked while the puzzle is open.
        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {

                return FinalRoomPuzzle4.isOpen()
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

                if (FinalRoomPuzzle4.solved && !solvedBefore) {

                     FinalRoomPuzzle4.solved = true;
                    StoryProgress.markBossPuzzleSolved(3);

                    FinalRoomPuzzle4Script.this.map.getFlagManager()
                        .setFlag("FinalRoomPuzzle4Solved");

                    FinalRoomCompletionScript.recordSolved(
                        FinalRoomPuzzle4Script.this.map,
                        "FinalRoomPuzzle4Solved",
                        FinalRoomPuzzle4Script.this.listeners
                    );
                }

                return ScriptState.COMPLETED;
            }
        });

        return scriptActions;
    }
}