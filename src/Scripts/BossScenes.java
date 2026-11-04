package Scripts;

import java.util.ArrayList;

import Level.Script;
import Level.ScriptState;
import Maps.LobbyMap2;
import NPCs.StoryNPCs;
import Puzzles.StoryProgress;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import ScriptActions.WaitScriptAction;
import Utils.Direction;
import Puzzles.StoryProgress;

// The two scenes in the boss room at the top of the stairs.
// They use StoryNPCs.EVELYN, ARTHUR and HEARTS, which must be in the boss room's loadNPCs()
// (StoryNPCs.addStoryGhosts does it in one line). The characters appear next to the player,
// so the scenes work wherever the player is standing in the room.
public class BossScenes {

    // Plays when the player first walks into the boss room: Evelyn confronts them
    public static Script before() {
        return new SceneScript(() -> {
            ArrayList<ScriptAction> a = new ArrayList<>();
            a.add(new LockPlayerScriptAction());
            a.add(new WaitScriptAction(40));
            a.add(Say.note("The room is freezing. Rose petals swirl through the air."));
            a.add(new CutsceneActions.CameraShake(40, 3));
            a.add(new CutsceneActions.NPCAppearNear(StoryNPCs.EVELYN, 4, 0));
            a.add(new CutsceneActions.FacePlayer(Direction.RIGHT));
            a.add(new WaitScriptAction(20));
            a.add(Say.evelyn("You've come a long way, little one.",
                    "Further than I thought you would."));
            a.add(Say.player("Evelyn. I know it was you."));
            a.add(Say.evelyn("...Then you know why I can't let you take his hotel."));
            a.add(Say.player("I'm not taking it. I'm trying to save it."));
            a.add(Say.evelyn("Arthur built every inch of this place. No one else has the right to run it!",
                    "If you truly want it, prove yourself. Solve my final trial, if you can."));
            a.add(new CutsceneActions.NPCDisappear(StoryNPCs.EVELYN));
            a.add(Say.player("Then let's settle this."));
            a.add(new UnlockPlayerScriptAction());
            return a;
        });
    }

    // Plays right after the boss puzzle is solved: Evelyn and Arthur are reunited, then the story moves on to the final shot
    public static Script after() {
        return new SceneScript(() -> {
            ArrayList<ScriptAction> a = new ArrayList<>();
            a.add(new LockPlayerScriptAction());
            a.add(new WaitScriptAction(40));
            a.add(Say.note("The cold fades from the room. The rose petals settle on the floor."));
            a.add(new CutsceneActions.NPCAppearNear(StoryNPCs.EVELYN, 3, 0));
            a.add(new CutsceneActions.FacePlayer(Direction.RIGHT));
            a.add(new WaitScriptAction(25));
            a.add(Say.evelyn("...You solved it.",
                    "I'm sorry. I never wanted to hurt anyone.",
                    "I just couldn't bear to lose him a second time."));
            a.add(Say.player("I know. You loved him."));
            a.add(Say.evelyn("I did. I never told him.",
                    "And then he got sick, and then... he was gone."));
            a.add(Say.player("He wouldn't have wanted this. He'd have wanted the doors open and the halls full of people."));
            a.add(Say.evelyn("I know. I think I always knew.",
                    "I just... missed him so much."));

            // Arthur appears on the other side of the player
            a.add(Say.note("A warm golden light fills the room."));
            a.add(new CutsceneActions.NPCAppearNear(StoryNPCs.ARTHUR, -3, 0));
            a.add(new WaitScriptAction(25));
            a.add(Say.arthur("Evelyn?"));
            a.add(new CutsceneActions.NPCFace(StoryNPCs.EVELYN, Direction.LEFT));
            a.add(Say.evelyn("...Arthur?"));
            a.add(Say.arthur("I'd moved on long ago, but tonight something pulled me back. I thought it was the hotel calling me home.",
                    "It was you."));
            a.add(Say.evelyn("You... you felt the same?"));
            a.add(Say.arthur("I was a foolish old man who never found the words.",
                    "I loved you, Evelyn. I always have."));
            a.add(new CutsceneActions.HeartsRise(StoryNPCs.HEARTS, StoryNPCs.EVELYN, StoryNPCs.ARTHUR, 140));
            a.add(Say.note("Evelyn laughs through her tears."));
            a.add(Say.evelyn("I waited so long to hear that."));

            // Arthur meets the player
            a.add(new CutsceneActions.NPCFace(StoryNPCs.ARTHUR, Direction.RIGHT));
            a.add(Say.player("Grandpa...?"));
            a.add(Say.arthur("And you must be my grandchild!",
                    "Look at what you've done with the old place. It's magnificent."));
            a.add(Say.player("Well... the haunting did make it a little harder."));
            a.add(Say.evelyn("I'm sorry about that. I'll make it up to you. I promise."));
            a.add(Say.arthur("Come, everyone. Let's show this old hotel what it has been missing."));
            a.add(Say.note("Every light in the hotel blazes on at once."));

            // on to the final shot: the lobby, full of guests
            a.add(new SetFlags());
            a.add(new CutsceneActions.HandOffTo(() -> new ChangeMapScript(LobbyMap2::new)));
            return a;
        });
    }

    private static class SetFlags extends ScriptAction {
        @Override
        public ScriptState execute() {
            StoryProgress.reunionSeen = true;
            StoryProgress.epilogueActive = true;   // the lobby is built full of guests from now on
            return ScriptState.COMPLETED;
        }
    }
}