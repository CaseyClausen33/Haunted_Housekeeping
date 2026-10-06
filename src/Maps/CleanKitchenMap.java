package Maps;

import Level.Map;
import Tilesets.CleanKitchenTileset;

public class CleanKitchenMap extends Map {

    public CleanKitchenMap() {
        super("cleankitchen_map.txt", new CleanKitchenTileset());
    }
}