package Scripts;

import java.util.ArrayList;

import Level.NPC;
import Level.Script;
import Level.ScriptState;
// import Puzzles.RoomProgress;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.NPCStandScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import ScriptActions.WaitScriptAction;
import Utils.Direction;
import Maps.OutsideMap;

// Opening cutscene on OutsideMap:
//   taxi pulls up -> player climbs out -> reads the letter -> walks up the path ->
//   a ghost flashes behind the player -> player jumps -> two lines of dialogue -> walks to the door and goes inside.
public class IntroCutsceneScript extends Script {

    // The player rides "inside" the taxi while it drives in: the player sprite is kept at this offset from the
    // taxi's top-left corner, hidden behind the taxi sprite. Tune these if your player sprite is not 72x72.
    private static final float PLAYER_OFFSET_X = 28;
    private static final float PLAYER_OFFSET_Y = 4;

    private static final int TAXI_ID = 1;
    private static final int GHOST_ID = 2;

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> actions = new ArrayList<>();

        actions.add(new LockPlayerScriptAction());
        actions.add(new WaitScriptAction(45));

        // --- the taxi pulls up and slows to a stop (480px = 7.5 tiles) ---
        actions.add(new TaxiRide(TAXI_ID, 300, 6));
        actions.add(new TaxiRide(TAXI_ID, 120, 3));
        actions.add(new TaxiRide(TAXI_ID, 60, 1));
        actions.add(new NPCStandScriptAction(TAXI_ID, Direction.RIGHT));
        actions.add(new WaitScriptAction(50));

        // --- player climbs out of the taxi and stands in front of the gate ---
        actions.add(new PlayerWalk(Direction.UP, 90, 2, Direction.RIGHT));
        actions.add(new WaitScriptAction(30));

        actions.add(new TextboxScriptAction("The letter says this is the place..."));
        actions.add(new WaitScriptAction(15));

        // --- walk halfway up the path ---
        actions.add(new PlayerWalk(Direction.UP, 330, 2, Direction.RIGHT));
        actions.add(new WaitScriptAction(20));

        // --- the ghost flashes behind the player and the player jumps ---
        actions.add(new GhostScare(GHOST_ID));
        actions.add(new WaitScriptAction(10));
        actions.add(new TextboxScriptAction("What was that?!?"));
        actions.add(new FacePlayer(Direction.RIGHT));
        actions.add(new WaitScriptAction(30));

        TextboxScriptAction thoughts = new TextboxScriptAction();
        thoughts.addText("I must be imagining things.");
        thoughts.addText("This place gives me the creeps.");   // shows after the player presses space
        actions.add(thoughts);
        actions.add(new WaitScriptAction(20));

        // --- walk the rest of the way to the door and go inside ---
        actions.add(new PlayerWalk(Direction.UP, 350, 2, Direction.RIGHT));
        actions.add(new WaitScriptAction(25));
        actions.add(new EnterDoor(14, 8));

        return actions;
    }

    // ---------------------------------------------------------------------------------------------
    // custom actions
    // ---------------------------------------------------------------------------------------------

    // Drives the taxi to the right and carries the (hidden) player along with it
    private static class TaxiRide extends ScriptAction {
        private final int taxiId;
        private final float distance;
        private final float speed;
        private NPC taxi;
        private float moved;

        TaxiRide(int taxiId, float distance, float speed) {
            this.taxiId = taxiId;
            this.distance = distance;
            this.speed = speed;
        }

        @Override
        public void setup() {
            taxi = map.getNPCById(taxiId);
            moved = 0;
        }

        @Override
        public ScriptState execute() {
            float step = Math.min(speed, distance - moved);
            taxi.walk(Direction.RIGHT, step);
            player.setLocation(taxi.getX() + PLAYER_OFFSET_X, taxi.getY() + PLAYER_OFFSET_Y);
            moved += step;
            return moved >= distance ? ScriptState.COMPLETED : ScriptState.RUNNING;
        }
    }

    // Makes the player walk in a direction for a set distance, keeping the sideways walking animation
    private static class PlayerWalk extends ScriptAction {
        private final Direction direction;
        private final float distance;
        private final float speed;
        private final Direction face;
        private float moved;

        PlayerWalk(Direction direction, float distance, float speed, Direction face) {
            this.direction = direction;
            this.distance = distance;
            this.speed = speed;
            this.face = face;
        }

        @Override
        public void setup() {
            moved = 0;
        }

        @Override
        public ScriptState execute() {
            float step = Math.min(speed, distance - moved);
            player.walk(direction, step);
            player.setFacingDirection(face);   // walking up/down would otherwise switch the animation to the left-facing one
            moved += step;
            if (moved >= distance) {
                player.stand(face);
                return ScriptState.COMPLETED;
            }
            return ScriptState.RUNNING;
        }
    }

    private static class FacePlayer extends ScriptAction {
        private final Direction direction;

        FacePlayer(Direction direction) {
            this.direction = direction;
        }

        @Override
        public ScriptState execute() {
            player.stand(direction);
            return ScriptState.COMPLETED;
        }
    }

    // The ghost appears just behind the player, streaks past for a moment and vanishes,
    // while the player turns to look and jumps.
    private static class GhostScare extends ScriptAction {
        private static final int JUMP_START = 8;
        private static final int JUMP_FRAMES = 18;
        private static final float JUMP_HEIGHT = 30;
        private static final int GHOST_FRAMES = 24;
        private static final int TOTAL_FRAMES = 40;

        private final int ghostId;
        private NPC ghost;
        private int frame;
        private float lastJumpOffset;

        GhostScare(int ghostId) {
            this.ghostId = ghostId;
        }

        @Override
        public void setup() {
            ghost = map.getNPCById(ghostId);
            ghost.setLocation(player.getX() - 20, player.getY() + 130);
            ghost.setIsHidden(false);
            frame = 0;
            lastJumpOffset = 0;
        }

        @Override
        public ScriptState execute() {
            // ghost streaks up and to the left, then disappears
            if (frame < GHOST_FRAMES) {
                ghost.moveX(-2.5f);
                ghost.moveY(-3.5f);
            } else {
                ghost.setIsHidden(true);
            }

            // player turns around and jumps
            if (frame == JUMP_START) {
                player.stand(Direction.LEFT);
            }
            if (frame >= JUMP_START && frame < JUMP_START + JUMP_FRAMES) {
                float t = (frame - JUMP_START + 1) / (float) JUMP_FRAMES;
                float offset = JUMP_HEIGHT * 4 * t * (1 - t);
                player.moveY(-(offset - lastJumpOffset));
                lastJumpOffset = offset;
            }

            frame++;
            return frame >= TOTAL_FRAMES ? ScriptState.COMPLETED : ScriptState.RUNNING;
        }
    }

    // Final step: marks the intro as seen and hands control to the door's interact script (the map change).
    private static class EnterDoor extends ScriptAction {
        private final int doorX;
        private final int doorY;

        EnterDoor(int doorX, int doorY) {
            this.doorX = doorX;
            this.doorY = doorY;
        }

        @Override
        public ScriptState execute() {
            // RoomProgress.introSeen = true;
            OutsideMap.OutsideCutsceneSeen = true;  // set the flag to true when the cutscene is completed
            Script doorScript = map.getMapTile(doorX, doorY).getInteractScript();
            if (doorScript == null) {
                player.unlock();             // nothing to hand off to, give control back
                return ScriptState.COMPLETED;
            }
            // From here the map runs the door script instead of this one, so this action just stays "running".
            map.setActiveScript(doorScript);
            return ScriptState.RUNNING;
        }
    }
}