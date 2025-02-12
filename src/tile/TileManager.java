package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import javax.sound.sampled.Line;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        // represents the number tiles we can have [water,grass,wall, etc.....]
        tile = new Tile[50];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/world01.txt");
    }

    public void getTileImage(){

        //We are going to reserve 0-9 tile indexes so map files look easy

        /*
        setup(0, "grass", false);
        setup(1, "wall", true);
        setup(2, "water", true);
        setup(3, "earth", false);
        setup(4, "tree", true);
        setup(5, "sand", false);
        */

        //I need the ability to load tiles sets per level.

        //Reserved so map making is easier on the eyes
        setup(0, "earth", false);
        setup(1, "grass", false);


        setup(2, "path_00", false);
        setup(3, "path_01", false);
        setup(4, "path_02", false);
        setup(5, "path_03", false);
        setup(6, "path_04", false);
        setup(7, "path_05", false);
        setup(8, "path_06", false);
        setup(9, "path_07", false);
        setup(10, "path_08", false);
        setup(11, "path_09", false);
        setup(12, "path_10", false);
        setup(13, "path_11", false);
        setup(14, "path_12", false);
        setup(15, "path_13", false);
        setup(16, "path_14", false);


        setup(17, "sand", false);
        setup(18, "tree", true);
        setup(19, "wall", true);

        setup(20, "water", true);
        setup(21, "water_00", true);
        setup(22, "water_01", true);
        setup(23, "water_02", true);
        setup(24, "water_03", true);
        setup(25, "water_04", true);
        setup(26, "water_05", true);
        setup(27, "water_06", true);
        setup(28, "water_07", true);
        setup(29, "water_08", true);
        setup(30, "water_09", true);
        setup(31, "water_10", true);
        setup(32, "water_11", true);
        setup(33, "water_12", true);
        setup(34, "water_13", true);


    }

    public void setup(int index, String imagePath, boolean collision){

        UtilityTool uTool = new UtilityTool();

        try{

            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/" + imagePath + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;


        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow){

                String line = br.readLine();

                while(col < gp.maxWorldCol) {

                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }

            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){

        /*g2.drawImage(tile[0].image,0, 0, gp.tileSize, gp.tileSize, null);
        g2.drawImage(tile[1].image,48, 0, gp.tileSize, gp.tileSize, null);
        g2.drawImage(tile[2].image,96, 0, gp.tileSize, gp.tileSize, null);*/

        int worldCol = 0;
        int worldRow = 0;


        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

            int tileNum = mapTileNum[worldCol][worldRow];


            //Where the tile is on the map
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            //Where on the screen the camera draws it based on players position relative to the tile
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            //this if acts like a render distance with a buffer of 1 tile in each direction to smooth camera rendering
            if(worldX + gp.tileSize> gp.player.worldX - gp.player.screenX &&
               worldX - gp.tileSize< gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize> gp.player.worldY - gp.player.screenY &&
               worldY - gp.tileSize< gp.player.worldY + gp.player.screenY) {

                g2.drawImage(tile[tileNum].image, screenX, screenY,null);

            }

            worldCol++;

            if(worldCol == gp.maxWorldCol){

                worldCol = 0;
                worldRow++;

            }

        }

    }

}
