package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;
import java.util.ArrayList;

public class OutsideTileset extends Tileset {

    // Spooky hotel exterior sheet: 16x16 tiles, 13 columns x 7 rows, 1px gap between tiles
    // (221x119 PNG). Ids 0-80 are used.
    private static final String SHEET_FILE = "outsidetileset.png";
    private static final int TILE_SIZE = 16;
    private static final int TILE_SCALE = 4;

    public OutsideTileset() {
        super(ImageLoader.load(SHEET_FILE), TILE_SIZE, TILE_SIZE, TILE_SCALE);
    }

    // Walkable tile (ground, path, porch, decals)
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

        addFloor(mapTiles, 0, 0);                         // 0 grass A
        addFloor(mapTiles, 0, 1);                         // 1 grass B
        addFloor(mapTiles, 0, 2);                         // 2 dead grass
        addFloor(mapTiles, 0, 3);                         // 3 gravel
        addFloor(mapTiles, 0, 4);                         // 4 cobblestone path
        addFloor(mapTiles, 0, 5);                         // 5 road
        addFloor(mapTiles, 0, 6);                         // 6 road with center line
        addFloor(mapTiles, 0, 7);                         // 7 porch floor
        addFloor(mapTiles, 0, 8);                         // 8 stone steps
        addFloor(mapTiles, 0, 9);                         // 9 bare dirt
        addSolid(mapTiles, 0, 10, 0, 0, 16, 16);          // 10 roof
        addSolid(mapTiles, 0, 11, 0, 0, 16, 16);          // 11 roof (missing shingles)
        addSolid(mapTiles, 0, 12, 0, 0, 16, 16);          // 12 roof edge left
        addSolid(mapTiles, 1, 0, 0, 0, 16, 16);           // 13 roof edge right
        addSolid(mapTiles, 1, 1, 0, 0, 16, 16);           // 14 eave
        addSolid(mapTiles, 1, 2, 0, 0, 16, 16);           // 15 chimney
        addSolid(mapTiles, 1, 3, 0, 0, 16, 16);           // 16 siding
        addSolid(mapTiles, 1, 4, 0, 0, 16, 16);           // 17 window, dark
        addSolid(mapTiles, 1, 5, 0, 0, 16, 16);           // 18 window, lit with a figure
        addSolid(mapTiles, 1, 6, 0, 0, 16, 16);           // 19 window, boarded up
        addSolid(mapTiles, 1, 7, 0, 0, 16, 16);           // 20 front door (swap for a trigger tile to enter)
        addSolid(mapTiles, 1, 8, 0, 0, 16, 16);           // 21 front door ajar
        addSolid(mapTiles, 1, 9, 0, 0, 16, 16);           // 22 sign board, blank
        addSolid(mapTiles, 1, 10, 0, 0, 16, 16);          // 23 sign letter H
        addSolid(mapTiles, 1, 11, 0, 0, 16, 16);          // 24 sign letter O
        addSolid(mapTiles, 1, 12, 0, 0, 16, 16);          // 25 sign letter T
        addSolid(mapTiles, 2, 0, 0, 0, 16, 16);           // 26 sign letter E (unlit)
        addSolid(mapTiles, 2, 1, 0, 0, 16, 16);           // 27 sign letter L
        addSolid(mapTiles, 2, 2, 0, 0, 16, 16);           // 28 pillar left
        addSolid(mapTiles, 2, 3, 0, 0, 16, 16);           // 29 pillar right
        addSolid(mapTiles, 2, 4, 0, 0, 16, 16);           // 30 dead vines on siding
        addSolid(mapTiles, 2, 5, 0, 0, 16, 16);           // 31 plaque
        addSolid(mapTiles, 2, 6, 0, 4, 16, 10);           // 32 iron fence
        addSolid(mapTiles, 2, 7, 5, 0, 6, 16);            // 33 fence post
        addSolid(mapTiles, 2, 8, 0, 2, 6, 14);            // 34 gate left
        addSolid(mapTiles, 2, 9, 10, 2, 6, 14);           // 35 gate right
        addSolid(mapTiles, 2, 10, 2, 4, 12, 12);          // 36 stone pillar with lantern
        addSolid(mapTiles, 2, 11, 4, 6, 8, 10);           // 37 dead tree
        addSolid(mapTiles, 2, 12, 1, 4, 14, 12);          // 38 dead bush
        addSolid(mapTiles, 3, 0, 6, 3, 4, 13);            // 39 lamp post
        addSolid(mapTiles, 3, 1, 4, 4, 8, 12);            // 40 mailbox
        addSolid(mapTiles, 3, 2, 2, 4, 12, 11);           // 41 bench
        addSolid(mapTiles, 3, 3, 0, 1, 16, 15);           // 42 old car left
        addSolid(mapTiles, 3, 4, 0, 1, 16, 15);           // 43 old car right
        addSolid(mapTiles, 3, 5, 3, 8, 10, 7);            // 44 suitcase
        addSolid(mapTiles, 3, 6, 3, 2, 10, 14);           // 45 angel statue
        addFloor(mapTiles, 3, 7);                         // 46 fog
        addFloor(mapTiles, 3, 8);                         // 47 fallen leaves
        addFloor(mapTiles, 3, 9);                         // 48 crow
        addFloor(mapTiles, 3, 10);                        // 49 puddle
        addSolid(mapTiles, 3, 11, 3, 3, 10, 13);          // 50 gravestone
        addSolid(mapTiles, 3, 12, 3, 2, 10, 14);          // 51 sign post
        addSolid(mapTiles, 4, 0, 0, 0, 16, 16);           // 52 slate mansard roof
        addSolid(mapTiles, 4, 1, 0, 0, 16, 16);           // 53 dormer window, dark
        addSolid(mapTiles, 4, 2, 0, 0, 16, 16);           // 54 dormer window, lit
        addSolid(mapTiles, 4, 3, 0, 0, 16, 16);           // 55 slate roof edge left
        addSolid(mapTiles, 4, 4, 0, 0, 16, 16);           // 56 slate roof edge right
        addSolid(mapTiles, 4, 5, 0, 0, 16, 16);           // 57 tower cap left slope
        addSolid(mapTiles, 4, 6, 0, 0, 16, 16);           // 58 tower cap center
        addSolid(mapTiles, 4, 7, 0, 0, 16, 16);           // 59 tower cap right slope
        addSolid(mapTiles, 4, 8, 0, 0, 16, 16);           // 60 spire tip
        addSolid(mapTiles, 4, 9, 0, 0, 16, 16);           // 61 clock tower face
        addSolid(mapTiles, 4, 10, 0, 0, 16, 16);          // 62 tower belfry
        addSolid(mapTiles, 4, 11, 0, 0, 16, 16);          // 63 tower wall
        addSolid(mapTiles, 4, 12, 0, 0, 16, 16);          // 64 cornice
        addSolid(mapTiles, 5, 0, 0, 0, 16, 16);           // 65 arched window, dark
        addSolid(mapTiles, 5, 1, 0, 0, 16, 16);           // 66 arched window, lit with a figure
        addSolid(mapTiles, 5, 2, 0, 0, 16, 16);           // 67 balcony doors
        addSolid(mapTiles, 5, 3, 0, 0, 16, 16);           // 68 balcony railing
        addSolid(mapTiles, 5, 4, 0, 0, 16, 16);           // 69 pilaster capital
        addSolid(mapTiles, 5, 5, 0, 0, 16, 16);           // 70 pilaster shaft
        addSolid(mapTiles, 5, 6, 0, 0, 16, 16);           // 71 pilaster base (on the porch)
        addSolid(mapTiles, 5, 7, 0, 0, 16, 16);           // 72 stone foundation
        addSolid(mapTiles, 5, 8, 0, 0, 16, 16);           // 73 quoin wall
        addSolid(mapTiles, 5, 9, 1, 1, 14, 14);           // 74 fountain top left
        addSolid(mapTiles, 5, 10, 1, 1, 14, 14);          // 75 fountain top right
        addSolid(mapTiles, 5, 11, 1, 1, 14, 14);          // 76 fountain bottom left
        addSolid(mapTiles, 5, 12, 1, 1, 14, 14);          // 77 fountain bottom right
        addSolid(mapTiles, 6, 0, 0, 4, 16, 12);           // 78 hedge
        addSolid(mapTiles, 6, 1, 4, 2, 8, 14);            // 79 topiary
        addSolid(mapTiles, 6, 2, 4, 5, 8, 11);            // 80 urn on pedestal

        return mapTiles;
    }
}