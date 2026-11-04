package Scripts;

import java.util.ArrayList;
import java.util.function.Supplier;

import Level.Script;
import ScriptActions.ScriptAction;

// A script whose actions are built by a function, so scenes can be written without a new class each time.
// The function runs when the scene starts, so it can use the story's current state (e.g. how many rooms are solved).
public class SceneScript extends Script {

    private final Supplier<ArrayList<ScriptAction>> builder;

    public SceneScript(Supplier<ArrayList<ScriptAction>> builder) {
        this.builder = builder;
    }

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        return builder.get();
    }
}