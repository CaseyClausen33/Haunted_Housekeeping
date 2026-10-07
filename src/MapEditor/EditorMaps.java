package MapEditor;

import Level.Map;
import Maps.TestMap;
import Maps.TitleScreenMap;
import Maps.HotelLobbyMap;
import Maps.LibraryMap;
import Maps.NewMap;
import Maps.KitchenMap;
import Maps.BathroomMap;
import Maps.BathroomMap2;
import Maps.CleanKitchenMap;
import Maps.LobbyMap2;
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
            add("LibraryMap");
            add("CleanKitchenMap");
            add("LobbyMap2");
            add("BathroomMap2");
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
            case "LibraryMap":
                return new LibraryMap();
            case "CleanKitchenMap":
                return new CleanKitchenMap();
            case "LobbyMap2":
                return new LobbyMap2();
            case "BathroomMap2":
                return new BathroomMap2();
            default:
                throw new RuntimeException("Unrecognized map name");
        }
    }
}
