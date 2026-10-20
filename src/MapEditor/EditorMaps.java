package MapEditor;

import Level.Map;
import Maps.BathroomMap2;
import Maps.BedroomMap2;
import Maps.CleanKitchenMap;
import Maps.FinalRoomMap;
import Maps.KitchenMap;
import Maps.LibraryMap2;
import Maps.LobbyMap2;
import Maps.NewMap;
import Maps.OutsideMap;
import Maps.TestMap;
import Maps.TitleScreenMap;
import java.util.ArrayList;

public class EditorMaps {
    public static ArrayList<String> getMapNames() {
        return new ArrayList<String>() {{
            add("TestMap");
            add("TitleScreen");
            add("HotelLobbyMap");
            add("newMap");
            add("KitchenMap");
            add("CleanKitchenMap");
            add("LobbyMap2");
            add("BathroomMap2");
            add("LibraryMap2");
            add("BedroomMap2");
            add("OutsideMap");
            add("FinalRoomMap");
        }};
    }


    public static Map getMapByName(String mapName) {
        switch(mapName) {
            case "TestMap":
                return new TestMap();
            case "TitleScreen":
                return new TitleScreenMap();
            case "newMap":
                return new NewMap();
            case "KitchenMap":
                return new KitchenMap();
            case "CleanKitchenMap":
                return new CleanKitchenMap();
            case "LobbyMap2":
                return new LobbyMap2();
            case "BathroomMap2":
                return new BathroomMap2();
            case "LibraryMap2":
                return new LibraryMap2();
            case "BedroomMap2":
                return new BedroomMap2();
            case "OutsideMap":
                return new OutsideMap();
            case "FinalRoomMap":
                return new FinalRoomMap();
            default:
                throw new RuntimeException("Unrecognized map name");
        }
    }
}
