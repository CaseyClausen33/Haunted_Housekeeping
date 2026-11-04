package Scripts;

import java.util.ArrayList;

import Puzzles.StoryProgress;
import Level.ScriptState;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import ScriptActions.WaitScriptAction;
import Utils.Direction;

// Plays in the lobby the first time the player comes back after fixing all four rooms.
// The camera shows the stairs, where a puzzle has appeared, and the lobby ghost points the player at it.
public class LobbyAllRoomsScript extends SceneScript {

    private static final int LOBBY_GHOST = 1;

    // tile at the bottom of the stairs, where the puzzle appears
    private static final float STAIRS_X = 11, STAIRS_Y = 4.5f;

    public LobbyAllRoomsScript() {
        super(LobbyAllRoomsScript::buildActions);
    }

    private static ArrayList<ScriptAction> buildActions() {
        ArrayList<ScriptAction> a = new ArrayList<>();

        a.add(new LockPlayerScriptAction());
        a.add(new WaitScriptAction(40));
        a.add(new CutsceneActions.PlayerWalkTo(11, 9, false, 2, Direction.RIGHT));
        a.add(new CutsceneActions.FacePlayer(Direction.RIGHT));

        a.add(Say.note("The lobby lights flicker. Far above, something creaks."));
        a.add(new CutsceneActions.CameraPan(STAIRS_X, STAIRS_Y, 80));
        a.add(Say.note("A strange puzzle has appeared at the foot of the grand staircase."));
        a.add(new WaitScriptAction(30));
        a.add(new CutsceneActions.CameraFollowPlayer(80));

        a.add(new CutsceneActions.NPCGlide(LOBBY_GHOST, 9, 9, 3));
        a.add(Say.ghost("You did it! All four rooms are restored!",
                "The hotel hasn't looked this good in years.",
                "But look... something has sealed off the stairs.",
                "Whoever has been causing all this trouble must be waiting up there."));
        a.add(Say.player("I think I know who it is.",
                "A rose petal. A recipe card. A note in a book. A locket.",
                "They all came from the same person. Someone who loved my grandfather."));
        a.add(Say.ghost("Then be careful. Whatever is up there won't let you pass without a fight."));
        a.add(Say.player("I will. Let's finish this."));
        a.add(new UnlockPlayerScriptAction());
        a.add(new CutsceneActions.NPCGlide(LOBBY_GHOST, 5, 3, 3));

        // a.add(new UnlockPlayerScriptAction());
        return a;
    }
}