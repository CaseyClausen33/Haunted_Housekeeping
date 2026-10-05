package Scripts;

import java.util.ArrayList;
import Level.Script;
import Level.ScriptState;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import Puzzles.KitchenPuzzle;

public class KitchenPuzzleScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {

        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new ScriptAction() {
            @Override
            public ScriptState execute() {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    new KitchenPuzzle();
                });
                return ScriptState.COMPLETED;
            }
        });
        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}