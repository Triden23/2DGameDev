package Navigation;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class NavMesh {
    HashMap<Point, NavCube> navigationMesh;
    int worldSizeX, worldSizeY;
    public int range;
    int subDivideSize;
    GamePanel gp;

    public NavMesh(int worldSizeX, int worldSizeY, int subDivideSize, GamePanel gp) {
        this.worldSizeX = worldSizeX;
        this.worldSizeY = worldSizeY;
        this.gp = gp;

        // Ensure subDivideSize cleanly divides tileSize before using it
        while (gp.tileSize % subDivideSize != 0 && subDivideSize > 1) {
            subDivideSize--;  // Reduce until it becomes a clean divisor
        }

        this.subDivideSize = subDivideSize; // Assign the adjusted value
        this.range = (gp.tileSize / this.subDivideSize) - 1; // Now always valid
        this.navigationMesh = new HashMap<>();
    }

    public int getSubDivideSize() {
        return subDivideSize;
    }

    public void cleanOccupied() {
        for (NavCube cube : navigationMesh.values()) {
            if (cube.occupied) {  // Only modify the necessary ones
                cube.setOccupied(false);
            }
        }
    }

    public void bakeTerrainMesh() {
        for (int x = 0; x < worldSizeX; x++) {
            for (int y = 0; y < worldSizeY; y++) {
                for (int xf = 0; xf < subDivideSize; xf++) {
                    for (int yf = 0; yf < subDivideSize; yf++) {
                        int cubeX = x * gp.tileSize + (xf * (gp.tileSize / subDivideSize));
                        int cubeY = y * gp.tileSize + (yf * (gp.tileSize / subDivideSize));
                        int xRange = gp.tileSize / subDivideSize;
                        int yRange = gp.tileSize / subDivideSize;

                        NavCube navCube = new NavCube(cubeX, xRange, cubeY, yRange, gp.tileM.tile[gp.tileM.mapTileNum[x][y]].collision);
                        navigationMesh.put(new Point(x * subDivideSize + xf, y * subDivideSize + yf), navCube);
                    }
                }
            }
        }
    }

    public NavCube getNavCubeFromWorldCoords(int worldX, int worldY) {
        // Calculate grid position based on world coordinates
        int gridX = worldX / (gp.tileSize / subDivideSize);
        int gridY = worldY / (gp.tileSize / subDivideSize);

        // Create the point to fetch the NavCube from the HashMap
        Point key = new Point(gridX, gridY);

        // Retrieve the NavCube from the map
        return navigationMesh.get(key);
    }

    public void entityPass(List<Entity> e) {
        for (Entity index : e) {

            int entityX = index.location.getWorldX() + index.solidAreaDefaultX;
            int entityY = index.location.getWorldY() + index.solidAreaDefaultY;
            int entityWidth = index.solidArea.width;
            int entityHeight = index.solidArea.height;
            NavCube cube;


            int bufferSize = range; // One cube size from range


            int checkStartX = (entityX - bufferSize) / (gp.tileSize / subDivideSize); // Start x of the check area
            int checkStartY = (entityY - bufferSize) / (gp.tileSize / subDivideSize); // Start y of the check area
            int checkEndX = (entityX + entityWidth + bufferSize) / (gp.tileSize / subDivideSize); // End x of the check area
            int checkEndY = (entityY + entityHeight + bufferSize) / (gp.tileSize / subDivideSize); // End y of the check area


            for (int x = checkStartX; x <= checkEndX; x++) {
                for (int y = checkStartY; y <= checkEndY; y++) {

                    Point cubePoint = new Point(x, y);


                    if (navigationMesh.containsKey(cubePoint)) {

                        cube = navigationMesh.get(cubePoint);
                        if(!cube.occupied) {
                            cube.setOccupied(gp.collisionManager.navigationCubeCheck(index, cube.rect));
                        }
                    }
                }
            }
        }
    }

    public boolean checkIfPathable(int x, int y) {
        NavCube cube = navigationMesh.get(new Point(x, y));
        return cube != null && cube.isPathable();
    }


    public void setCubeNeighbors(){
        int[] directions = {-1, 1}; // left/right (x) and up/down (y)
        for (NavCube currentCube : navigationMesh.values()) {
            List<NavCube> temp = new ArrayList<>();
            // Check the 4 cardinal directions for neighbors
            int x = currentCube.rect.x;
            int y = currentCube.rect.y;

            for (int dx : directions) {
                for (int dy : directions) {
                    int newX = x + dx * (gp.tileSize / subDivideSize);
                    int newY = y + dy * (gp.tileSize / subDivideSize);

                    NavCube neighbor = getNavCubeFromWorldCoords(newX, newY);
                    if (neighbor != null && neighbor.isPathable()) {
                        temp.add(neighbor);
                    }
                }
            }
            currentCube.setNeighbors(temp);
        }
    }

    public void debugDraw(Graphics2D g2) {
        Color c = g2.getColor();
        float f = 0.4f;

        int screenX;
        int screenY;

        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, f);
        g2.setComposite(composite);

        // Loop through each NavCube in the navigationMesh and fill its rectangle if it's not pathable
        for (NavCube cube : navigationMesh.values()) {
            screenX = cube.rect.x - gp.player.location.getWorldX() + gp.player.screenX;
            screenY = cube.rect.y - gp.player.location.getWorldY() + gp.player.screenY;
            if (!cube.isPathable()) {
                g2.setColor(Color.RED);
            }else{
                g2.setColor(Color.GREEN);
            }
            g2.fillRect(screenX, screenY, cube.rect.width, cube.rect.height);
        }

        // Restore the original opacity and color
        f = 1.0f;
        composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, f);
        g2.setComposite(composite);
        g2.setColor(c);
    }

}

