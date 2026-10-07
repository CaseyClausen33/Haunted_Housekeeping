package Maps;

import Level.Map;
import Tilesets.BedroomTileset;
import Scripts.ChangeMapScript;

public class BedroomMap2 extends Map {

    public BedroomMap2() {
        super("bedroom_map2.txt", new BedroomTileset());
        this.playerStartPosition = getMapTile(1, 2).getLocation();
    }

    @Override 
    public void loadScripts() {
        getMapTile(2, 1).setInteractScript(new ChangeMapScript(LobbyMap2::new));
    }
}