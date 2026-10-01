package MapEditor;

import Level.Map;
import Maps.TestMap;
import Maps.TitleScreenMap;
import Maps.HotelLobbyMap;
import Maps.NewMap;
import Maps.KitchenMap;

import java.util.ArrayList;

public class EditorMaps {
    public static ArrayList<String> getMapNames() {
        return new ArrayList<String>() {{
            add("TestMap");
            add("TitleScreen");
            add("HotelLobbyMap");
            add("newMap");
            add("KitchenMap");
        }};
    }

    public static Map getMapByName(String mapName) {
        switch(mapName) {
            case "TestMap":
                return new TestMap();
            case "TitleScreen":
                return new TitleScreenMap();
            case "HotelLobbyMap":
                return new HotelLobbyMap();
            case "newMap":
                return new NewMap();
            case "KitchenMap":
                return new KitchenMap();
            default:
                throw new RuntimeException("Unrecognized map name");
        }
    }
}
