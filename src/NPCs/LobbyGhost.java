package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Direction;
import Utils.Point;
import java.util.HashMap;

public class LobbyGhost extends NPC {
    private int totalAmountMoved = 0;
    private Direction direction = Direction.RIGHT;
    private float speed = 2;

    // constructor name must match the class name
    public LobbyGhost(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("ghostspritesheet_lobby.png"), 48, 48), "WALK_RIGHT");
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