package NPCs;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;

import java.util.HashMap;

// A hotel guest for the final shot. variant 1-6 picks the sprite (Guest1.png ... Guest6.png).
// Each PNG is two 24x24 frames side by side with a 1px gap (49x24 image).
public class Guest extends NPC {

    public Guest(int id, Point location, int variant, boolean facingLeft) {
        super(id, location.x, location.y,
                new SpriteSheet(ImageLoader.load("Guest" + variant + ".png"), 24, 24),
                facingLeft ? "STAND_LEFT" : "STAND_RIGHT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 30)
                            .withScale(3).withBounds(6, 6, 12, 16).build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 30)
                            .withScale(3).withBounds(6, 6, 12, 16).build()
            });
            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 30)
                            .withScale(3).withImageEffect(ImageEffect.FLIP_HORIZONTAL).withBounds(6, 6, 12, 16).build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 30)
                            .withScale(3).withImageEffect(ImageEffect.FLIP_HORIZONTAL).withBounds(6, 6, 12, 16).build()
            });
            put("WALK_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 10)
                            .withScale(3).withBounds(6, 6, 12, 16).build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 10)
                            .withScale(3).withBounds(6, 6, 12, 16).build()
            });
            put("WALK_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 10)
                            .withScale(3).withImageEffect(ImageEffect.FLIP_HORIZONTAL).withBounds(6, 6, 12, 16).build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 10)
                            .withScale(3).withImageEffect(ImageEffect.FLIP_HORIZONTAL).withBounds(6, 6, 12, 16).build()
            });
        }};
    }
}