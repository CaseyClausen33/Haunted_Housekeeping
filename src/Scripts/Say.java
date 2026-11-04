package Scripts;

import ScriptActions.TextboxScriptAction;

// Short helpers for writing dialogue. Each returns a textbox that shows its lines one after another
// (the player presses space to move to the next line).
public class Say {

    // Labels shown in front of a speaker's lines. Set one to "" to turn its label off.
    public static final String GHOST = "Ghost: ";        // the lobby ghost
    public static final String EVELYN = "Evelyn: ";      // the ghost who has been haunting the hotel
    public static final String ARTHUR = "Arthur: ";      // the player's grandfather

    public static TextboxScriptAction player(String... lines) {
        return new TextboxScriptAction(lines);
    }

    // narration / sound effects, shown in parentheses
    public static TextboxScriptAction note(String line) {
        return new TextboxScriptAction("(" + line + ")");
    }

    public static TextboxScriptAction ghost(String... lines) { return labeled(GHOST, lines); }
    public static TextboxScriptAction evelyn(String... lines) { return labeled(EVELYN, lines); }
    public static TextboxScriptAction arthur(String... lines) { return labeled(ARTHUR, lines); }

    public static TextboxScriptAction labeled(String label, String... lines) {
        TextboxScriptAction action = new TextboxScriptAction();
        for (String line : lines) {
            action.addText(label + line);
        }
        return action;
    }
}