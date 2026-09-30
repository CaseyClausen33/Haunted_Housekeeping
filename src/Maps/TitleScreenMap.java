package Maps;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.ImageEffect;
import GameObject.Sprite;
import Level.Map;
import Tilesets.CommonTileset;
import Utils.Colors;
import Utils.Point;

// Represents the map that is used as a background for the main menu and credits menu screen
public class TitleScreenMap extends Map {

    private Sprite character;

    public TitleScreenMap() {
        super("title_screen_map.txt", new CommonTileset());
        Point characterLocation = getMapTile(8, 9).getLocation().subtractX(6).subtractY(7);
        character = new Sprite(ImageLoader.loadSubImage("characterspritesheet4.png", Colors.MAGENTA, 0, 0, 48, 48));
        character.setScale(2);
        character.setImageEffect(ImageEffect.FLIP_HORIZONTAL);
        character.setLocation(characterLocation.x, characterLocation.y);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        character.draw(graphicsHandler);
    }
}
