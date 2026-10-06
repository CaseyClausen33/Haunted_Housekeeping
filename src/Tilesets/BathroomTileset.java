package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;
import java.util.ArrayList;

public class BathroomTileset extends Tileset {

    private static final int TILE_SIZE = 127;

    public BathroomTileset() {
        super(
            ImageLoader.load("bathroomtileset.png"),
            TILE_SIZE,
            TILE_SIZE,
            1
        );
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {

        ArrayList<MapTileBuilder> tiles = new ArrayList<>();

        // 64 tiles: 8 rows × 8 columns
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                Frame frame = new FrameBuilder(getSubImage(row, col))
                    .withScale(tileScale)
                    .withBounds(0, 0, TILE_SIZE, TILE_SIZE)
                    .build();

                tiles.add(
                    new MapTileBuilder(frame)
                        .withTileType(TileType.PASSABLE)
                );
            }
        }

        return tiles;
    }
}