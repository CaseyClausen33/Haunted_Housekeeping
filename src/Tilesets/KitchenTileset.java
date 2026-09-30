package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;
import java.util.ArrayList;

public class KitchenTileset extends Tileset {

    // The engine's SpriteSheet adds a 1px gap between tiles, so a 127px tile
    // gives a pitch of 128px, which matches the 1024x1024 sheet (8x8 grid).
    private static final int TILE_SIZE = 127;

    public KitchenTileset() {
        super(ImageLoader.load("kitchentileset3.png"), TILE_SIZE, TILE_SIZE, 1);
    }

    // Walkable floor tile at sheet position (row, col)
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

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // ---------- FLOOR (walkable) ----------
        addFloor(mapTiles, 2, 2);                        // 0  floor1
        addFloor(mapTiles, 2, 5);                        // 1  floor2
        addFloor(mapTiles, 6, 6);                        // 2  floor3
        addFloor(mapTiles, 6, 7);                        // 3  floor4
        addFloor(mapTiles, 7, 6);                        // 4  floor5
        addFloor(mapTiles, 7, 7);                        // 5  floor6 (cobweb corner)

        // ---------- TOP ROW: APPLIANCES & COUNTERS (row 0 = top, row 1 = base) ----------
        // Bounds are (x, y, width, height) inside the tile
        addSolid(mapTiles, 0, 0, 5, 92, 122, 35);        // 6  stove with pots (top)
        addSolid(mapTiles, 1, 0, 5, 0, 122, 67);         // 7  stove cabinet (bottom)
        addSolid(mapTiles, 0, 1, 5, 5, 115, 123);        // 8  fridge (top)
        addSolid(mapTiles, 1, 1, 5, 0, 115, 67);         // 9  fridge (bottom)
        addSolid(mapTiles, 0, 2, 27, 35, 92, 93);        // 10 oven left (top)
        addSolid(mapTiles, 1, 2, 27, 0, 92, 20);         // 11 oven left (bottom)
        addSolid(mapTiles, 0, 3, 4, 35, 87, 93);         // 12 oven right (top)
        addSolid(mapTiles, 1, 3, 4, 0, 87, 20);          // 13 oven right (bottom)
        addSolid(mapTiles, 0, 4, 1, 92, 126, 36);        // 14 sink counter left (top)
        addSolid(mapTiles, 1, 4, 1, 0, 126, 72);         // 15 sink counter left (cabinets)
        addSolid(mapTiles, 0, 5, 0, 92, 127, 36);        // 16 sink counter right (pumpkin)
        addSolid(mapTiles, 1, 5, 0, 0, 127, 72);         // 17 sink counter right (cabinets)
        addSolid(mapTiles, 0, 6, 37, 92, 90, 36);        // 18 stove counter left (top)
        addSolid(mapTiles, 1, 6, 37, 0, 90, 72);         // 19 stove counter left (oven)
        addSolid(mapTiles, 0, 7, 0, 92, 124, 36);        // 20 stove counter right (top)
        addSolid(mapTiles, 1, 7, 0, 0, 124, 72);         // 21 stove counter right (drawers)

        // ---------- PLATES TABLE (upper left) ----------
        // Bounds run the full tile height because the stool table below it starts
        // in the bottom edge of this row
        addSolid(mapTiles, 2, 0, 38, 0, 89, 127);        // 22 plates table left
        addSolid(mapTiles, 2, 1, 0, 0, 92, 127);         // 23 plates table right

        // ---------- STEEL TABLE WITH JAR (center) ----------
        addSolid(mapTiles, 2, 3, 6, 44, 121, 84);        // 24 steel table top left (cleaver)
        addSolid(mapTiles, 2, 4, 0, 44, 120, 84);        // 25 steel table top right (jar)
        addSolid(mapTiles, 3, 3, 0, 0, 127, 127);        // 26 steel table bottom left / book island top
        addSolid(mapTiles, 3, 4, 0, 0, 127, 127);        // 27 steel table bottom right / jar island top

        // ---------- GHOST DOOR (right side) ----------
        addSolid(mapTiles, 2, 6, 52, 66, 75, 61);        // 28 ghost door top left
        addSolid(mapTiles, 2, 7, 0, 66, 16, 61);         // 29 ghost door top right
        addSolid(mapTiles, 3, 6, 52, 0, 75, 94);         // 30 ghost door bottom left
        addSolid(mapTiles, 3, 7, 0, 0, 16, 94);          // 31 ghost door bottom right

