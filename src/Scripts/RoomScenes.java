package Scripts;

import java.util.ArrayList;

import Level.Script;
import Puzzles.StoryProgress;
import Puzzles.StoryProgress.Room;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import ScriptActions.WaitScriptAction;

// The short scenes that play in each puzzle room: one before the puzzle and one after it is solved.
// Every "after" scene leaves a clue about who has been haunting the hotel:
//   bathroom = a rose petal, kitchen = a recipe card, library = a note in a book, bedroom = a locket.
public class RoomScenes {

    public static Script before(Room room) {
        switch (room) {
            case BATHROOM: return new SceneScript(() -> scene(
                    new CutsceneActions.CameraShake(45, 4),
                    Say.note("The pipes shudder and bang inside the walls."),
                    Say.player("Whoa! What was that?",
                            "The pipes are rattling like crazy... and there's water all over the floor.",
                            "Is that a handprint on the wall? Like someone was just here."),
                    Say.player("Someone really doesn't want this place fixed.",
                            "Too bad. I've got a hotel to save.",
                            "If I can connect the pipes again, the water should start flowing.")));

            case KITCHEN: return new SceneScript(() -> scene(
                    new CutsceneActions.CameraShake(40, 3),
                    Say.note("Every burner on the stove flares to life at once!"),
                    Say.player("Hot! Hot! Hot!",
                            "Who left every burner on?!",
                            "The pots are banging by themselves... something is playing with the stove."),
                    Say.player("I have to turn them all off before this place burns down!")));

            case LIBRARY: return new SceneScript(() -> scene(
                    new CutsceneActions.CameraShake(40, 3),
                    Say.note("Books fly off the shelves and swirl through the air!"),
                    Say.player("Hey! Quit it!",
                            "The books keep flipping open and shut on their own...",
                            "Each cover has a twin somewhere. If I match them up, maybe they'll settle down.")));

            default: return new SceneScript(() -> scene(
                    Say.note("The lamp, the teddy bear, the clock and the mirror begin to glow, one after the other."),
                    Say.player("They're glowing... in order?",
                            "It's almost like the room wants me to remember something.",
                            "Okay. I'll play along.")));
        }
    }

    public static Script after(Room room) {
        switch (room) {
            case BATHROOM: return new SceneScript(() -> scene(
                    Say.note("Water rushes through the pipes. The faucets sparkle."),
                    Say.player("The water's running again!",
                            "Hm? What's floating in the sink?"),
                    Say.note("A single red rose petal drifts across the water."),
                    Say.player("A rose petal... in a bathroom nobody has used in years?"),
                    progress()));

            case KITCHEN: return new SceneScript(() -> scene(
                    Say.note("The last flame flickers out. The kitchen finally goes quiet."),
                    Say.player("Phew. All cooled down.",
                            "What's this stuck to the fridge? A recipe card?"),
                    Say.note("It's written in elegant, faded handwriting: \"Apple tart - his favorite. Made with love, E.\""),
                    Say.player("\"His favorite\"? Who's E?"),
                    progress()));

            case LIBRARY: return new SceneScript(() -> scene(
                    Say.note("The books drift back onto their shelves, one by one."),
                    Say.player("Finally, some peace and quiet.",
                            "Wait, one of them fell open on the floor."),
                    Say.note("A pressed flower is tucked inside, with a note: \"For the man who built this place. I never found the courage to tell him. - E.\""),
                    Say.player("Whoever wrote this was in love with someone here.",
                            "And she signed it with an E again."),
                    progress()));

            default: return new SceneScript(() -> scene(
                    Say.note("The glowing fades. The room feels warm again."),
                    Say.player("I think the curse is broken!"),
                    Say.note("Something glints under the pillow: a small silver locket."),
                    Say.player("It has an \"E\" engraved on it... and a tiny photo of my grandfather inside!",
                            "The roses, the recipe, the note, and now this. Somebody here cared about him very much."),
                    progress()));
        }
    }

    // "One room down..." etc, based on how many puzzles are solved when the scene starts
    private static ScriptAction progress() {
        switch (StoryProgress.solvedCount()) {
            case 1:  return Say.player("One room down, three to go.");
            case 2:  return Say.player("Two rooms down. Halfway there.");
            case 3:  return Say.player("Three down... just one more room to fix.");
            default: return Say.player("That's all four rooms! I should head back to the lobby.");
        }
    }

    private static ArrayList<ScriptAction> scene(ScriptAction... middle) {
        ArrayList<ScriptAction> actions = new ArrayList<>();
        actions.add(new LockPlayerScriptAction());
        actions.add(new WaitScriptAction(30));
        for (ScriptAction action : middle) {
            actions.add(action);
            actions.add(new WaitScriptAction(10));
        }
        actions.add(new WaitScriptAction(10));
        actions.add(new UnlockPlayerScriptAction());
        return actions;
    }
}