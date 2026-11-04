package Scripts;

import java.util.ArrayList;

import Puzzles.StoryProgress;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import ScriptActions.WaitScriptAction;
import Level.ScriptState;
import Utils.Direction;

// The ending. Plays in the lobby once all four puzzles are solved:
//   the lobby ghost congratulates the player -> the player works out who has been haunting the hotel ->
//   Evelyn appears and explains -> Arthur (the grandfather) comes down the stairs -> they reunite ->
//   all the ghosts gather and the hotel reopens.
// It needs these NPCs in LobbyMap2 (hidden until used): lobby ghost 1, kitchen ghost 4, Evelyn 5, Arthur 6, hearts 7-10.
public class LobbyFinalScript extends SceneScript {

    public static final int LOBBY_GHOST = 1;
    public static final int KITCHEN_GHOST = 4;
    public static final int EVELYN = 5;
    public static final int ARTHUR = 6;
    public static final int[] HEARTS = {7, 8, 9, 10};

    public LobbyFinalScript() {
        super(LobbyFinalScript::buildActions);
    }

    private static ArrayList<ScriptAction> buildActions() {
        ArrayList<ScriptAction> a = new ArrayList<>();

        a.add(new LockPlayerScriptAction());
        a.add(new WaitScriptAction(40));

        // --- the player walks to the middle of the lobby and the lobby ghost floats over ---
        a.add(new CutsceneActions.PlayerWalkTo(11, 9, false, 2, Direction.LEFT));
        a.add(new CutsceneActions.FacePlayer(Direction.LEFT));
        a.add(Say.note("The lobby lights flicker, then settle into a warm glow."));
        a.add(new CutsceneActions.NPCGlide(LOBBY_GHOST, 9, 9, 3));
        a.add(Say.ghost("You did it! All four rooms are restored!",
                "The hotel hasn't looked this good in years.",
                "But... whoever has been causing all this trouble still hasn't shown themselves."));
        a.add(Say.player("I think I know who it is.",
                "A rose petal. A recipe card. A note in a book. A locket.",
                "They all came from the same person. Someone who loved my grandfather."));
        a.add(new CutsceneActions.FacePlayer(Direction.RIGHT));
        a.add(Say.player("I know you're here. Please... come out."));
        a.add(Say.note("The air turns cold. Somewhere, someone is crying softly."));

        // --- Evelyn appears in front of the stairs ---
        a.add(new CutsceneActions.NPCAppear(EVELYN, 11, 6));
        a.add(new WaitScriptAction(25));
        a.add(Say.evelyn("...You figured it out.",
                "I'm so sorry. I never wanted to frighten anyone."));
        a.add(Say.player("Why did you do it?"));
        a.add(Say.evelyn("Because I loved him.",
                "Arthur was the kindest man I ever knew. We talked for hours in the library, and he always saved me the last slice of apple tart.",
                "I never told him how I felt. And then he got sick, and then... he was gone."));
        a.add(Say.evelyn("I couldn't bear the thought of a stranger running his hotel.",
                "So I made sure no one ever could."));
        a.add(Say.player("He wouldn't have wanted that. He'd have wanted the doors open and the halls full of people."));
        a.add(Say.evelyn("I know. I think I always knew.",
                "I just... missed him so much."));

        // --- Arthur appears at the top of the stairs and floats down ---
        a.add(Say.note("A warm golden light spills down the grand staircase."));
        a.add(new CutsceneActions.NPCAppear(ARTHUR, 11, 3));
        a.add(new WaitScriptAction(25));
        a.add(Say.arthur("Evelyn?"));
        a.add(new CutsceneActions.NPCFace(EVELYN, Direction.RIGHT));
        a.add(Say.evelyn("...Arthur?"));
        a.add(new CutsceneActions.NPCGlide(ARTHUR, 12, 6, 1.5f));
        a.add(Say.arthur("I'd moved on long ago, but tonight something pulled me back. I thought it was the hotel calling me home.",
                "It was you."));
        a.add(Say.evelyn("You... you felt the same?"));
        a.add(Say.arthur("I was a foolish old man who never found the words.",
                "I loved you, Evelyn. I always have."));
        a.add(new CutsceneActions.HeartsRise(HEARTS, EVELYN, ARTHUR, 140));
        a.add(Say.note("Evelyn laughs through her tears."));
        a.add(Say.evelyn("I waited so long to hear that."));

        // --- Arthur meets the player ---
        a.add(new CutsceneActions.NPCFace(ARTHUR, Direction.LEFT));
        a.add(Say.player("Grandpa...?"));
        a.add(Say.arthur("And you must be my grandchild!",
                "Look at what you've done with the old place. It's magnificent."));
        a.add(Say.player("Well... the haunting did make it a little harder."));
        a.add(Say.evelyn("I'm sorry about that. I'll make it up to you. I promise."));

        // --- the rest of the ghosts join them ---
        a.add(new CutsceneActions.NPCAppear(KITCHEN_GHOST, 13, 8));
        a.add(new WaitScriptAction(15));
        a.add(Say.ghost("So... does this mean we can finally open for business?"));
        a.add(Say.arthur("It most certainly does!"));
        a.add(Say.player("Then let's get to work. Together!"));
        a.add(Say.arthur("Together!"));
        a.add(Say.evelyn("Together."));
        a.add(new WaitScriptAction(30));

        // --- epilogue ---
        a.add(Say.note("And so the hotel reopened its doors."));
        a.add(Say.note("Guests came from near and far, never suspecting that half the staff had worked there for over a hundred years."));
        a.add(Say.note("The living and the dead worked side by side, and the lights of the old hotel never went out again."));
        a.add(new CutsceneActions.NPCDisappear(KITCHEN_GHOST));
        a.add(Say.note("THE END"));

        // the player gets control back to walk around the finished hotel
        a.add(new MarkFinished());
        a.add(new UnlockPlayerScriptAction());
        return a;
    }

    private static class MarkFinished extends ScriptAction {
        @Override
        public ScriptState execute() {
            // StoryProgress.finalSeen = true;
            return ScriptState.COMPLETED;
        }
    }
}