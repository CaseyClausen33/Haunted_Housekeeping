package Scripts;

import Engine.ScreenManager;
import Level.NPC;
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

    private static float clamp(float value, float max) {
        return Math.max(0, Math.min(max, value));
    }
}