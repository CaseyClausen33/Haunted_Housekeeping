package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;
import java.util.ArrayList;

public class BedroomTileset extends Tileset {

    // Spooky hotel bedroom sheet: 16x16 tiles, 13 columns x 4 rows, 1px gap between tiles
    // (221x68 PNG). Ids 0-39 are used.
    private static final String SHEET_FILE = "bedroomtileset.png";
    private static final int TILE_SIZE = 16;
    private static final int TILE_SCALE = 4;

    public BedroomTileset() {
        super(ImageLoader.load(SHEET_FILE), TILE_SIZE, TILE_SIZE, TILE_SCALE);
    }

    // Walkable tile (carpet, rug, decals, hanging decor)
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

        addFloor(mapTiles, 0, 0);                         // 0 carpet A
        addFloor(mapTiles, 0, 1);                         // 1 carpet B
        addFloor(mapTiles, 0, 2);                         // 2 stained carpet
        addFloor(mapTiles, 0, 3);                         // 3 round rug
        addSolid(mapTiles, 0, 4, 0, 0, 16, 16);           // 4 striped wallpaper
        addSolid(mapTiles, 0, 5, 0, 0, 16, 16);           // 5 wallpaper + paneling
        addSolid(mapTiles, 0, 6, 0, 0, 16, 16);           // 6 peeling wallpaper + cobweb
        addSolid(mapTiles, 0, 7, 0, 0, 16, 16);           // 7 curtained window
        addSolid(mapTiles, 0, 8, 0, 0, 16, 16);           // 8 window with figure outside
        addSolid(mapTiles, 0, 9, 0, 0, 16, 16);           // 9 room door (swap for a trigger tile to leave)
        addSolid(mapTiles, 0, 10, 0, 0, 16, 16);          // 10 closet door ajar
        addSolid(mapTiles, 0, 11, 0, 0, 16, 16);          // 11 painting
        addSolid(mapTiles, 0, 12, 0, 0, 16, 16);          // 12 wall sconce
        addSolid(mapTiles, 1, 0, 1, 0, 15, 16);           // 13 bed head left
        addSolid(mapTiles, 1, 1, 0, 0, 15, 16);           // 14 bed head right
        addSolid(mapTiles, 1, 2, 1, 0, 15, 16);           // 15 bed foot left
        addSolid(mapTiles, 1, 3, 0, 0, 15, 16);           // 16 bed foot right (pale hand)
        addSolid(mapTiles, 1, 4, 2, 7, 12, 9);            // 17 nightstand with lamp
        addSolid(mapTiles, 1, 5, 2, 7, 12, 9);            // 18 nightstand with candle
        addSolid(mapTiles, 1, 6, 1, 3, 14, 13);           // 19 dresser
        addSolid(mapTiles, 1, 7, 2, 0, 12, 16);           // 20 vanity with mirror
        addSolid(mapTiles, 1, 8, 1, 0, 14, 16);           // 21 wardrobe
        addSolid(mapTiles, 1, 9, 1, 3, 14, 13);           // 22 armchair
        addSolid(mapTiles, 1, 10, 2, 5, 12, 10);          // 23 trunk
        addSolid(mapTiles, 1, 11, 2, 3, 12, 11);          // 24 rocking horse
        addSolid(mapTiles, 1, 12, 4, 2, 8, 14);           // 25 floor lamp
        addSolid(mapTiles, 2, 0, 1, 2, 14, 13);           // 26 writing desk
        addSolid(mapTiles, 2, 1, 3, 1, 10, 15);           // 27 coat rack
        addFloor(mapTiles, 2, 2);                         // 28 teddy bear
        addFloor(mapTiles, 2, 3);                         // 29 slippers
        addFloor(mapTiles, 2, 4);                         // 30 pile of clothes
        addFloor(mapTiles, 2, 5);                         // 31 claw scratches
        addFloor(mapTiles, 2, 6);                         // 32 mist wisps
        addFloor(mapTiles, 2, 7);                         // 33 hanging bulb (walk under it)
        addFloor(mapTiles, 2, 8);                         // 34 cobweb corner
        addSolid(mapTiles, 2, 9, 3, 1, 10, 15);           // 35 music box table
        addSolid(mapTiles, 2, 10, 2, 5, 12, 9);           // 36 cradle
        addSolid(mapTiles, 2, 11, 2, 3, 12, 11);          // 37 open suitcase
        addSolid(mapTiles, 2, 12, 4, 3, 8, 13);           // 38 doll on a chair
        addSolid(mapTiles, 3, 0, 2, 0, 12, 16);           // 39 old TV with static

        return mapTiles;
    }
}