package Maps;

import java.util.ArrayList;

import Level.Map;
import Level.NPC;
import NPCs.LibraryGhost;
import Scripts.Match2PuzzleScript;
import Scripts.TestMap.LibraryGhostScript;
import Tilesets.HotelTileset;

public class LibraryMap extends Map {

    public LibraryMap() {
        super("library_map.txt", new HotelTileset());
        this.playerStartPosition = getMapTile(5, 10).getLocation();
    }

    @Override
    public void loadScripts() {
        getMapTile(4, 8).setInteractScript(new Match2PuzzleScript());
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        LibraryGhost ghost = new LibraryGhost(3, getMapTile(10, 6).getLocation().subtractX(20));
        ghost.setInteractScript(new LibraryGhostScript());
        npcs.add(ghost);

        return npcs;
    }
}
