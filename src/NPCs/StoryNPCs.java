package NPCs;

import java.util.ArrayList;

import Level.Map;
import Level.NPC;
import Puzzles.StoryProgress;
import Utils.Point;

// Creates the NPCs the story scenes need, so a map only has to make one call.
// NPC ids used by the story: 4 kitchen ghost, 5 Evelyn, 6 Arthur, 7-10 hearts, 20-31 guests.
public class StoryNPCs {

    public static final int KITCHEN_GHOST = 4;
    public static final int EVELYN = 5;
    public static final int ARTHUR = 6;
    public static final int[] HEARTS = {7, 8, 9, 10};

    // Tiles (x, y) in the lobby where the guests stand in the final shot
    private static final int[][] GUEST_SPOTS = {
            {3, 5}, {4, 5}, {5, 5}, {6, 5},          // queue at the front desk
            {13, 3}, {14, 3}, {18, 3}, {20, 3},      // by the sofas and the front doors
            {9, 9}, {13, 9}, {11, 10}, {8, 11}       // chatting on the rug
    };

    // For the BOSS ROOM map: hidden Evelyn, Arthur and hearts. Call it from loadNPCs().
    // 'where' can be any point on the map; the scenes move the characters next to the player.
    public static void addStoryGhosts(ArrayList<NPC> npcs, Point where) {
        Evelyn evelyn = new Evelyn(EVELYN, where);
        evelyn.setIsHidden(true);
        npcs.add(evelyn);

        GrandfatherGhost arthur = new GrandfatherGhost(ARTHUR, where);
        arthur.setIsHidden(true);
        npcs.add(arthur);

        for (int id : HEARTS) {
            Heart heart = new Heart(id, where);
            heart.setIsHidden(true);
            npcs.add(heart);
        }
    }

    // For the LOBBY map: everyone in the final shot (the guests, Evelyn, Arthur and the kitchen ghost).
    // They are only visible while StoryProgress.epilogueActive is true. Call it from loadNPCs().
    public static void addLobbyCast(ArrayList<NPC> npcs, Map map) {
        boolean show = StoryProgress.epilogueActive;

        Evelyn evelyn = new Evelyn(EVELYN, tile(map, 5, 3));
        evelyn.setIsHidden(!show);
        npcs.add(evelyn);

        GrandfatherGhost arthur = new GrandfatherGhost(ARTHUR, tile(map, 19, 3));
        arthur.setIsHidden(!show);
        npcs.add(arthur);

        KitchenGhost kitchenGhost = new KitchenGhost(KITCHEN_GHOST, tile(map, 13, 6));
        kitchenGhost.setIsHidden(!show);
        npcs.add(kitchenGhost);

        for (int id : HEARTS) {
            Heart heart = new Heart(id, tile(map, 11, 7));
            heart.setIsHidden(true);
            npcs.add(heart);
        }

        for (int i = 0; i < GUEST_SPOTS.length; i++) {
            Guest guest = new Guest(20 + i, tile(map, GUEST_SPOTS[i][0], GUEST_SPOTS[i][1]), i % 6 + 1, GUEST_SPOTS[i][0] > 10);
            guest.setIsHidden(!show);
            npcs.add(guest);
        }
    }

    // a 72px-wide NPC standing on a 64px tile: centered, with its feet near the bottom of the tile
    private static Point tile(Map map, int x, int y) {
        return map.getMapTile(x, y).getLocation().addX(-4).addY(-2);
    }
}