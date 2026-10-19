package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Direction;
import Utils.Point;
import java.util.HashMap;

public class KitchenGhost extends NPC {
    private int totalAmountMoved = 0;
    private Direction direction = Direction.RIGHT;
    private float speed = 2;

    // constructor name must match the class name
    public KitchenGhost(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("ghostspritesheet_kitchen.png"), 48, 48), "WALK_RIGHT");
    }

    // makes the ghost walk back and forth, turning around at 128 pixels or when it hits something
    @Override
    public void performAction(Player player) {
        if (totalAmountMoved < 128) {
            float requestedMove = speed * direction.getVelocity();
            float amountMoved = moveXHandleCollision(requestedMove);
            totalAmountMoved += Math.abs(amountMoved);

            // if the ghost moved less than it tried to, it ran into something, so turn around
            if (Math.abs(amountMoved) < Math.abs(requestedMove)) {
                flipDirection();
            }
        }
        // ghost has moved 128 pixels in one direction, so turn around
        else {
            flipDirection();
        }

        // set animation to match the current walking direction
        if (direction == Direction.RIGHT) {
            currentAnimationName = "WALK_RIGHT";
        }
        else {
            currentAnimationName = "WALK_LEFT";
        }
    }

    // reverses direction and resets the distance counter
    private void flipDirection() {
        totalAmountMoved = 0;
        direction = (direction == Direction.LEFT) ? Direction.RIGHT : Direction.LEFT;
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(4)
                    .withBounds(11, 9, 25, 25)
                    .build()
            });
            put("STAND_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(4)
                    .withBounds(11, 9, 25, 25)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build()
            });
            put("WALK_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(1, 0), 8)
                    .withScale(4)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(11, 9, 25, 25)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 0), 8)
                    .withScale(4)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(11, 9, 25, 25)
                    .build()
            });
            put("WALK_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(1, 0), 8)
                    .withScale(4)
                    .withBounds(11, 9, 25, 25)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 0), 8)
                    .withScale(4)
                    .withBounds(11, 9, 25, 25)
                    .build()
            });
        }};
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}