        // ---------- TABLES WITH STOOLS (left column) ----------
        addSolid(mapTiles, 3, 0, 38, 0, 89, 101);        // 32 stool table 1 left
        addSolid(mapTiles, 3, 1, 0, 0, 92, 101);         // 33 stool table 1 right
        addSolid(mapTiles, 4, 0, 38, 0, 89, 127);        // 34 stool table 2 left
        addSolid(mapTiles, 4, 1, 0, 0, 92, 127);         // 35 stool table 2 right
        addSolid(mapTiles, 5, 0, 38, 0, 89, 90);         // 36 stool table 3 left
        addSolid(mapTiles, 5, 1, 0, 0, 92, 90);          // 37 stool table 3 right

        // ---------- BOOK & CAULDRON ISLAND (center left) ----------
        // (3,3) is already used above as steel table bottom left
        addSolid(mapTiles, 3, 2, 39, 56, 88, 71);        // 38 book island top left (spellbook)
        addSolid(mapTiles, 4, 2, 39, 0, 88, 127);        // 39 book island bottom left
        addSolid(mapTiles, 4, 3, 0, 0, 94, 127);         // 40 book island bottom right / cauldron
        addSolid(mapTiles, 5, 2, 39, 0, 88, 72);         // 41 cauldron island cabinets left
        addSolid(mapTiles, 5, 3, 0, 0, 94, 72);          // 42 cauldron island cabinets right

        // ---------- JAR & SKELETON-STOOL ISLAND (center right) ----------
        // (3,4) is already used above as steel table bottom right
        addSolid(mapTiles, 3, 5, 0, 56, 115, 71);        // 43 jar island top right (jar)
        addSolid(mapTiles, 4, 4, 36, 0, 91, 127);        // 44 jar island bottom left / ghost orb
        addSolid(mapTiles, 4, 5, 0, 0, 115, 127);        // 45 jar island bottom right / ghost orb
        addSolid(mapTiles, 5, 4, 36, 0, 91, 95);         // 46 skeleton stools left
        addSolid(mapTiles, 5, 5, 0, 0, 115, 95);         // 47 skeleton stools right

        // ---------- SINK ISLAND (right side) ----------
        // The tiled backsplash above the counter stays walkable
        addSolid(mapTiles, 4, 6, 2, 63, 125, 64);        // 48 sink island top left
        addSolid(mapTiles, 4, 7, 0, 63, 124, 64);        // 49 sink island top right (candle)
        addSolid(mapTiles, 5, 6, 2, 0, 125, 75);         // 50 sink island cabinets left
        addSolid(mapTiles, 5, 7, 0, 0, 124, 75);         // 51 sink island cabinets right

        // ---------- WITCH'S CORNER (bottom left) ----------
        // The hood and wall shelf are on the wall, so only the counter is solid
        addSolid(mapTiles, 6, 0, 5, 97, 122, 30);        // 52 witch / hood top left (stove top)
        addSolid(mapTiles, 6, 1, 0, 107, 127, 20);       // 53 kettle shelf top right (counter top)
        addSolid(mapTiles, 7, 0, 5, 0, 122, 89);         // 54 bubbling stove
        addSolid(mapTiles, 7, 1, 0, 0, 127, 89);         // 55 counter with sink

        // ---------- THREE-STOOL TABLE (bottom center) ----------
        addSolid(mapTiles, 7, 2, 37, 0, 90, 99);         // 56 three-stool table left
        addSolid(mapTiles, 7, 3, 0, 0, 94, 99);          // 57 three-stool table right

        // ---------- DOUBLE DOOR & SMALL FURNITURE ----------
        addSolid(mapTiles, 6, 5, 0, 12, 88, 115);        // 58 double door (top)
        addSolid(mapTiles, 7, 5, 0, 0, 88, 54);          // 59 double door (bottom)
        addSolid(mapTiles, 6, 4, 18, 37, 78, 90);        // 60 wall shelf with jars
        addSolid(mapTiles, 7, 4, 18, 0, 78, 64);         // 61 small dresser

        return mapTiles;
    }
}