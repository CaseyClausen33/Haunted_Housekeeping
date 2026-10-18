package Maps;

import java.util.ArrayList;

import Level.Map;
import Level.NPC;
import Level.Trigger;
import NPCs.Ghost;
import NPCs.Taxi;
// import Puzzles.RoomProgress;
import Scripts.ChangeMapScript;
import Scripts.IntroCutsceneScript;
import Tilesets.OutsideTileset;
import Utils.Point;

public class OutsideMap extends Map {

    // The taxi starts on the road six tiles left of the gate; the player starts hidden behind it.
    private static final int TAXI_TILE_X = 6;
    private static final int ROAD_TILE_Y = 20;
    public static Boolean OutsideCutsceneSeen = false;

    public OutsideMap() {
        super("outside_map.txt", new OutsideTileset());
        this.playerStartPosition = getMapTile(TAXI_TILE_X, ROAD_TILE_Y).getLocation().addX(28).addY(14);
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

    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();

        // the player starts inside this trigger, so the cutscene begins as soon as the map loads
        if (!OutsideCutsceneSeen) {
            Point start = getMapTile(TAXI_TILE_X, ROAD_TILE_Y).getLocation().addX(28).addY(14);
            triggers.add(new Trigger(start.x, start.y, 72, 72, new IntroCutsceneScript()));
        }

        return triggers;
    }

    @Override
    public void loadScripts() {
        // front door -> lobby (the cutscene hands off to this script when the player reaches the door)
        getMapTile(14, 8).setInteractScript(new ChangeMapScript(LobbyMap2::new));
    }
}