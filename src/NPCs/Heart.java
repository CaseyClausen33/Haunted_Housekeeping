package NPCs;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;

import java.util.HashMap;

// A small floating heart used in the ending. Heart.png is a single 16x16 frame.
// Its collision box is tiny so it can never block the player.
public class Heart extends NPC {

    public Heart(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("Heart.png"), 16, 16), "STAND_RIGHT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0)).withScale(3).withBounds(7, 7, 1, 1).build()
            });
            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0)).withScale(3).withBounds(7, 7, 1, 1).build()
            });
            put("WALK_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0)).withScale(3).withBounds(7, 7, 1, 1).build()
            });
            put("WALK_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0)).withScale(3).withBounds(7, 7, 1, 1).build()
            });
        }};
    }
}