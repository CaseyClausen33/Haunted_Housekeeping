package Maps;

import Tilesets.KitchenTileset;
import Level.Map;

public class KitchenMap extends Map {
    public KitchenMap() {
        super("kitchen_map.txt", new KitchenTileset());
        this.playerStartPosition = getMapTile(5, 2).getLocation();
    }
}
