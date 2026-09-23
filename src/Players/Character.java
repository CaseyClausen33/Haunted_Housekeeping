package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Player;

import java.util.HashMap;

// Player character using a 1-column x 2-row spritesheet (48x48 cells, 1px spacing)
// Sheet layout: (0,0) = stand pose, (0,1) = walk pose
// All frames face right; left-facing animations mirror them with FLIP_HORIZONTAL
public class Character extends Player {

    public Character(float x, float y) {
        super(new SpriteSheet(ImageLoader.load("characterspritesheet4.png"), 48, 48), x, y, "STAND_RIGHT");
        walkSpeed = 2.3f;
    }

    public void update() {
        super.update();
    }

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            // Body-only hitbox (excludes the gun barrel): sprite pixels span x=15..35, y=7..41 in each cell.
            // x=16, width=16 is centered on the cell so it stays correct when flipped.
            put("STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(2)
                            .withBounds(16, 29, 16, 12)
                            .build()
            });

            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(16, 29, 16, 12)
                            .build()
            });

            // Only one walk pose exists, so the cycle alternates walk pose <-> stand pose
            put("WALK_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 12)
                            .withScale(2)
                            .withBounds(16, 29, 16, 12)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 0), 12)
                            .withScale(2)
                            .withBounds(16, 29, 16, 12)
                            .build()
            });

            put("WALK_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 12)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(16, 29, 16, 12)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 0), 12)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(16, 29, 16, 12)
                            .build()
            });
        }};
    }
}