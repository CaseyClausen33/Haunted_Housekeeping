package Scripts;

import Engine.ScreenManager;
import Level.Map;
import Level.NPC;
import java.util.Random;
import Level.ScriptState;
import ScriptActions.ScriptAction;
import Utils.Direction;
import Utils.Point;

// Reusable custom script actions for cutscenes (player walking, jumping, ghost flicker, camera pans).
// Use them like any other ScriptAction: actions.add(new CutsceneActions.PlayerWalkTo(...));
public class CutsceneActions {

    // A player standing "on" a tile has their sprite's top-left corner at the tile's top-left plus this offset.
    // (-4, 1) centers a 72px-wide player on a 64px tile with their feet near the bottom of the tile.
    // Tune these if your player sprite is a different size.
    public static final float PLAYER_TILE_OFFSET_X = -4;
    public static final float PLAYER_TILE_OFFSET_Y = 1;

    // Walks the player along ONE axis until they are standing on the given tile's column (horizontal = true)
    // or row (horizontal = false). Chain a few of these to walk around furniture.
    // endFace is the way the player faces while walking up/down and when they stop.
    public static class PlayerWalkTo extends ScriptAction {
        private final int tileX;
        private final int tileY;
        private final boolean horizontal;
        private final float speed;
        private final Direction endFace;

        public PlayerWalkTo(int tileX, int tileY, boolean horizontal, float speed, Direction endFace) {
            this.tileX = tileX;
            this.tileY = tileY;
            this.horizontal = horizontal;
            this.speed = speed;
            this.endFace = endFace;
        }

        @Override
        public ScriptState execute() {
            Point tile = map.getMapTile(tileX, tileY).getLocation();
            float delta = horizontal
                    ? tile.x + PLAYER_TILE_OFFSET_X - player.getX()
                    : tile.y + PLAYER_TILE_OFFSET_Y - player.getY();

            if (Math.abs(delta) < 0.5f) {
                player.stand(endFace);
                return ScriptState.COMPLETED;
            }

            float step = Math.min(speed, Math.abs(delta));
            Direction dir = horizontal ? (delta > 0 ? Direction.RIGHT : Direction.LEFT)
                                       : (delta > 0 ? Direction.DOWN : Direction.UP);
            player.walk(dir, step);
            player.setFacingDirection(horizontal ? dir : endFace);   // keep the sideways walking animation when moving up/down
            return ScriptState.RUNNING;
        }
    }

    // Walks the player a set number of pixels in one direction
    public static class PlayerWalk extends ScriptAction {
        private final Direction direction;
        private final float distance;
        private final float speed;
        private final Direction face;
        private float moved;

        public PlayerWalk(Direction direction, float distance, float speed, Direction face) {
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
            player.setFacingDirection(face);
            moved += step;
            if (moved >= distance) {
                player.stand(face);
                return ScriptState.COMPLETED;
            }
            return ScriptState.RUNNING;
        }
    }

    public static class FacePlayer extends ScriptAction {
        private final Direction direction;

        public FacePlayer(Direction direction) {
            this.direction = direction;
        }

        @Override
        public ScriptState execute() {
            player.stand(direction);
            return ScriptState.COMPLETED;
        }
    }

    // The player turns to face a direction and jumps in surprise
    public static class PlayerJump extends ScriptAction {
        private final Direction turnTo;
        private final float height;
        private final int frames;
        private int frame;
        private float lastOffset;

        public PlayerJump(Direction turnTo, float height, int frames) {
            this.turnTo = turnTo;
            this.height = height;
            this.frames = frames;
        }

        @Override
        public void setup() {
            frame = 0;
            lastOffset = 0;
            player.stand(turnTo);
        }

        @Override
        public ScriptState execute() {
            frame++;
            float t = frame / (float) frames;
            float offset = height * 4 * t * (1 - t);
            player.moveY(-(offset - lastOffset));
            lastOffset = offset;
            return frame >= frames ? ScriptState.COMPLETED : ScriptState.RUNNING;
        }
    }

    // Makes a hidden NPC flicker into view standing behind a tile. Its feet end up OVERLAP pixels below the top of
    // the tile in row standTileY, so for a desk in that row the ghost looks like it is standing behind the desk.
    public static class GhostAppear extends ScriptAction {
        private static final int OVERLAP = 24;
        private static final int FRAMES_PER_STEP = 3;
        private static final boolean[] FLICKER = {true, false, true, false, false, true, true, false, true, true, true, true};

