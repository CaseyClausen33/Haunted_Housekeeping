package MapEditor;

import Level.Map;
import Maps.TestMap;
import Maps.TitleScreenMap;
import Maps.HotelLobbyMap;
import Maps.NewMap;
import Maps.KitchenMap;
import Maps.BathroomMap;
import Maps.CleanKitchenMap;

import java.util.ArrayList;

public class EditorMaps {
    public static ArrayList<String> getMapNames() {
        return new ArrayList<String>() {{
            add("TestMap");
            add("TitleScreen");
            add("HotelLobbyMap");
            add("newMap");
            add("KitchenMap");
            add("BathroomMap");
            add("CleanKitchenMap");
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
            case "BathroomMap":
                return new BathroomMap();
            case "CleanKitchenMap":
                return new CleanKitchenMap();
            default:
                throw new RuntimeException("Unrecognized map name");
        }
    }
}
