package MapEditor;

import Level.Map;
import Maps.TestMap;
import Maps.TitleScreenMap;
import Maps.HotelLobbyMap;
import Maps.LibraryMap;
import Maps.NewMap;
import Maps.KitchenMap;
import Maps.BedroomMap;
import Maps.BathroomMap;
import Maps.CleanKitchenMap;
import Maps.CleanBathroomMap;

import java.util.ArrayList;

public class EditorMaps {
    public static ArrayList<String> getMapNames() {
        return new ArrayList<String>() {{
            add("TestMap");
            add("TitleScreen");
            add("HotelLobbyMap");
            add("newMap");
            add("KitchenMap");
            add("BedroomMap");
            add("BathroomMap");
            add("LibraryMap");
            add("CleanKitchenMap");
            add("CleanBathroomMap");
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
            case "BedroomMap":
                return new BedroomMap();
            case "BathroomMap":
                return new BathroomMap();
            case "LibraryMap":
                return new LibraryMap();
            case "CleanKitchenMap":
                return new CleanKitchenMap();
            case "CleanBathroomMap":
                return new CleanBathroomMap();
            default:
                throw new RuntimeException("Unrecognized map name");
        }
    }
}
