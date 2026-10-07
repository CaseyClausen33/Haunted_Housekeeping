package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;
import java.util.ArrayList;

public class CleanBathroomTileset2 extends Tileset {

    // Spooky hotel bathroom sheet: 16x16 tiles, 13 columns x 4 rows, 1px gap between tiles
    // (221x68 PNG). Ids 0-47 are used.
    private static final String SHEET_FILE = "cleanbathroomtileset2.png";
    private static final int TILE_SIZE = 16;
    private static final int TILE_SCALE = 4;

    public CleanBathroomTileset2() {
        super(ImageLoader.load(SHEET_FILE), TILE_SIZE, TILE_SIZE, TILE_SCALE);
        System.out.println(ImageLoader.load(SHEET_FILE).getWidth() + "x" + ImageLoader.load(SHEET_FILE).getHeight());
    }

    // Walkable tile (floors, mat, decals, hanging decor)
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

        // ---------- ROW 0 ----------
        addFloor(mapTiles, 0, 0);                        // 0  floor tile A
        addFloor(mapTiles, 0, 1);                        // 1  floor tile B
        addFloor(mapTiles, 0, 2);                        // 2  cracked floor tile
        addFloor(mapTiles, 0, 3);                        // 3  bath mat

        addSolid(mapTiles, 0, 4, 0, 0, 16, 16);          // 4  peeling paint wall
        addSolid(mapTiles, 0, 5, 0, 0, 16, 16);          // 5  tiled wall
        addSolid(mapTiles, 0, 6, 0, 0, 16, 16);          // 6  tiled wall, mold + cobweb
        addSolid(mapTiles, 0, 7, 0, 0, 16, 16);          // 7  frosted window
        addSolid(mapTiles, 0, 8, 0, 0, 16, 16);          // 8  cracked mirror (ghost)
        addSolid(mapTiles, 0, 9, 0, 0, 16, 16);          // 9  medicine cabinet
        addSolid(mapTiles, 0, 10, 0, 0, 16, 16);         // 10 towel rail
        addSolid(mapTiles, 0, 11, 0, 0, 16, 16);         // 11 door (swap for a trigger tile to leave)
        addSolid(mapTiles, 0, 12, 0, 0, 16, 16);         // 12 door ajar (closet)

        // ---------- ROW 1 ----------
        addSolid(mapTiles, 1, 0, 1, 3, 15, 13);          // 13 bathtub left (faucet)
        addSolid(mapTiles, 1, 1, 0, 3, 15, 13);          // 14 bathtub right (pale hand)
        addSolid(mapTiles, 1, 2, 3, 1, 10, 14);          // 15 toilet
        addSolid(mapTiles, 1, 3, 3, 4, 10, 12);          // 16 pedestal sink
        addSolid(mapTiles, 1, 4, 0, 2, 16, 14);          // 17 shower left (silhouette behind curtain)
        addSolid(mapTiles, 1, 5, 0, 2, 16, 14);          // 18 shower right (showerhead)
        addSolid(mapTiles, 1, 6, 3, 8, 10, 7);           // 19 bath scale
        addSolid(mapTiles, 1, 7, 4, 6, 8, 10);           // 20 stool with candle

        addFloor(mapTiles, 1, 8);                        // 21 puddle
        addFloor(mapTiles, 1, 9);                        // 22 floor drain
        addFloor(mapTiles, 1, 10);                       // 23 wet footprints
        addFloor(mapTiles, 1, 11);                       // 24 hanging bulb (walk under it)
        addFloor(mapTiles, 1, 12);                       // 25 cobweb corner

        // ---------- ROW 2 ----------
        addFloor(mapTiles, 2, 0);                        // 26 mold patch
        addSolid(mapTiles, 2, 1, 0, 0, 16, 16);          // 27 wall lamp
        addSolid(mapTiles, 2, 2, 0, 0, 16, 16);          // 28 fogged tile with handprint
        addSolid(mapTiles, 2, 3, 0, 0, 16, 16);          // 29 rusty pipe

        // ---------- MORE DECOR (row 2 / row 3) ----------
        addSolid(mapTiles, 2, 4, 2, 2, 12, 13);          // 30 overflowing hamper
        addSolid(mapTiles, 2, 5, 3, 2, 10, 13);          // 31 trash can
        addSolid(mapTiles, 2, 6, 3, 6, 10, 10);          // 32 mop bucket
        addSolid(mapTiles, 2, 7, 4, 3, 8, 12);           // 33 wet floor sign
        addSolid(mapTiles, 2, 8, 2, 0, 12, 16);          // 34 linen cabinet
        addSolid(mapTiles, 2, 9, 1, 4, 14, 12);          // 35 radiator
        addSolid(mapTiles, 2, 10, 1, 4, 14, 12);         // 36 bench with towels
        addSolid(mapTiles, 2, 11, 4, 3, 8, 13);          // 37 moldy plant
        addFloor(mapTiles, 2, 12);                       // 38 rubber duck in puddle
        addFloor(mapTiles, 3, 0);                        // 39 unrolled toilet paper
        addFloor(mapTiles, 3, 1);                        // 40 pile of damp towels
        addFloor(mapTiles, 3, 2);                        // 41 broken tile rubble
        addSolid(mapTiles, 3, 3, 3, 5, 10, 8);           // 42 hole in the floor
        addFloor(mapTiles, 3, 4);                        // 43 spilled bottles and pills
        addSolid(mapTiles, 3, 5, 0, 0, 16, 16);          // 44 crooked picture
        addSolid(mapTiles, 3, 6, 0, 0, 16, 16);          // 45 bathrobe on hook
        addSolid(mapTiles, 3, 7, 0, 0, 16, 16);          // 46 wall shelf
        addSolid(mapTiles, 3, 8, 0, 0, 16, 16);          // 47 vent with eyes

        return mapTiles;
    }
}