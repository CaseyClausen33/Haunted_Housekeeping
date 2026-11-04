package NPCs;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;

import java.util.HashMap;

// The player's grandfather, Arthur, the hotel's old owner. Put Grandfather.png next to your other sprites.
// Grandfather.png is two 24x24 frames side by side with a 1px gap (49x24 image).
public class GrandfatherGhost extends NPC {

    public GrandfatherGhost(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("Grandfather.png"), 24, 24), "STAND_RIGHT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                            .withScale(3).withBounds(6, 6, 12, 14).build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                            .withScale(3).withBounds(6, 6, 12, 14).build()
            });
            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                            .withScale(3).withImageEffect(ImageEffect.FLIP_HORIZONTAL).withBounds(6, 6, 12, 14).build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                            .withScale(3).withImageEffect(ImageEffect.FLIP_HORIZONTAL).withBounds(6, 6, 12, 14).build()
            });
            put("WALK_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                            .withScale(3).withBounds(6, 6, 12, 14).build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                            .withScale(3).withBounds(6, 6, 12, 14).build()
            });
            put("WALK_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                            .withScale(3).withImageEffect(ImageEffect.FLIP_HORIZONTAL).withBounds(6, 6, 12, 14).build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                            .withScale(3).withImageEffect(ImageEffect.FLIP_HORIZONTAL).withBounds(6, 6, 12, 14).build()
            });
        }};
    }
}