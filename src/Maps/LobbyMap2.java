package Maps;

import java.util.ArrayList;

import Level.Map;
import Level.NPC;
import Level.Player;
import Level.Script;
import NPCs.Ghost;
import Scripts.ChangeMapScript;
import Scripts.LobbyIntroScript;
import Scripts.TestMap.GhostScript;
import Tilesets.LobbyTileset2;

// Based on the LobbyMap2 you pasted earlier. Merge the new parts into your own copy if you have changed it since:
//   - lobbyIntroSeen + introStarted fields
//   - the ghost is hidden until the cutscene, and waits behind the front desk afterwards
//   - update() starts LobbyIntroScript the first time the lobby loads
//   - door scripts for the library and bedroom (the cutscene pans the camera to these doors)
public class LobbyMap2 extends Map {

    // true once the lobby introduction has played. It is static because a new LobbyMap2 object is created
    // every time the player enters the lobby, so an instance field would always start as false.
    public static boolean lobbyIntroSeen = false;

    private boolean introStarted = false;

    public LobbyMap2() {
        super("lobby_map2.txt", new LobbyTileset2());
        this.playerStartPosition = getMapTile(11, 12).getLocation();
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        // The lobby ghost stands behind the front desk (the desk is in row 4, the ghost is centered on column 5).
        // Before the intro cutscene it is hidden; LobbyIntroScript makes it appear.
        Ghost ghost = new Ghost(1, getMapTile(5, 4).getLocation().addX(-4).addY(-48));
        ghost.setIsHidden(!lobbyIntroSeen);
        ghost.setInteractScript(new GhostScript());
        npcs.add(ghost);

        return npcs;
    }

    // Starts the lobby introduction on the first frame of the first visit
    @Override
    public void update(Player player) {
        if (!introStarted && !lobbyIntroSeen) {
            introStarted = true;
            Script intro = new LobbyIntroScript();
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
        getMapTile(17, 1).setInteractScript(new ChangeMapScript(KitchenMap::new));
        getMapTile(22, 1).setInteractScript(new ChangeMapScript(BathroomMap2::new));

        // double door on the left: the library
        getMapTile(6, 1).setInteractScript(new ChangeMapScript(LibraryMap::new));
        getMapTile(7, 1).setInteractScript(new ChangeMapScript(LibraryMap::new));

        // room door on the far left: the bedroom
        getMapTile(2, 1).setInteractScript(new ChangeMapScript(BedroomMap2::new));
    }
}