package Maps;

import Tilesets.KitchenTileset;
import Level.Map;
import Puzzles.KitchenPuzzle;
import Scripts.KitchenPuzzleScript;
import Scripts.SimpleTextScript;
import Scripts.TestMap.TreeScript;

public class KitchenMap extends Map {
    public KitchenMap() {
        super("kitchen_map.txt", new KitchenTileset());
        this.playerStartPosition = getMapTile(17, 2).getLocation();
    }

    @Override
    public void loadScripts() {
        getMapTile(2, 9).setInteractScript(new KitchenPuzzleScript());
    }
}
