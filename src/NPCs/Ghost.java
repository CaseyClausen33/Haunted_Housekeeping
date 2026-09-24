package NPCs;

import java.util.HashMap;

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

public class Ghost extends NPC {
    private int totalAmountMoved = 0;
    private Direction direction = Direction.RIGHT;
    private float speed = 1;
    
    public Ghost(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("ghostspritesheet.png"), 48, 48), "WALK_RIGHT");
    }

    // this code makes the ghost npc walk back and forth (left to right)
    @Override
    public void performAction(Player player) {
        // if ghost has not yet moved 90 pixels in one direction, move ghost forward
        if (totalAmountMoved < 90) {
            float amountMoved = moveXHandleCollision(speed * direction.getVelocity());
            totalAmountMoved += Math.abs(amountMoved);
        }

        // else if ghost has already moved 90 pixels in one direction, flip the ghost's direction
        else {
            totalAmountMoved = 0;
            if (direction == Direction.LEFT) {
                direction = Direction.RIGHT;
            }
            else {
                direction = Direction.LEFT;
            }
        }

        // based off of the ghosts current walking direction, set its animation to match
        if (direction == Direction.RIGHT) {
            currentAnimationName = "WALK_RIGHT";
        }
        else {
            currentAnimationName = "WALK_LEFT";
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(2)
                    .withBounds(11, 9, 25, 25)
                    .build()
            });
            put("STAND_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(2)
                    .withBounds(11, 9, 25, 25)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build()
           });
           put("WALK_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(1, 0), 8)
                        .withScale(2)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                        .withBounds(11, 9, 25, 25)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 0), 8)
                        .withScale(2)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                        .withBounds(11, 9, 25, 25)
                        .build()
            });
            put("WALK_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(1, 0), 8)
                        .withScale(2)
                        .withBounds(11, 9, 25, 25)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 0), 8)
                        .withScale(2)
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
