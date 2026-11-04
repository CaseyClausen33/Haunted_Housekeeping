package Scripts;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import ScriptActions.UnlockPlayerScriptAction;

// A simple "talk to this NPC" script: the NPC says a few lines (with a speaker label) and the player gets control back.
public class NPCChatScript extends Script {

    private final String label;
    private final String[] lines;

    public NPCChatScript(String label, String... lines) {
        this.label = label;
        this.lines = lines;
    }

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> actions = new ArrayList<>();
        actions.add(new LockPlayerScriptAction());
        TextboxScriptAction text = new TextboxScriptAction();
        for (String line : lines) {
            text.addText(label + line);
        }
        actions.add(text);
        actions.add(new UnlockPlayerScriptAction());
        return actions;
    }
}