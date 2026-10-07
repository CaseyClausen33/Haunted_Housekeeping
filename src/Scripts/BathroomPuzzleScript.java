package Scripts;

import java.util.ArrayList;

import Level.Script;
import Level.ScriptState;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import Puzzles.BathroomPuzzle;

public class BathroomPuzzleScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {

        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {

                javax.swing.SwingUtilities.invokeLater(() -> {

                    BathroomPuzzle puzzle = new BathroomPuzzle();

                    puzzle.setPuzzleCompleteListener(() -> {

                        for (Level.GameListener listener : listeners) {
                            listener.onMapChange(
                                new Maps.CleanBathroomMap()
                            );
                        }

                    });

                });

                return ScriptState.COMPLETED;
            }
        });

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}