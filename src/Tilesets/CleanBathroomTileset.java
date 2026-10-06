package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;
import java.util.ArrayList;

public class CleanBathroomTileset extends Tileset {

    private static final int TILE_SIZE = 127;

    public CleanBathroomTileset() {
        super(
            ImageLoader.load("cleanbathroomtileset.png"),
            TILE_SIZE,
            TILE_SIZE,
            1
        );
    }


   @Override
    public ArrayList<MapTileBuilder> defineTiles() {

        ArrayList<MapTileBuilder> tiles = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                Frame frame = new FrameBuilder(getSubImage(row, col))
                    .withScale(tileScale)
                    .withBounds(0, 0, TILE_SIZE, TILE_SIZE)
                    .build();

                // Tile number: 0-63
                int tileNumber = row * 8 + col;

                TileType tileType = TileType.PASSABLE;

                // Solid bathroom objects/walls
                if (
                    tileNumber == 2 || // wall
                    tileNumber == 4 || // wall
                    tileNumber == 6 || // window
                    tileNumber == 7 || // bathroom door
                    tileNumber == 16 ||  // bathtub
                    tileNumber == 17 ||  // bathtub
                    tileNumber == 18 ||  // bathtub
                    tileNumber == 19 ||  // shower
                    tileNumber == 20 ||  // toilet
                    tileNumber == 21 ||  // toilet
                    tileNumber == 22 ||  // sink
                    tileNumber == 23 ||  // sink
                    tileNumber == 24 ||  // shelfs
                    tileNumber == 25 ||  // wardrobe
                    tileNumber == 26 ||  // plant
                    tileNumber == 27 ||  // stool
                    tileNumber == 28 ||  // laundry basket
                    tileNumber == 29 ||  // stool/furniture
                    tileNumber == 32 ||  // bathroom object
                    tileNumber == 34 ||  // bathroom object
                    tileNumber == 33 ||  // bathroom object
                    tileNumber == 35 ||  // wall/object
                    tileNumber == 37 ||  // bathroom object
                    tileNumber == 38 ||  // broom/plunger
                    tileNumber == 39 ||  // trash can
                    tileNumber == 45 ||  // lantern
                    tileNumber == 46 ||  // plant
                    tileNumber == 54    // wall
                ) {
                    tileType = TileType.NOT_PASSABLE;
                }

                // These MUST remain walkable
                if (
                    tileNumber == 36 ||  // floor
                    tileNumber == 40 ||  // floor
                    tileNumber == 41 ||  // floor
                    tileNumber == 42 ||  // floor
                    tileNumber == 43 ||  // floor
                    tileNumber == 47 ||  // floor
                    tileNumber == 59     // floor
                ) {
                    tileType = TileType.PASSABLE;
                }

                tiles.add(
                    new MapTileBuilder(frame)
                    .withTileType(tileType)
                );
            }
        }
        return tiles;
    }
}