        private final int ghostId;
        private final int tileX;
        private final int standTileY;
        private NPC ghost;
        private int frame;

        public GhostAppear(int ghostId, int tileX, int standTileY) {
            this.ghostId = ghostId;
            this.tileX = tileX;
            this.standTileY = standTileY;
        }

        @Override
        public void setup() {
            ghost = map.getNPCById(ghostId);
            Point tile = map.getMapTile(tileX, standTileY).getLocation();
            ghost.setLocation(tile.x + (64 - ghost.getWidth()) / 2f, tile.y + OVERLAP - ghost.getHeight());
            ghost.setIsHidden(true);
            frame = 0;
        }

        @Override
        public ScriptState execute() {
            int step = frame / FRAMES_PER_STEP;
            ghost.setIsHidden(!FLICKER[Math.min(step, FLICKER.length - 1)]);
            frame++;
            return step >= FLICKER.length ? ScriptState.COMPLETED : ScriptState.RUNNING;
        }
    }

    // Slides the camera so the given spot is centered on screen. tileX/tileY are tile coordinates and can be
    // fractional (6.5 = halfway between tile 6 and tile 7). The camera stops following the player until
    // CameraFollowPlayer is used.
    public static class CameraPan extends ScriptAction {
        private final float centerX;
        private final float centerY;
        private final int frames;
        private float startX, startY, targetX, targetY;
        private int frame;

        public CameraPan(float tileX, float tileY, int frames) {
            this.centerX = (tileX + 0.5f) * 64;
            this.centerY = (tileY + 0.5f) * 64;
            this.frames = frames;
        }

        @Override
        public void setup() {
            map.setAdjustCamera(false);
            startX = map.getCamera().getX();
            startY = map.getCamera().getY();
            targetX = clamp(centerX - ScreenManager.getScreenWidth() / 2f, map.getWidthPixels() - ScreenManager.getScreenWidth());
            targetY = clamp(centerY - ScreenManager.getScreenHeight() / 2f, map.getHeightPixels() - ScreenManager.getScreenHeight());
            frame = 0;
        }

        @Override
        public ScriptState execute() {
            frame++;
            float t = Math.min(1f, frame / (float) frames);
            float eased = t * t * (3 - 2 * t);   // slow start, slow finish
            map.getCamera().setLocation(startX + (targetX - startX) * eased, startY + (targetY - startY) * eased);
            return frame >= frames ? ScriptState.COMPLETED : ScriptState.RUNNING;
        }
    }

    // Slides the camera back onto the player and turns normal camera following back on
    public static class CameraFollowPlayer extends ScriptAction {
        private final int frames;
        private float startX, startY, targetX, targetY;
        private int frame;

        public CameraFollowPlayer(int frames) {
            this.frames = frames;
        }

        @Override
        public void setup() {
            startX = map.getCamera().getX();
            startY = map.getCamera().getY();
            float playerCenterX = player.getX() + player.getWidth() / 2f;
            float playerCenterY = player.getY() + player.getHeight() / 2f;
            targetX = clamp(playerCenterX - ScreenManager.getScreenWidth() / 2f, map.getWidthPixels() - ScreenManager.getScreenWidth());
            targetY = clamp(playerCenterY - ScreenManager.getScreenHeight() / 2f, map.getHeightPixels() - ScreenManager.getScreenHeight());
            frame = 0;
        }

