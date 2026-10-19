package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;
import java.util.ArrayList;

public class LibraryTileset extends Tileset {

    // Spooky hotel library sheet: 16x16 tiles, 13 columns x 4 rows, 1px gap between tiles
    // (221x68 PNG). Ids 0-43 are used.
    private static final String SHEET_FILE = "librarytileset.png";
    private static final int TILE_SIZE = 16;
    private static final int TILE_SCALE = 4;

    public LibraryTileset() {
        super(ImageLoader.load(SHEET_FILE), TILE_SIZE, TILE_SIZE, TILE_SCALE);
    }

    // Walkable tile (floors, rug, decals, hanging decor)
    private void addFloor(ArrayList<MapTileBuilder> tiles, int row, int col) {
        Frame frame = new FrameBuilder(getSubImage(row, col))
                .withScale(tileScale)
                .withBounds(0, 0, TILE_SIZE, TILE_SIZE)
                .build();
        tiles.add(new MapTileBuilder(frame).withTileType(TileType.PASSABLE));
    }

    // Solid tile. Only (bx, by, bw, bh), in pixels from the tile's top-left, blocks the player.
    private void addSolid(ArrayList<MapTileBuilder> tiles, int row, int col,
                          int bx, int by, int bw, int bh) {
        Frame frame = new FrameBuilder(getSubImage(row, col))
                .withScale(tileScale)
                .withBounds(bx, by, bw, bh)
                .build();
        tiles.add(new MapTileBuilder(frame).withTileType(TileType.NOT_PASSABLE));
    }

    // Tile ids follow the sheet order, left to right, top to bottom.
    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        addFloor(mapTiles, 0, 0);                         // 0 parquet floor A
        addFloor(mapTiles, 0, 1);                         // 1 parquet floor B
        addFloor(mapTiles, 0, 2);                         // 2 cracked parquet
        addFloor(mapTiles, 0, 3);                         // 3 green rug
        addSolid(mapTiles, 0, 4, 0, 0, 16, 16);           // 4 wallpaper
        addSolid(mapTiles, 0, 5, 0, 0, 16, 16);           // 5 wallpaper + paneling
        addSolid(mapTiles, 0, 6, 0, 0, 16, 16);           // 6 cobweb wall
        addSolid(mapTiles, 0, 7, 0, 0, 16, 16);           // 7 bookshelf top
        addSolid(mapTiles, 0, 8, 0, 0, 16, 16);           // 8 bookshelf top, something watching
        addSolid(mapTiles, 0, 9, 0, 0, 16, 16);           // 9 bookshelf bottom
        addSolid(mapTiles, 0, 10, 0, 0, 16, 16);          // 10 bookshelf bottom, open cabinet with skull
        addSolid(mapTiles, 0, 11, 0, 0, 16, 16);          // 11 bookshelf with ladder rail
        addSolid(mapTiles, 0, 12, 0, 0, 16, 16);          // 12 library door (swap for a trigger tile)
        addSolid(mapTiles, 1, 0, 0, 3, 16, 13);           // 13 reading desk left
        addSolid(mapTiles, 1, 1, 0, 3, 16, 13);           // 14 reading desk right
        addSolid(mapTiles, 1, 2, 1, 3, 14, 13);           // 15 leather armchair
        addSolid(mapTiles, 1, 3, 2, 6, 12, 10);           // 16 table with glowing book
        addSolid(mapTiles, 1, 4, 3, 2, 10, 14);           // 17 antique globe
        addSolid(mapTiles, 1, 5, 1, 2, 14, 14);           // 18 card catalog (one drawer glows)
        addSolid(mapTiles, 1, 6, 3, 1, 10, 15);           // 19 rolling ladder
        addSolid(mapTiles, 1, 7, 3, 4, 10, 11);           // 20 stack of books
        addFloor(mapTiles, 1, 8);                         // 21 scattered books
        addFloor(mapTiles, 1, 9);                         // 22 floating book
        addFloor(mapTiles, 1, 10);                        // 23 hanging oil lamp (walk under it)
        addFloor(mapTiles, 1, 11);                        // 24 cobweb corner
        addFloor(mapTiles, 1, 12);                        // 25 mist wisps
        addSolid(mapTiles, 2, 0, 1, 3, 14, 13);           // 26 skeleton reading in armchair
        addSolid(mapTiles, 2, 1, 4, 2, 8, 14);            // 27 bust with raven
        addSolid(mapTiles, 2, 2, 0, 0, 16, 16);           // 28 fireplace left
        addSolid(mapTiles, 2, 3, 0, 0, 16, 16);           // 29 fireplace right
        addSolid(mapTiles, 2, 4, 0, 0, 16, 16);           // 30 portrait with glowing eyes
        addSolid(mapTiles, 2, 5, 0, 0, 16, 16);           // 31 wall clock
        addSolid(mapTiles, 2, 6, 0, 0, 16, 16);           // 32 arched window
        addSolid(mapTiles, 2, 7, 0, 0, 16, 16);           // 33 wall sconce
        addFloor(mapTiles, 2, 8);                         // 34 glowing sigil
        addFloor(mapTiles, 2, 9);                         // 35 ink spill
        addSolid(mapTiles, 2, 10, 4, 2, 8, 14);           // 36 floor lamp
        addSolid(mapTiles, 2, 11, 1, 4, 14, 11);          // 37 map table
        addSolid(mapTiles, 2, 12, 2, 5, 12, 10);          // 38 locked chest
        addSolid(mapTiles, 3, 0, 2, 1, 12, 15);           // 39 book cart
        addSolid(mapTiles, 3, 1, 2, 3, 12, 11);           // 40 rocking chair
        addSolid(mapTiles, 3, 2, 3, 4, 10, 12);           // 41 side table with teacup
        addSolid(mapTiles, 3, 3, 0, 0, 16, 16);           // 42 quiet please plaque
        addSolid(mapTiles, 3, 4, 4, 3, 8, 13);            // 43 dead fern

        return mapTiles;
    }
}