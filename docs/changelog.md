* Added ChangeMapScript.java to add a script to change from one map to another
* Added loadMap function into PlayLevelScreen.java to set the map, player start position, and scripts
* Created new\_map.txt and NewMap.java to create a second map to transfer between, use the tile that appears out of place to transfer back to TestMap
* Added script to TestMap.java (replacing the initial sign) to the door to change from NewMap to TitleScreenMap
* Added script to NewMap.java to the door (add location of door) to change from NewMap to TestMap
* Fixed bug where map freezes if trying to code a transition that loops between two maps:

  * Use (MapName)::new instead of new MapName to pass the constructor for Map objects without creating the map yet, fixes unneeded maps loading indefinitely

