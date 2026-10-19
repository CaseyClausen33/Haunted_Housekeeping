package Maps;

import java.util.ArrayList;

import Engine.ScreenManager;
import Level.Map;
import Level.NPC;
import Level.Player;
import Level.Script;
import NPCs.Ghost;
import NPCs.Taxi;
import Scripts.ChangeMapScript;
import Scripts.IntroCutsceneScript;
import Tilesets.OutsideTileset;

public class OutsideMap extends Map {

    private static final int TILE_PX = 64;          // 16px tiles drawn at scale 4
    private static final int MAP_TILES_W = 29;
    private static final int MAP_TILES_H = 22;

    // The taxi starts on the road six tiles left of the gate
    private static final int TAXI_TILE_X = 6;
    private static final int ROAD_TILE_Y = 20;

    // true once the cutscene has played so it never plays again. It is static because a new OutsideMap
    // object is created every time the player enters this map, so an instance field would always start as false.
    public static boolean cutsceneSeen = false;

    private boolean cutsceneStarted = false;

    public OutsideMap() {
        super("outside_map.txt", new OutsideTileset());

        if (cutsceneSeen) {
            // normal start (the cutscene only plays once): on the road in front of the gate
            this.playerStartPosition = getMapTile(14, ROAD_TILE_Y).getLocation();
        } else {
            // Cutscene start: the player waits off to the left of the screen (off camera) while the taxi drives in.
            // The camera is frozen on the road in front of the gate until the player steps out of the taxi.
            this.playerStartPosition = getMapTile(3, 10).getLocation();
            setAdjustCamera(false);

            float screenW = ScreenManager.getScreenWidth();
            float screenH = ScreenManager.getScreenHeight();
            float mapW = MAP_TILES_W * TILE_PX;
            float mapH = MAP_TILES_H * TILE_PX;
            float camX = Math.max(0, Math.min(mapW - screenW, 14 * TILE_PX + TILE_PX / 2f - screenW / 2f));
            float camY = Math.max(0, mapH - screenH);
            getCamera().setLocation(camX, camY);
        }
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        // taxi (id 1) and the ghost (id 2) are only used by IntroCutsceneScript
        npcs.add(new Taxi(1, getMapTile(TAXI_TILE_X, ROAD_TILE_Y).getLocation().addY(10)));

        Ghost ghost = new Ghost(2, getMapTile(14, 14).getLocation());
        ghost.setIsHidden(true);
        npcs.add(ghost);

        return npcs;
    }

    // Starts the opening cutscene on the first frame (a trigger would not work here, because the player
    // is parked off camera and triggers only fire while they are on screen).
    @Override
    public void update(Player player) {
        if (!cutsceneStarted && !cutsceneSeen) {
            cutsceneStarted = true;
            Script intro = new IntroCutsceneScript();
            intro.setMap(this);
            intro.setPlayer(player);
            intro.setListeners(listeners);
            intro.initialize();
            setActiveScript(intro);
        }
        super.update(player);
    }

    @Override
    public void loadScripts() {
        // front door -> lobby (the cutscene hands off to this script when the player reaches the door)
        getMapTile(14, 8).setInteractScript(new ChangeMapScript(LobbyMap2::new));
    }
}