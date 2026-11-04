package Scripts;

import java.util.ArrayList;

import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.WaitScriptAction;

// The final shot. The camera pans slowly across the lobby of the reopened hotel, packed with guests, with
// Evelyn at the front desk and Arthur greeting people at the door. Then the game returns to the title screen.
// Plays when LobbyMap2 is built with StoryProgress.epilogueActive = true (see LobbyStory).
public class EpilogueScript extends SceneScript {

    private static final int LOBBY_GHOST = 1;

    public EpilogueScript() {
        super(EpilogueScript::buildActions);
    }

    private static ArrayList<ScriptAction> buildActions() {
        ArrayList<ScriptAction> a = new ArrayList<>();

        a.add(new LockPlayerScriptAction());
        a.add(new CutsceneActions.NPCGlide(LOBBY_GHOST, 4, 3, 6));   // the lobby ghost takes her place at the desk
        a.add(new WaitScriptAction(30));
        a.add(Say.note("Some time later..."));

        // pan across the lobby from the front desk to the front doors
        a.add(new CutsceneActions.CameraPan(5, 5, 90));
        a.add(Say.evelyn("Welcome! Checking in? Right this way."));
        a.add(new WaitScriptAction(30));
        a.add(new CutsceneActions.CameraPan(11, 6, 220));
        a.add(new WaitScriptAction(60));
        a.add(new CutsceneActions.CameraPan(17, 5, 220));
        a.add(Say.arthur("A full house tonight. Just like the old days."));
        a.add(new WaitScriptAction(60));

        // closing narration
        a.add(Say.note("And so the hotel reopened its doors."));
        a.add(Say.note("Guests came from near and far, never suspecting that half the staff had worked there for over a hundred years."));
        a.add(Say.note("The living and the dead worked side by side, and the lights of the old hotel never went out again."));
        a.add(Say.note("THE END"));
        a.add(new WaitScriptAction(60));

        a.add(new CutsceneActions.ReturnToTitle());
        return a;
    }
}