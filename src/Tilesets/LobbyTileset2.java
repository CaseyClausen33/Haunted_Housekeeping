package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;
import java.util.ArrayList;

public class LobbyTileset2 extends Tileset {

    // Spooky hotel lobby sheet v2: 16x16 tiles, 13 columns x 4 rows, 1px gap between tiles
    // (221x68 PNG). Ids 0-25 are identical to LobbyTileset; ids 26-43 add doors, stairs and stair barricades.
    private static final String SHEET_FILE = "lobbytileset2.png";
    private static final int TILE_SIZE = 16;
    private static final int TILE_SCALE = 4;

    public LobbyTileset2() {
        super(ImageLoader.load(SHEET_FILE), TILE_SIZE, TILE_SIZE, TILE_SCALE);
    }

    // Walkable tile (floors, carpet, decals, hanging decor)
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

    // Tile ids follow the sheet order, left to right, top to bottom (0-25).
    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // ---------- ROW 0 ----------
        addFloor(mapTiles, 0, 0);                        // 0  wood planks A
        addFloor(mapTiles, 0, 1);                        // 1  wood planks B
        addFloor(mapTiles, 0, 2);                        // 2  cracked wood planks
        addFloor(mapTiles, 0, 3);                        // 3  red carpet

        addSolid(mapTiles, 0, 4, 0, 0, 16, 16);          // 4  wallpaper
        addSolid(mapTiles, 0, 5, 0, 0, 16, 16);          // 5  wallpaper + wood paneling
        addSolid(mapTiles, 0, 6, 0, 0, 16, 16);          // 6  cobweb wall
        addSolid(mapTiles, 0, 7, 0, 0, 16, 16);          // 7  watching portrait
        addSolid(mapTiles, 0, 8, 0, 0, 16, 16);          // 8  candle sconce
        addSolid(mapTiles, 0, 9, 0, 0, 16, 16);          // 9  door (swap for a trigger tile if it opens)

        addSolid(mapTiles, 0, 10, 0, 3, 16, 13);         // 10 reception desk left
        addSolid(mapTiles, 0, 11, 0, 3, 16, 13);         // 11 reception desk middle (ledger + bell)
        addSolid(mapTiles, 0, 12, 0, 3, 16, 13);         // 12 reception desk right (phone)

        // ---------- ROW 1 ----------
        addSolid(mapTiles, 1, 0, 3, 0, 10, 16);          // 13 grandfather clock
        addSolid(mapTiles, 1, 1, 1, 0, 14, 16);          // 14 elevator (swap for a trigger tile if it works)
        addSolid(mapTiles, 1, 2, 0, 3, 16, 13);          // 15 velvet sofa
        addSolid(mapTiles, 1, 3, 2, 2, 12, 13);          // 16 armchair
        addSolid(mapTiles, 1, 4, 2, 6, 12, 10);          // 17 candelabra table
        addSolid(mapTiles, 1, 5, 2, 3, 12, 12);          // 18 old suitcases
        addSolid(mapTiles, 1, 6, 4, 3, 8, 13);           // 19 dead plant
        addSolid(mapTiles, 1, 7, 2, 7, 12, 5);           // 20 broken chandelier

        addFloor(mapTiles, 1, 8);                        // 21 ectoplasm puddle
        addFloor(mapTiles, 1, 9);                        // 22 mist wisps
        addFloor(mapTiles, 1, 10);                       // 23 hanging bulb (walk under it)
        addFloor(mapTiles, 1, 11);                       // 24 cobweb corner

        addSolid(mapTiles, 1, 12, 4, 2, 8, 14);          // 25 floor lamp

        // ---------- NEW: DOORS (row 2 / row 3) ----------
        addSolid(mapTiles, 2, 0, 0, 0, 16, 16);          // 26 room door "101" (swap for a trigger tile to enter)
        addSolid(mapTiles, 2, 1, 0, 0, 16, 16);          // 27 door ajar, something inside
        addSolid(mapTiles, 2, 2, 0, 0, 16, 16);          // 28 double door left
        addSolid(mapTiles, 2, 3, 0, 0, 16, 16);          // 29 double door right
        addSolid(mapTiles, 2, 4, 0, 0, 16, 16);          // 30 boarded-up door (stays locked)
        addSolid(mapTiles, 2, 5, 0, 0, 16, 16);          // 31 staff only door
        addFloor(mapTiles, 2, 6);                        // 32 arch passage (walk through, put a trigger on it)

        // ---------- NEW: STAIRS ----------
        addFloor(mapTiles, 2, 7);                        // 33 stairs middle (walkable)
        addSolid(mapTiles, 2, 8, 0, 0, 3, 16);           // 34 stairs left, banister blocks (steps walkable)
        addSolid(mapTiles, 2, 9, 13, 0, 3, 16);          // 35 stairs right, banister blocks (steps walkable)
        addSolid(mapTiles, 2, 10, 5, 3, 6, 13);          // 36 newel post
        addSolid(mapTiles, 2, 11, 0, 4, 16, 11);         // 37 balcony railing
        addFloor(mapTiles, 2, 12);                       // 38 cellar stairs going down (put a trigger on it)
        addFloor(mapTiles, 3, 0);                        // 39 stairs top, fading into darkness (put a trigger on it)

        // ---------- NEW: FRONT ENTRANCE ----------
        addSolid(mapTiles, 3, 1, 0, 0, 16, 16);          // 40 front door left (moonlit glass)
        addSolid(mapTiles, 3, 2, 0, 0, 16, 16);          // 41 front door right

        // ---------- NEW: BLOCKED STAIRS ----------
        addSolid(mapTiles, 3, 3, 0, 0, 16, 16);          // 42 stairs boarded up (replace with stairs middle when unlocked)
        addSolid(mapTiles, 3, 4, 0, 0, 16, 16);          // 43 stairs blocked by overturned armchair and debris

        return mapTiles;
    }
}