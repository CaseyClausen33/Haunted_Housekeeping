package Scripts;

import java.util.ArrayList;

import Level.Script;
import Maps.LobbyMap2;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import ScriptActions.WaitScriptAction;
import Utils.Direction;
import Level.ScriptState;

// Second half of the opening: the player looks around the lobby, goes to the front desk, meets the ghost,
// and is told about the four rooms that need cleaning. When it ends the player is standing in front of the desk
// and can move again.
public class LobbyIntroScript extends Script {

    // Tile coordinates used by the scene (see lobby_map2.txt). Tiles can be fractional for the camera pans.
    private static final int DESK_FRONT_X = 5, DESK_FRONT_Y = 5;      // where the player stands to talk to the desk
    private static final int DESK_TILE_Y = 4;                         // the row the front desk is in
    private static final int GHOST_TILE_X = 5;                        // the ghost appears behind the desk at this column

    private static final float BATHROOM_DOOR_X = 22, BATHROOM_DOOR_Y = 1;
    private static final float KITCHEN_DOOR_X = 17, KITCHEN_DOOR_Y = 1;
    private static final float LIBRARY_DOOR_X = 6.5f, LIBRARY_DOOR_Y = 1;   // the double door
    private static final float BEDROOM_DOOR_X = 2, BEDROOM_DOOR_Y = 1;

    private static final int GHOST_ID = 1;

    // Put this in front of everything the ghost says so it is clear who is talking. Set to "" to turn it off.
    private static final String GHOST_LABEL = "Ghost: ";

    private static final float WALK_SPEED = 2;

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> a = new ArrayList<>();

        a.add(new LockPlayerScriptAction());
        a.add(new WaitScriptAction(40));

        // --- the player looks around the lobby ---
        a.add(new CutsceneActions.FacePlayer(Direction.LEFT));
        a.add(new WaitScriptAction(45));
        a.add(new CutsceneActions.FacePlayer(Direction.RIGHT));
        a.add(new WaitScriptAction(45));
        a.add(new CutsceneActions.FacePlayer(Direction.LEFT));
        a.add(new WaitScriptAction(30));
        a.add(new CutsceneActions.FacePlayer(Direction.RIGHT));
        a.add(new WaitScriptAction(20));
        a.add(player("What happened here..."));
        a.add(new WaitScriptAction(20));

        // --- walk to the front desk (up the rug, left past the stairs, then up to the desk) ---
        a.add(new CutsceneActions.PlayerWalkTo(11, 9, false, WALK_SPEED, Direction.RIGHT));
        a.add(new CutsceneActions.PlayerWalkTo(7, 9, true, WALK_SPEED, Direction.LEFT));
        a.add(new CutsceneActions.PlayerWalkTo(7, DESK_FRONT_Y, false, WALK_SPEED, Direction.LEFT));
        a.add(new CutsceneActions.PlayerWalkTo(DESK_FRONT_X, DESK_FRONT_Y, true, WALK_SPEED, Direction.RIGHT));
        a.add(new CutsceneActions.FacePlayer(Direction.RIGHT));
        a.add(new WaitScriptAction(25));

        a.add(player("Is anyone here?"));
        a.add(new WaitScriptAction(70));                       // silence
        a.add(player("..."));
        a.add(player("Weird... the letter said they were eagerly awaiting my arrival..."));
        a.add(new WaitScriptAction(20));

        // --- the player starts to leave and the ghost pops up behind the desk ---
        a.add(new CutsceneActions.PlayerWalk(Direction.RIGHT, 100, WALK_SPEED, Direction.RIGHT));
        a.add(new WaitScriptAction(10));
        a.add(new CutsceneActions.GhostAppear(GHOST_ID, GHOST_TILE_X, DESK_TILE_Y));
        a.add(ghost("Hello!"));
        a.add(new CutsceneActions.PlayerJump(Direction.LEFT, 30, 18));
        a.add(new WaitScriptAction(10));
        a.add(player("You're a-a-a-a...."));
        a.add(ghost("A ghost!", "Though some prefer poltergeist."));
        a.add(player("But that's not possible!"));
        a.add(ghost("Of course it is! How else would we keep this hotel running?"));
        a.add(player("Wait... you're not saying there's more ghosts here?"));

        TextboxScriptAction backstory = ghostAction(
                "Of course!",
                "Once upon a time, we all worked here. We loved it so much, we never wanted to leave!",
                "It's too bad your grandfather didn't stick around too...",
                "He was the best owner this hotel ever had.",
                "But now it belongs to you!",
                "Unfortunately, the place went to shambles while he was sick...");
        // the player picks a reply; both lead to the same place
        backstory.addText(GHOST_LABEL + "You'll have some work to do if you want to get it operational again.",
                new String[] {"Of course I do!", "Let's get right to work!"});
        a.add(backstory);

        // --- the ghost lists the four rooms and the camera pans to each door ---
        a.add(ghost("You have four rooms to clean up."));
        a.add(new CutsceneActions.CameraPan(BATHROOM_DOOR_X, BATHROOM_DOOR_Y, 70));
        a.add(ghost("The bathroom..."));
        a.add(new CutsceneActions.CameraPan(KITCHEN_DOOR_X, KITCHEN_DOOR_Y, 60));
        a.add(ghost("The kitchen..."));
        a.add(new CutsceneActions.CameraPan(LIBRARY_DOOR_X, LIBRARY_DOOR_Y, 70));
        a.add(ghost("The library..."));
        a.add(new CutsceneActions.CameraPan(BEDROOM_DOOR_X, BEDROOM_DOOR_Y, 60));
        a.add(ghost("And a bedroom."));
        a.add(new WaitScriptAction(15));
        a.add(new CutsceneActions.CameraFollowPlayer(80));

        // --- the player steps back to the front of the desk for the warning ---
        a.add(new CutsceneActions.PlayerWalkTo(DESK_FRONT_X, DESK_FRONT_Y, true, WALK_SPEED, Direction.RIGHT));
        a.add(new CutsceneActions.FacePlayer(Direction.RIGHT));
        a.add(new WaitScriptAction(20));

        a.add(ghost("I should warn you... there have been some weird things going on in the hotel.",
                "One of the ghosts has been missing ever since your grandfather died."));
        a.add(ghost("Good luck with your task!"));

        // --- cutscene over: the player is in front of the desk and can move again ---
        a.add(new MarkSeen());
        a.add(new UnlockPlayerScriptAction());

        return a;
    }

    private TextboxScriptAction player(String... lines) {
        return new TextboxScriptAction(lines);
    }

    private TextboxScriptAction ghost(String... lines) {
        return ghostAction(lines);
    }

    private TextboxScriptAction ghostAction(String... lines) {
        TextboxScriptAction action = new TextboxScriptAction();
        for (String line : lines) {
            action.addText(GHOST_LABEL + line);
        }
        return action;
    }

    // remembers that the scene has played so it does not repeat the next time the lobby loads
    private static class MarkSeen extends ScriptAction {
        @Override
        public ScriptState execute() {
            LobbyMap2.lobbyIntroSeen = true;
            return ScriptState.COMPLETED;
        }
    }
}