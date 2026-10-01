package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;
import java.util.ArrayList;

public class HotelTileset extends Tileset {

    public HotelTileset() {
        super(ImageLoader.load("HotelTilesetNormalized.png"), 128, 128, 1);
    }

    @Override 
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();


        // wood1
        Frame wood1Frame = new FrameBuilder(getSubImage(0, 0))
                .withScale(tileScale)
                .withBounds(0, 0, 128, 128)
                .build();
        MapTileBuilder wood1 = new MapTileBuilder(wood1Frame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(wood1);

        
        // wood2
        Frame wood2Frame = new FrameBuilder(getSubImage(0, 1))
                .withScale(tileScale)
                .withBounds(0, 0, 128, 128)
                .build();
        MapTileBuilder wood2 = new MapTileBuilder(wood2Frame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(wood2);

        // wood3
        Frame wood3Frame = new FrameBuilder(getSubImage(0, 3))
                .withScale(tileScale)
                .withBounds(0, 0, 128, 128)
                .build();
        MapTileBuilder wood3 = new MapTileBuilder(wood3Frame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(wood3);


        // woodWall1
        Frame woodWall1Frame = new FrameBuilder(getSubImage(1, 0))
                .withScale(tileScale)
                .withBounds(0, 0, 128, 128)
                .build();
        MapTileBuilder woodWall1 = new MapTileBuilder(woodWall1Frame)
                .withTileType(TileType.NOT_PASSABLE);
        mapTiles.add(woodWall1);

        // woodWall2
        Frame woodWall2Frame = new FrameBuilder(getSubImage(1, 1))
                .withScale(tileScale)
                .withBounds(0, 0, 128, 128)
                .build();
        MapTileBuilder woodWall2 = new MapTileBuilder(woodWall2Frame)
                .withTileType(TileType.NOT_PASSABLE);
        mapTiles.add(woodWall2);

        // woodWall3
        Frame woodWall3Frame = new FrameBuilder(getSubImage(1, 2))
                .withScale(tileScale)
                .withBounds(0, 0, 128, 128)
                .build();
        MapTileBuilder woodWall3 = new MapTileBuilder(woodWall3Frame)
                .withTileType(TileType.NOT_PASSABLE);
        mapTiles.add(woodWall3);

        // stone
        Frame stoneFrame = new FrameBuilder(getSubImage(2, 0))
                .withScale(tileScale)
                .withBounds(0, 0, 128, 128)
                .build();
        MapTileBuilder stone = new MapTileBuilder(stoneFrame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(stone);

        // stone2
        Frame stone2Frame = new FrameBuilder(getSubImage(2, 1))
                .withScale(tileScale)
                .withBounds(0, 0, 128, 128)
                .build();
        MapTileBuilder stone2 = new MapTileBuilder(stone2Frame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(stone2);

        // redCarpet
        Frame redCarpetFrame = new FrameBuilder(getSubImage(0, 4))
                .withScale(tileScale)
                .withBounds(0, 0, 128, 128)
                .build();
        MapTileBuilder redCarpet = new MapTileBuilder(redCarpetFrame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(redCarpet);

        // greenCarpet
        Frame greenCarpetFrame = new FrameBuilder(getSubImage(0, 5))
                .withScale(tileScale)
                .withBounds(0, 0, 128, 128)
                .build();
        MapTileBuilder greenCarpet = new MapTileBuilder(greenCarpetFrame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(greenCarpet);

        // blueCarpet
        Frame blueCarpetFrame = new FrameBuilder(getSubImage(0, 6))
                .withScale(tileScale)
                .withBounds(0, 0, 128, 128)
                .build();
        MapTileBuilder blueCarpet = new MapTileBuilder(blueCarpetFrame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(blueCarpet);

        // door
        Frame doorFrame = new FrameBuilder(getSubImage(3, 0))
                .withScale(tileScale)
                .withBounds(0, 0, 128, 128)
                .build();
        MapTileBuilder door = new MapTileBuilder(doorFrame)
                .withTileType(TileType.NOT_PASSABLE);
        mapTiles.add(door);

         
        // decoration
        Frame decorationFrame = new FrameBuilder(getSubImage(1, 7))
                .withScale(tileScale)
                .withBounds(0, 0, 128, 128)
                .build();
        MapTileBuilder decoration = new MapTileBuilder(decorationFrame)
                .withTileType(TileType.NOT_PASSABLE);
        mapTiles.add(decoration);
         
        // vertical wall
        Frame verticalPillarFrame = new FrameBuilder(getSubImage(4, 5))
                .withScale(tileScale)
                .withBounds(0, 0, 128, 128)
                .build();
        MapTileBuilder verticalPillar = new MapTileBuilder(verticalPillarFrame)
                .withTileType(TileType.NOT_PASSABLE);
        mapTiles.add(verticalPillar);

        // regalDoor
        Frame regalDoorFrame = new FrameBuilder(getSubImage(7, 6))
                .withScale(tileScale)
                .withBounds(0, 0, 128, 128)
                .build();
        MapTileBuilder regalDoor = new MapTileBuilder(regalDoorFrame)
                .withTileType(TileType.NOT_PASSABLE);
        mapTiles.add(regalDoor);

        
        return mapTiles;

    }
}