        @Override
        public ScriptState execute() {
            frame++;
            float t = Math.min(1f, frame / (float) frames);
            float eased = t * t * (3 - 2 * t);
            map.getCamera().setLocation(startX + (targetX - startX) * eased, startY + (targetY - startY) * eased);
            if (frame >= frames) {
                map.setAdjustCamera(true);
                return ScriptState.COMPLETED;
            }
            return ScriptState.RUNNING;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Added for the story scenes
    // ---------------------------------------------------------------------------------------------

    // Rattles the screen (pipes rattling, books flying...). The camera stops following the player while it shakes.
    public static class CameraShake extends ScriptAction {
        private final int frames;
        private final float amplitude;
        private final Random random = new Random();
        private float baseX, baseY;
        private int frame;

        public CameraShake(int frames, float amplitude) {
            this.frames = frames;
            this.amplitude = amplitude;
        }

        @Override
        public void setup() {
            map.setAdjustCamera(false);
            baseX = map.getCamera().getX();
            baseY = map.getCamera().getY();
            frame = 0;
        }

        @Override
        public ScriptState execute() {
            frame++;
            if (frame >= frames) {
                map.getCamera().setLocation(baseX, baseY);
                map.setAdjustCamera(true);
                return ScriptState.COMPLETED;
            }
            float falloff = 1f - frame / (float) frames;   // the shaking dies down
            float dx = (random.nextFloat() * 2 - 1) * amplitude * falloff;
            float dy = (random.nextFloat() * 2 - 1) * amplitude * falloff;
            map.getCamera().setLocation(baseX + dx, baseY + dy);
            return ScriptState.RUNNING;
        }
    }

    // Puts a hidden NPC on a tile (feet near the bottom of the tile) and flickers it into view
    public static class NPCAppear extends ScriptAction {
        private static final int FRAMES_PER_STEP = 3;
        private static final boolean[] FLICKER = {true, false, true, false, false, true, true, false, true, true, true, true};

        private final int npcId;
        private final float tileX;
        private final float tileY;
        private NPC npc;
        private int frame;

        public NPCAppear(int npcId, float tileX, float tileY) {
            this.npcId = npcId;
            this.tileX = tileX;
            this.tileY = tileY;
        }

        @Override
        public void setup() {
            npc = map.getNPCById(npcId);
            npc.setLocation(NPCGlide.targetX(map, npc, tileX), NPCGlide.targetY(map, npc, tileY));
            npc.setIsHidden(true);
            frame = 0;
        }

        @Override
        public ScriptState execute() {
            int step = frame / FRAMES_PER_STEP;
            npc.setIsHidden(!FLICKER[Math.min(step, FLICKER.length - 1)]);
            frame++;
            return step >= FLICKER.length ? ScriptState.COMPLETED : ScriptState.RUNNING;
        }
    }

    // Flickers an NPC out of view
    public static class NPCDisappear extends ScriptAction {
        private static final int FRAMES_PER_STEP = 3;
        private static final boolean[] FLICKER = {true, false, true, false, false, true, false, false, false, false};

        private final int npcId;
        private NPC npc;
        private int frame;

        public NPCDisappear(int npcId) {
            this.npcId = npcId;
        }

        @Override
        public void setup() {
            npc = map.getNPCById(npcId);
            frame = 0;
        }

        @Override
        public ScriptState execute() {
            int step = frame / FRAMES_PER_STEP;
            npc.setIsHidden(!FLICKER[Math.min(step, FLICKER.length - 1)]);
            frame++;
            return step >= FLICKER.length ? ScriptState.COMPLETED : ScriptState.RUNNING;
        }
    }

    // Floats an NPC in a straight line to a tile (no walking animation needed, so it works for every NPC)
    public static class NPCGlide extends ScriptAction {
        private final int npcId;
        private final float tileX;
        private final float tileY;
        private final float speed;
        private NPC npc;

        public NPCGlide(int npcId, float tileX, float tileY, float speed) {
            this.npcId = npcId;
            this.tileX = tileX;
            this.tileY = tileY;
            this.speed = speed;
        }

        // where an NPC's top-left corner goes so it is centered on the tile with its feet near the bottom
        static float targetX(Map map, NPC npc, float tileX) {
            return tileX * 64 + (64 - npc.getWidth()) / 2f;
        }

        static float targetY(Map map, NPC npc, float tileY) {
            return tileY * 64 + 64 - npc.getHeight() + 6;
        }

        @Override
        public void setup() {
            npc = map.getNPCById(npcId);
        }

        @Override
        public ScriptState execute() {
            float dx = targetX(map, npc, tileX) - npc.getX();
            float dy = targetY(map, npc, tileY) - npc.getY();
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            if (distance <= speed) {
                npc.setLocation(targetX(map, npc, tileX), targetY(map, npc, tileY));
                return ScriptState.COMPLETED;
            }
            npc.moveX(dx / distance * speed);
            npc.moveY(dy / distance * speed);
            return ScriptState.RUNNING;
        }
    }

    // Hearts drift up from above two NPCs, sway a little, then disappear.
    // Needs one hidden Heart NPC per heart (ids passed in).
    public static class HeartsRise extends ScriptAction {
        private final int[] heartIds;
        private final int npcA;
        private final int npcB;
        private final int frames;
        private final float[] baseX;
        private final float[] baseY;
        private int frame;

        public HeartsRise(int[] heartIds, int npcA, int npcB, int frames) {
            this.heartIds = heartIds;
            this.npcA = npcA;
            this.npcB = npcB;
            this.frames = frames;
            this.baseX = new float[heartIds.length];
            this.baseY = new float[heartIds.length];
        }

        @Override
        public void setup() {
            for (int i = 0; i < heartIds.length; i++) {
                NPC heart = map.getNPCById(heartIds[i]);
                NPC owner = map.getNPCById(i % 2 == 0 ? npcA : npcB);
                int row = i / 2;
                baseX[i] = owner.getX() + owner.getWidth() / 2f - heart.getWidth() / 2f + (row % 2 == 0 ? -10 : 12);
                baseY[i] = owner.getY() - heart.getHeight() - 6 - row * 26;
                heart.setLocation(baseX[i], baseY[i]);
                heart.setIsHidden(false);
            }
            frame = 0;
        }

        @Override
        public ScriptState execute() {
            frame++;
            for (int i = 0; i < heartIds.length; i++) {
                NPC heart = map.getNPCById(heartIds[i]);
                float rise = frame * 0.7f;
                float sway = (float) Math.sin(frame * 0.12 + i) * 6;
                heart.setLocation(baseX[i] + sway, baseY[i] - rise);
            }
            if (frame >= frames) {
                for (int id : heartIds) {
                    map.getNPCById(id).setIsHidden(true);
                }
                return ScriptState.COMPLETED;
            }
            return ScriptState.RUNNING;
        }
    }

    // Makes an NPC face left or right (needs that NPC to have STAND_LEFT / STAND_RIGHT animations)
    public static class NPCFace extends ScriptAction {
        private final int npcId;
        private final Direction direction;

        public NPCFace(int npcId, Direction direction) {
            this.npcId = npcId;
            this.direction = direction;
        }

        @Override
        public ScriptState execute() {
            map.getNPCById(npcId).stand(direction);
            return ScriptState.COMPLETED;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Added for the boss room and the ending
    // ---------------------------------------------------------------------------------------------

    // Puts a hidden NPC a number of tiles away from the player (so it works in any map) and flickers it into view.
    // dxTiles > 0 is to the right of the player, dyTiles > 0 is below the player.
    public static class NPCAppearNear extends ScriptAction {
        private static final int FRAMES_PER_STEP = 3;
        private static final boolean[] FLICKER = {true, false, true, false, false, true, true, false, true, true, true, true};

        private final int npcId;
        private final float dxTiles;
        private final float dyTiles;
        private NPC npc;
        private int frame;

        public NPCAppearNear(int npcId, float dxTiles, float dyTiles) {
            this.npcId = npcId;
            this.dxTiles = dxTiles;
            this.dyTiles = dyTiles;
        }

        @Override
        public void setup() {
            npc = map.getNPCById(npcId);
            float feetY = player.getY() + player.getHeight();
            npc.setLocation(player.getX() + player.getWidth() / 2f - npc.getWidth() / 2f + dxTiles * 64,
                    feetY + dyTiles * 64 - npc.getHeight() + 6);
            npc.setIsHidden(true);
            frame = 0;
        }

        @Override
        public ScriptState execute() {
            int step = frame / FRAMES_PER_STEP;
            npc.setIsHidden(!FLICKER[Math.min(step, FLICKER.length - 1)]);
            frame++;
            return step >= FLICKER.length ? ScriptState.COMPLETED : ScriptState.RUNNING;
        }
    }

    // Hands control to another script (for example a ChangeMapScript) and leaves this one "running".
    // Build the other script at the call site, e.g. new CutsceneActions.HandOffTo(() -> new ChangeMapScript(LobbyMap2::new))
    public static class HandOffTo extends ScriptAction {
        private final java.util.function.Supplier<Level.Script> next;

        public HandOffTo(java.util.function.Supplier<Level.Script> next) {
            this.next = next;
        }

        @Override
        public ScriptState execute() {
            Level.Script script = next.get();
            script.setMap(map);
            script.setPlayer(player);
            script.setListeners(map.getListeners());
            script.initialize();
            player.unlock();   // a map change should not leave the player locked
            map.setActiveScript(script);
            return ScriptState.RUNNING;   // the map now runs the other script
        }
    }

    // Ends the game and goes back to the title screen. It tells the game it has been won (PlayLevelScreen.onWin).
    // Patch PlayLevelScreen.onWin() to call goBackToMenu() so it skips the "You win!" screen (see the notes).
    public static class ReturnToTitle extends ScriptAction {
        @Override
        public ScriptState execute() {
            Puzzles.StoryProgress.resetForNewGame();
            for (Level.GameListener listener : map.getListeners()) {
                listener.onWin();
            }
            return ScriptState.RUNNING;
        }
    }

    private static float clamp(float value, float max) {
        return Math.max(0, Math.min(max, value));
    }
}