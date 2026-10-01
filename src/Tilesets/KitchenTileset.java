package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;
import java.util.ArrayList;

public class KitchenTileset extends Tileset {

    // Spooky kitchen sheet: 16x16 tiles, 13 columns x 2 rows.
    // The engine's SpriteSheet adds a 1px gap between tiles, so a 16px tile gives a
    // pitch of 17px. The sheet PNG has 1px of transparent space after every tile
    // (including the last column/row), so it is 221x34 (13*17 x 2*17).
    private static final String SHEET_FILE = "spookykitchentileset2.png";
    private static final int TILE_SIZE = 16;

    // Draw scale. 16px * 4 = 64px tiles on screen. Change to taste.
    // Bounds below are written in sheet pixels (0-15) and get scaled with the tile.
    private static final int TILE_SCALE = 4;

    public KitchenTileset() {
        super(ImageLoader.load(SHEET_FILE), TILE_SIZE, TILE_SIZE, TILE_SCALE);
    }

    // Walkable tile at sheet position (row, col). Also used for flat decals
    // (slime, blood, cobweb) and hanging decor that the player can walk under.
    private void addFloor(ArrayList<MapTileBuilder> tiles, int row, int col) {
        Frame frame = new FrameBuilder(getSubImage(row, col))
                .withScale(tileScale)
                .withBounds(0, 0, TILE_SIZE, TILE_SIZE)
                .build();
        tiles.add(new MapTileBuilder(frame).withTileType(TileType.PASSABLE));
    }

    // Solid tile at sheet position (row, col). Only the rectangle (bx, by, bw, bh),
    // measured in pixels from the tile's top-left corner, blocks the player.
    // The rest of the tile can be walked through.
    private void addSolid(ArrayList<MapTileBuilder> tiles, int row, int col,
                          int bx, int by, int bw, int bh) {
        Frame frame = new FrameBuilder(getSubImage(row, col))
                .withScale(tileScale)
                .withBounds(bx, by, bw, bh)
                .build();
        tiles.add(new MapTileBuilder(frame).withTileType(TileType.NOT_PASSABLE));
    }

    // Tile ids follow the sheet order, left to right, top to bottom (0-25).
    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // ---------- ROW 0 ----------
        // Floors and rug (walkable)
        addFloor(mapTiles, 0, 0);                        // 0  floor A
        addFloor(mapTiles, 0, 1);                        // 1  floor B
        addFloor(mapTiles, 0, 2);                        // 2  floor cracked
        addFloor(mapTiles, 0, 3);                        // 3  rug

        // Walls (fully solid)
        addSolid(mapTiles, 0, 4, 0, 0, 16, 16);          // 4  brick wall
        addSolid(mapTiles, 0, 5, 0, 0, 16, 16);          // 5  wall + baseboard
        addSolid(mapTiles, 0, 6, 0, 0, 16, 16);          // 6  cobweb wall
        addSolid(mapTiles, 0, 7, 0, 0, 16, 16);          // 7  moon window
        addSolid(mapTiles, 0, 8, 0, 0, 16, 16);          // 8  pot rail
        addSolid(mapTiles, 0, 9, 0, 0, 16, 16);          // 9  door (swap for a trigger tile if it should open)

        // Counters and appliances (3/4 view: the top 3px is empty space behind the counter)
        addSolid(mapTiles, 0, 10, 0, 3, 16, 13);         // 10 counter
        addSolid(mapTiles, 0, 11, 0, 3, 16, 13);         // 11 counter + potion
        addSolid(mapTiles, 0, 12, 0, 3, 16, 13);         // 12 stove

        // ---------- ROW 1 ----------
        addSolid(mapTiles, 1, 0, 0, 3, 16, 13);          // 13 sink
        addSolid(mapTiles, 1, 1, 2, 0, 12, 16);          // 14 haunted fridge
        addSolid(mapTiles, 1, 2, 1, 4, 14, 12);          // 15 cauldron
        addSolid(mapTiles, 1, 3, 1, 3, 14, 12);          // 16 table
        addSolid(mapTiles, 1, 4, 4, 8, 8, 8);            // 17 candle
        addSolid(mapTiles, 1, 5, 4, 4, 8, 9);            // 18 skull

        // Flat decals (walkable)
        addFloor(mapTiles, 1, 6);                        // 19 slime puddle
        addFloor(mapTiles, 1, 7);                        // 20 blood splatter

        // Props
        addSolid(mapTiles, 1, 8, 2, 2, 12, 13);          // 21 barrel
        addSolid(mapTiles, 1, 9, 1, 5, 14, 9);           // 22 jack-o-lantern
        addFloor(mapTiles, 1, 10);                       // 23 hanging spider (walk under it)
        addSolid(mapTiles, 1, 11, 3, 3, 10, 12);         // 24 cleaver block
        addFloor(mapTiles, 1, 12);                       // 25 cobweb corner

        return mapTiles;
    }
}