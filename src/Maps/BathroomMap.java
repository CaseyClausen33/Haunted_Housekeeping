package Maps;

import Level.Map;
import Tilesets.BathroomTileset;

public class BathroomMap extends Map {

    public BathroomMap() {
        super("bathroom_map.txt", new BathroomTileset());
    }
}